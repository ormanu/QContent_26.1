package ormanu.qcontent.network;

import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketInventory;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import ormanu.qcontent.items.custom.BackpackItem;
import ormanu.qcontent.items.custom.MagnetItem;
import ormanu.qcontent.items.custom.RefinedHelmetItem;
import ormanu.qcontent.screen.BackpackMenu;
import ormanu.qcontent.screen.BackpackTrinketMenu;

import java.util.Optional;

public class ModNetworks {

    private static ItemStack findTrinketBackpack(ServerPlayer player) {
        TrinketAttachment attachment = TrinketsApi.getAttachment(player);
        if (attachment == null) {
            return ItemStack.EMPTY;
        }

        for (TrinketInventory inventory : attachment.getInventories().values()) {
            if (inventory.slotType().isVanityOnly()) {
                continue;
            }

            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                TrinketSlotAccess slotAccess = inventory.getOrCreateSlotAccess(slot);
                ItemStack stack = slotAccess.get();

                if (stack.getItem() instanceof BackpackItem) {
                    return stack;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    private static void openFromInventory(ServerPlayer player, ItemStack backpack, int slot) {
        int slots = BackpackItem.getSlots(backpack);
        int rows  = BackpackItem.getRows(backpack);

        SimpleContainer container = new SimpleContainer(slots);
        BackpackItem.loadItemsPreserveMax(backpack, container);

        player.openMenu(new SimpleMenuProvider(
                (containerId, playerInventory, p) ->
                        new BackpackMenu(containerId, playerInventory, container, slot, rows),
                backpack.getHoverName()
        ));
    }

    private static void openFromTrinket(ServerPlayer player, ItemStack backpack) {
        int slots = BackpackItem.getSlots(backpack);
        int rows  = BackpackItem.getRows(backpack);

        SimpleContainer container = new SimpleContainer(slots);
        BackpackItem.loadItemsPreserveMax(backpack, container);

        player.openMenu(new SimpleMenuProvider(
                (containerId, playerInventory, p) ->
                        new BackpackTrinketMenu(containerId, playerInventory, container, rows, backpack),
                backpack.getHoverName()
        ));
    }

    public static void initialize() {
        // Register the payload type
        PayloadTypeRegistry.serverboundPlay().register(ToggleHoodPayload.TYPE, ToggleHoodPayload.CODEC);

        // Handle the packet on the server
        ServerPlayNetworking.registerGlobalReceiver(ToggleHoodPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                Player player = context.player();
                ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);

                // Only toggle if wearing the refined helmet
                if (headStack.getItem() instanceof RefinedHelmetItem) {
                    CustomData customData = headStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                    CompoundTag tag = customData.copyTag();

                    boolean isHoodDown = tag.getBoolean("HoodDown").orElse(false);

                    // Toggle the state
                    CustomData.update(DataComponents.CUSTOM_DATA, headStack, t -> {
                        t.putBoolean("HoodDown", !isHoodDown);
                    });

                    // Send feedback to player
                    Component msg = Component.literal(isHoodDown ? "Hood pulled up!" : "Hood pulled down!")
                            .withStyle(ChatFormatting.GRAY);
                    player.sendOverlayMessage(msg);
                }
            });
        });
        PayloadTypeRegistry.serverboundPlay().register(ToggleMagnetPayload.TYPE, ToggleMagnetPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ToggleMagnetPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                Player player = context.player();

                // Find first magnet in inventory
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);

                    if (stack.getItem() instanceof MagnetItem) {
                        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                        CompoundTag tag = customData.copyTag();
                        boolean isActive = tag.getBoolean("MagnetActive").orElse(false);

                        CustomData.update(DataComponents.CUSTOM_DATA, stack, t -> {
                            t.putBoolean("MagnetActive", !isActive);
                        });

                        Component msg = Component.literal(isActive ? "✖ Magnet: OFF" : "✔ Magnet: ON")
                                .withStyle(isActive ? ChatFormatting.RED : ChatFormatting.GREEN);
                        player.sendOverlayMessage(msg);

                        // Only toggle the first magnet found
                        break;
                    }
                }
            });
        });

        PayloadTypeRegistry.serverboundPlay().register(OpenBackpackPayload.TYPE, OpenBackpackPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(OpenBackpackPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayer player = context.player();

                // 1. First check trinket back slot
                ItemStack trinketBackpack = findTrinketBackpack(player);
                if (!trinketBackpack.isEmpty()) {
                    openFromTrinket(player, trinketBackpack);
                    return;
                }

                // 2. Then check inventory
                int backpackSlot = -1;
                ItemStack backpackStack = ItemStack.EMPTY;

                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.getItem() instanceof BackpackItem) {
                        backpackSlot = i;
                        backpackStack = stack;
                        break;
                    }
                }

                if (!backpackStack.isEmpty() && backpackSlot != -1) {
                    openFromInventory(player, backpackStack, backpackSlot);
                }
            });
        });
    }

}