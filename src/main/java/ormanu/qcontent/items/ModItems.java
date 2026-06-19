package ormanu.qcontent.items;

import eu.pb4.trinkets.api.component.TrinketDataComponents;
import eu.pb4.trinkets.api.component.TrinketEquippable;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Blocks;
import ormanu.qcontent.QContent;
import ormanu.qcontent.blocks.ModBlocks;
import ormanu.qcontent.datagen.QItemTagProvider;
import ormanu.qcontent.entity.ModEntityTypes;
import ormanu.qcontent.items.armor.RefinedArmorMaterial;
import ormanu.qcontent.items.custom.*;


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
            new Item.Properties().sword(ModItems.REFINED_TOOLMATERIAL, 5f, -2.8f).rarity(Rarity.UNCOMMON).fireResistant().enchantable(30)
    );

    public static final Item Scythe = registerItem("scythe", ScytheItem::new,
            new Item.Properties().sword(ModItems.REFINED_TOOLMATERIAL, 5f, -3f).rarity(Rarity.RARE).fireResistant().enchantable(30)
    );

    public static final Item V2Trident = registerItem("v2trident", V2TridentItem::new,
            new Item.Properties().rarity(Rarity.COMMON).enchantable(30).stacksTo(1)
    );

    public static final Item Refined_Ingot = registerItem("refined_ingot", Item::new,
            new Item.Properties().stacksTo(64)
    );

    public static final Item CROW_SPAWN_EGG = registerItem("crow_spawn_egg", SpawnEggItem::new,
            new Item.Properties().spawnEgg(ModEntityTypes.CROW)
    );

    public static final Item TRAINING_DUMMY_SPAWN_EGG = registerItem("training_dummy", SpawnEggItem::new,
            new Item.Properties().spawnEgg(ModEntityTypes.TRAINING_DUMMY)
    );

    public static final Item MAGNET = registerItem("magnet", MagnetItem::new,
            new Item.Properties().stacksTo(1).rarity(Rarity.RARE)
    );

    public static final Item POUCH = registerItem("pouch",
            properties -> new BackpackItem(
                    properties.component(
                            TrinketDataComponents.EQUIPMENT,
                            TrinketEquippable.DEFAULT
                                    .withSlots("chest/back")
                                    .withEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
                                    .withSwappable(true)
                                    .withEquipOnInteract(false)
                    )
            ),
            new Item.Properties().stacksTo(1).enchantable(15)
    );

    public static final Item REFINED_HELMET = registerItem(
            "refined_helmet",
            RefinedHelmetItem::new,
            new Item.Properties().humanoidArmor(RefinedArmorMaterial.INSTANCE, ArmorType.HELMET)
                    .durability(ArmorType.HELMET.getDurability(RefinedArmorMaterial.BASE_DURABILITY))
    );
    public static final Item REFINED_CHESTPLATE = registerItem(
            "refined_chestplate",
            Item::new,
            new Item.Properties().humanoidArmor(RefinedArmorMaterial.INSTANCE, ArmorType.CHESTPLATE)
                    .durability(ArmorType.CHESTPLATE.getDurability(RefinedArmorMaterial.BASE_DURABILITY))
    );

    public static final Item REFINED_LEGGINGS = registerItem(
            "refined_leggings",
            Item::new,
            new Item.Properties().humanoidArmor(RefinedArmorMaterial.INSTANCE, ArmorType.LEGGINGS)
                    .durability(ArmorType.LEGGINGS.getDurability(RefinedArmorMaterial.BASE_DURABILITY))
    );

    public static final Item REFINED_BOOTS = registerItem(
            "refined_boots",
            Item::new,
            new Item.Properties().humanoidArmor(RefinedArmorMaterial.INSTANCE, ArmorType.BOOTS)
                    .durability(ArmorType.BOOTS.getDurability(RefinedArmorMaterial.BASE_DURABILITY))
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
                output.accept(ModBlocks.TeddyBear);
                output.accept(ModItems.CROW_SPAWN_EGG);
                output.accept(ModItems.TRAINING_DUMMY_SPAWN_EGG);
                output.accept(ModItems.MAGNET);
                output.accept(ModItems.REFINED_HELMET);
                output.accept(ModItems.REFINED_CHESTPLATE);
                output.accept(ModItems.REFINED_LEGGINGS);
                output.accept(ModItems.REFINED_BOOTS);
                output.accept(ModItems.POUCH);
                output.accept(Blocks.CALCITE);
                output.accept(ModBlocks.CALCITE_STAIRS);
                output.accept(ModBlocks.CALCITE_SLABS);
                output.accept(ModBlocks.DIRT_SLAB);
                output.accept(ModBlocks.COARSE_DIRT_SLAB);
            })
            .noScrollBar()
            .build();

    public static void initialize() {
        QContent.LOGGER.info("ModItems Init");
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, QCONTENT_TAB_KEY, QCONTENT_CREATIVE_TAB);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((creativeTab) -> creativeTab.accept(ModItems.MAGNET));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((creativeTab) -> creativeTab.accept(ModItems.TRAINING_DUMMY_SPAWN_EGG));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS)
                .register((creativeTab) -> creativeTab.accept(ModItems.CROW_SPAWN_EGG));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> creativeTab.accept(ModItems.Refined_Ingot));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.RefinedSword));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.Scythe));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.V2Trident));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.LongSword));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.REFINED_HELMET));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.REFINED_CHESTPLATE));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.REFINED_LEGGINGS));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.REFINED_BOOTS));

    }

}
