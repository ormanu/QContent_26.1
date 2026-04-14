package ormanu.qcontent.items;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import ormanu.qcontent.QContent;
import ormanu.qcontent.datagen.QItemTagProvider;


import java.util.function.Function;

public class ModItems {

    public static final ToolMaterial REFINED_TOOLMATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2031,
            9.0F,
            4.0F,
            15,
            QItemTagProvider.REFINED_REPAIR
    );

    public static final Item LongSword = registerItem("longsword", LongSwordItem::new,
            new Item.Properties().sword(ToolMaterial.NETHERITE, 5f, -2.8f).rarity(Rarity.RARE).fireResistant().enchantable(30)
    );

    public static final Item RefinedSword = registerItem("refined_sword", RefinedSwordItem::new,
            new Item.Properties().sword(ModItems.REFINED_TOOLMATERIAL, 5f, -2.8f).rarity(Rarity.COMMON).fireResistant().enchantable(30)
    );

    public static final Item Scythe = registerItem("scythe", ScytheItem::new,
            new Item.Properties().sword(ModItems.REFINED_TOOLMATERIAL, 5f, -3f).rarity(Rarity.RARE).fireResistant().enchantable(30)
    );

    public static final Item V2Trident = registerItem("v2trident", V2TridentItem::new,
            new Item.Properties().rarity(Rarity.COMMON).enchantable(30)
    );

    public static final Item Refined_Ingot = registerItem("refined_ingot", Item::new,
            new Item.Properties().stacksTo(64)
    );

    public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, name));
        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static final ResourceKey<CreativeModeTab> QCONTENT_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(QContent.MOD_ID, "creative_tab")
    );
    public static final CreativeModeTab QCONTENT_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.Refined_Ingot))
            .title(Component.translatable("creativeTab.qcontent"))
            .displayItems((params, output) -> {
                output.accept(ModItems.LongSword);
                output.accept(ModItems.V2Trident);
                output.accept(ModItems.Scythe);
                output.accept(ModItems.Refined_Ingot);
                output.accept(ModItems.RefinedSword);
            })
            .build();

    public static void initialize() {
        QContent.LOGGER.info("ModItems Init");
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, QCONTENT_TAB_KEY, QCONTENT_CREATIVE_TAB);
    }

}
