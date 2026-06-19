package ormanu.qcontent.screen;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import ormanu.qcontent.items.custom.BackpackItem;

public class BackpackMenu extends ChestMenu {

    private final Container backpackContainer;
    private final int backpackSlotIndex;

    public BackpackMenu(int containerId,
                        Inventory playerInventory,
                        Container container,
                        int backpackSlotIndex,
                        int rows) {
        super(getMenuType(rows), containerId, playerInventory, container, rows);
        this.backpackContainer = container;
        this.backpackSlotIndex = backpackSlotIndex;
    }

    private static MenuType<ChestMenu> getMenuType(int rows) {
        return switch (rows) {
            case 1 -> MenuType.GENERIC_9x1;
            case 2 -> MenuType.GENERIC_9x2;
            case 3 -> MenuType.GENERIC_9x3;
            case 4 -> MenuType.GENERIC_9x4;
            case 5 -> MenuType.GENERIC_9x5;
            case 6 -> MenuType.GENERIC_9x6;
            default -> MenuType.GENERIC_9x3;
        };
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        ItemStack backpack = player.getInventory().getItem(backpackSlotIndex);
        if (backpack.getItem() instanceof BackpackItem) {
            BackpackItem.saveItemsPreserveMax(backpack, (SimpleContainer) backpackContainer);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        ItemStack backpack = player.getInventory().getItem(backpackSlotIndex);
        return backpack.getItem() instanceof BackpackItem;
    }
}