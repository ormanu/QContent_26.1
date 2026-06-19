package ormanu.qcontent.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import ormanu.qcontent.enchantment.ModEnchantments;
import ormanu.qcontent.screen.BackpackMenu;

import java.util.function.Consumer;

public class BackpackItem extends Item {

    private static final int BASE_SLOTS = 27;
    private static final int STEP = 9;
    private static final int MAX_SLOTS = 54;

    public BackpackItem(Properties properties) {
        super(properties);
    }

    /* =========================
       SLOT CALCULATION
       ========================= */

    public static int getSlots(ItemStack stack) {
        int level = getDeepPocketsLevel(stack);
        int slots = BASE_SLOTS + (level * STEP);
        return Math.min(slots, MAX_SLOTS);
    }

    private static int getDeepPocketsLevel(ItemStack stack) {
        var enchants = stack.getEnchantments();

        for (Holder<Enchantment> ench : enchants.keySet()) {
            if (ench.is(ModEnchantments.DEEP_POCKETS)) {
                return enchants.getLevel(ench);
            }
        }
        return 0;
    }


    public static int getRows(ItemStack stack) {
        return getSlots(stack) / 9;
    }

    /* =========================
       OPEN BACKPACK
       ========================= */

    @Override
    public @NonNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            int slotIndex = player.getInventory().getSelectedSlot();

            int slots = getSlots(stack);
            int rows = getRows(stack);

            SimpleContainer container = new SimpleContainer(slots);
            loadItemsPreserveMax(stack, container);

            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, p) ->
                            new BackpackMenu(containerId, playerInventory, container, slotIndex, rows),
                    stack.getHoverName()
            ));

            level.playSound(null, player.blockPosition(),
                    SoundEvents.BUNDLE_INSERT,
                    SoundSource.PLAYERS,
                    0.5F,
                    1.0F);
        }

        return InteractionResult.SUCCESS;
    }

    /* =========================
       SAFE STORAGE (54 backing)
       ========================= */

    public static void loadItemsPreserveMax(ItemStack backpack, SimpleContainer gui) {
        NonNullList<ItemStack> all = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);

        ItemContainerContents contents = backpack.getOrDefault(
                DataComponents.CONTAINER,
                ItemContainerContents.EMPTY
        );

        contents.copyInto(all);

        for (int i = 0; i < gui.getContainerSize(); i++) {
            gui.setItem(i, all.get(i));
        }
    }

    public static void saveItemsPreserveMax(ItemStack backpack, SimpleContainer gui) {
        NonNullList<ItemStack> all = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);

        ItemContainerContents contents = backpack.getOrDefault(
                DataComponents.CONTAINER,
                ItemContainerContents.EMPTY
        );

        contents.copyInto(all);

        for (int i = 0; i < gui.getContainerSize(); i++) {
            all.set(i, gui.getItem(i));
        }

        backpack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(all));
    }

    /* =========================
       TOOLTIP
       ========================= */

    public static int countItems(ItemStack backpack) {
        ItemContainerContents contents = backpack.getOrDefault(
                DataComponents.CONTAINER,
                ItemContainerContents.EMPTY
        );

        return (int) contents.nonEmptyItemCopyStream().count();
    }

    @Override
    public void appendHoverText(ItemStack stack,
                                TooltipContext context,
                                TooltipDisplay displayComponent,
                                Consumer<Component> textConsumer,
                                TooltipFlag type) {

        int slots = getSlots(stack);
        int level = getDeepPocketsLevel(stack);

        textConsumer.accept(Component.literal("Portable storage")
                .withStyle(ChatFormatting.GRAY));

        textConsumer.accept(Component.literal("Slots: " + slots + "/" + MAX_SLOTS)
                .withStyle(ChatFormatting.GRAY));

        if (level > 0) {
            textConsumer.accept(Component.literal("Deep Pockets +" + (level * 9) + " Slots")
                    .withStyle(ChatFormatting.AQUA));
        }

        textConsumer.accept(Component.empty());

        textConsumer.accept(Component.literal("Right-click to open")
                .withStyle(ChatFormatting.DARK_GRAY));
    }
}