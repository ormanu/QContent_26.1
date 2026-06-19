package ormanu.qcontent.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import ormanu.qcontent.config.QConfig;
import ormanu.qcontent.screen.MagnetFilterHopperMenu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class MagnetItem extends Item {

    private static final double MAGNET_RANGE = QConfig.magnet_range;
    private static final int TICK_INTERVAL = 4;
    private static final int MAX_ITEMS = 64;
    private static final int MAX_XP_ORBS = 64;
    private static final int FILTER_SIZE = 5;

    public MagnetItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {

            // SHIFT + Right-click = Open filter GUI (only in main hand)
            if (player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
                openFilterGui(serverPlayer, stack);
                return InteractionResult.SUCCESS;
            }

            // Normal right-click = Toggle magnet
            toggleMagnet(level, player, stack);
        }

        return InteractionResult.SUCCESS;
    }

    private void toggleMagnet(Level level, Player player, ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        boolean isActive = tag.getBoolean("MagnetActive").orElse(false);

        CustomData.update(DataComponents.CUSTOM_DATA, stack, t -> {
            t.putBoolean("MagnetActive", !isActive);
        });

        level.playSound(null, player.blockPosition(),
                isActive ? SoundEvents.BEACON_DEACTIVATE : SoundEvents.BEACON_ACTIVATE,
                SoundSource.PLAYERS, 0.5F, 1.5F);

        Component msg = Component.literal(isActive ? "✖ Magnet: OFF" : "✔ Magnet: ON")
                .withStyle(isActive ? ChatFormatting.RED : ChatFormatting.GREEN);
        player.sendOverlayMessage(msg);
    }

    private void openFilterGui(ServerPlayer player, ItemStack magnet) {
        int magnetSlotIndex = player.getInventory().getSelectedSlot();

        // Create 5-slot filter container
        SimpleContainer filterContainer = new SimpleContainer(FILTER_SIZE);

        // Load current blacklist into the 5 slots
        Set<String> blacklist = getBlacklist(magnet);
        int slot = 0;
        for (String id : blacklist) {
            if (slot >= FILTER_SIZE) break;
            try {
                Identifier identifier = Identifier.parse(id);
                var item = BuiltInRegistries.ITEM.getValue(identifier);
                if (item != null) {
                    filterContainer.setItem(slot, new ItemStack(item, 1));
                    slot++;
                }
            } catch (Exception ignored) {
            }
        }

        // Open the hopper menu
        player.openMenu(new SimpleMenuProvider(
                (containerId, playerInventory, p) ->
                        new MagnetFilterHopperMenu(containerId, playerInventory, filterContainer, magnetSlotIndex),
                Component.literal("Magnet Filter")
        ));
    }

    @Override
    public void inventoryTick(final @NonNull ItemStack itemStack, final ServerLevel level, final @NonNull Entity owner, final @Nullable EquipmentSlot slot) {
        if (level.isClientSide()) return;
        if (!(owner instanceof Player player)) return;
        if (player.isSpectator() || player.isDeadOrDying()) return;
        if (player.tickCount % TICK_INTERVAL != 0) return;

        CustomData customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        boolean isActive = customData.copyTag().getBoolean("MagnetActive").orElse(false);

        if (!isActive) return;

        // Load blacklist
        Set<String> blacklist = getBlacklist(itemStack);

        AABB area = player.getBoundingBox().inflate(MAGNET_RANGE);

        // Instant pickup items (skip blacklisted)
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, area);
        int itemCount = 0;
        for (ItemEntity itemEntity : items) {
            if (itemCount >= MAX_ITEMS) break;
            if (itemEntity.hasPickUpDelay()) continue;
            if (!itemEntity.isAlive()) continue;

            // Check blacklist
            Identifier itemId = BuiltInRegistries.ITEM.getKey(itemEntity.getItem().getItem());
            if (blacklist.contains(itemId.toString())) continue;

            itemEntity.setPos(player.getX(), player.getY(), player.getZ());
            itemEntity.playerTouch(player);
            itemCount++;
        }

        // Instant pickup XP orbs (always allowed)
        List<ExperienceOrb> xpOrbs = level.getEntitiesOfClass(ExperienceOrb.class, area);
        int xpCount = 0;
        for (ExperienceOrb xpOrb : xpOrbs) {
            if (xpCount >= MAX_XP_ORBS) break;
            if (!xpOrb.isAlive()) continue;

            xpOrb.setPos(player.getX(), player.getY(), player.getZ());
            xpOrb.playerTouch(player);
            xpCount++;
        }
    }

    // ===== BLACKLIST HELPERS =====

    public static Set<String> getBlacklist(ItemStack stack) {
        Set<String> blacklist = new HashSet<>();
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        if (tag.contains("Blacklist")) {
            ListTag listTag = tag.getList("Blacklist").orElse(new ListTag());
            for (int i = 0; i < listTag.size(); i++) {
                String id = listTag.getString(i).orElse("");
                if (!id.isEmpty()) {
                    blacklist.add(id);
                }
            }
        }

        return blacklist;
    }

    public static void saveBlacklist(ItemStack stack, Set<String> blacklist) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            ListTag listTag = new ListTag();
            for (String id : blacklist) {
                listTag.add(StringTag.valueOf(id));
            }
            tag.put("Blacklist", listTag);
        });
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.copyTag().getBoolean("MagnetActive").orElse(false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent,
                                Consumer<Component> textConsumer, TooltipFlag type) {

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        boolean isActive = customData.copyTag().getBoolean("MagnetActive").orElse(false);
        Set<String> blacklist = getBlacklist(stack);

        textConsumer.accept(Component.literal("Instantly picks up nearby items & XP")
                .withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.literal("Range: " + (int) MAGNET_RANGE + " blocks")
                .withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.empty());

        if (isActive) {
            textConsumer.accept(Component.literal("✔ Active")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            textConsumer.accept(Component.literal("✖ Inactive")
                    .withStyle(ChatFormatting.RED));
        }

        if (!blacklist.isEmpty()) {
            textConsumer.accept(Component.literal("Blacklisted: " + blacklist.size() + " items")
                    .withStyle(ChatFormatting.YELLOW));
        }

        textConsumer.accept(Component.empty());
        textConsumer.accept(
                Component.literal("Toggle: ")
                        .withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal("[")
                                .withStyle(ChatFormatting.DARK_GRAY))
                        .append(Component.keybind("key.qcontent.toggle_magnet")
                                .withStyle(ChatFormatting.LIGHT_PURPLE))
                        .append(Component.literal("]")
                                .withStyle(ChatFormatting.DARK_GRAY))
                        .append(Component.literal(" or Right-click")
                                .withStyle(ChatFormatting.DARK_GRAY))
        );
        textConsumer.accept(
                Component.literal("Filter: ")
                        .withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal("Sneak + Right-click")
                                .withStyle(ChatFormatting.DARK_GRAY))
        );
    }
}