package ormanu.qcontent.screen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import ormanu.qcontent.items.custom.MagnetItem;

import java.util.HashSet;
import java.util.Set;

public class MagnetFilterHopperMenu extends HopperMenu {

    private final int magnetSlotIndex;
    private final Container filterContainer;
    private final Set<String> initialBlacklist;

    public MagnetFilterHopperMenu(int containerId, Inventory playerInventory, Container filterContainer, int magnetSlotIndex) {
        super(containerId, playerInventory, filterContainer);
        this.magnetSlotIndex = magnetSlotIndex;
        this.filterContainer = filterContainer;

        // Remember what was loaded initially (these are ghost items)
        this.initialBlacklist = new HashSet<>();
        for (int i = 0; i < filterContainer.getContainerSize(); i++) {
            ItemStack stack = filterContainer.getItem(i);
            if (!stack.isEmpty()) {
                Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                initialBlacklist.add(id.toString());
            }
        }
    }

    @Override
    public void clicked(int slotId, int button, ContainerInput clickType, Player player) {
        // Filter slots are 0-4 (the 5 hopper slots)
        if (slotId >= 0 && slotId < 5) {
            Slot slot = this.slots.get(slotId);
            ItemStack carried = this.getCarried();
            ItemStack slotItem = slot.getItem();

            // Block shift-click on filter slots (would put items in player inventory)
            if (clickType == ContainerInput.QUICK_MOVE) {
                // Just remove the ghost item, don't move it
                slot.set(ItemStack.EMPTY);
                return;
            }

            // Block throw/drop actions on filter slots
            if (clickType == ContainerInput.THROW || clickType == ContainerInput.SWAP) {
                return;
            }

            if (!carried.isEmpty()) {
                // Placing an item: set ghost copy (1 item) without consuming
                ItemStack ghost = carried.copy();
                ghost.setCount(1);
                slot.set(ghost);
                // Don't change carried item
            } else {
                // Empty hand click: just remove the ghost
                slot.set(ItemStack.EMPTY);
            }
            return;
        }

        // Normal behavior for player inventory slots
        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Disable shift-click between inventories for filter
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        // Save blacklist from the 5 slots
        Set<String> newBlacklist = new HashSet<>();
        for (int i = 0; i < filterContainer.getContainerSize(); i++) {
            ItemStack stack = filterContainer.getItem(i);
            if (!stack.isEmpty()) {
                Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                newBlacklist.add(id.toString());
            }
        }

        // Save into magnet in hotbar
        ItemStack magnet = player.getInventory().getItem(magnetSlotIndex);
        if (magnet.getItem() instanceof MagnetItem) {
            MagnetItem.saveBlacklist(magnet, newBlacklist);
        }

        // Clear the filter container (everything is a ghost now)
        for (int i = 0; i < filterContainer.getContainerSize(); i++) {
            filterContainer.setItem(i, ItemStack.EMPTY);
        }
    }
}