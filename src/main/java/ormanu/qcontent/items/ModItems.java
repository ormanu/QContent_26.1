package ormanu.qcontent.items;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import ormanu.qcontent.QContent;


import java.util.function.Function;

public class ModItems {

    public static final Item LongSword = registerItem("longsword", LongSwordItem::new,
            new Item.Properties().sword(ToolMaterial.NETHERITE, 5f, -2.8f).rarity(Rarity.RARE).fireResistant().enchantable(30)
    );

    public static final Item V2Trident = registerItem("v2trident", V2TridentItem::new,
            new Item.Properties()
    );

    public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, name));
        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize() {
        QContent.LOGGER.info("ModItems Init");

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.LongSword));
    }

}
