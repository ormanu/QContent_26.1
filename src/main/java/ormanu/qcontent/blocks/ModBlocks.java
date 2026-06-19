package ormanu.qcontent.blocks;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import ormanu.qcontent.QContent;
import ormanu.qcontent.items.ModItems;

import java.util.function.Function;

public class ModBlocks {

    public static final Block TeddyBear = register(
            "teddybear",
            TeddyBearBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.COBWEB).noOcclusion(),
            true
    );

    public static final Block CALCITE_STAIRS = register(
            "calcite_stairs",
            properties -> new StairBlock(Blocks.CALCITE.defaultBlockState(),
                    properties.strength(0.75f).requiresCorrectToolForDrops()),
            BlockBehaviour.Properties.of(),
            true
    );

    public static final Block CALCITE_SLABS = register(
            "calcite_slab",
            properties -> new SlabBlock(
                    properties.strength(0.75f).requiresCorrectToolForDrops()),
            BlockBehaviour.Properties.of(),
            true
    );

    public static final Block COARSE_DIRT_SLAB = register(
            "coarse_dirt_slab",
            properties -> new SlabBlock(
                    properties.strength(0.75f).requiresCorrectToolForDrops()),
            BlockBehaviour.Properties.of(),
            true
    );

    public static final Block DIRT_SLAB = register(
            "dirt_slab",
            properties -> new SlabBlock(
                    properties.strength(0.75f).requiresCorrectToolForDrops()),
            BlockBehaviour.Properties.of(),
            true
    );

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        ResourceKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(settings.setId(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(QContent.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, name));
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((creativeTab) -> creativeTab.accept(ModBlocks.CALCITE_STAIRS));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((creativeTab) -> creativeTab.accept(ModBlocks.CALCITE_SLABS));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((creativeTab) -> creativeTab.accept(ModBlocks.DIRT_SLAB));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((creativeTab) -> creativeTab.accept(ModBlocks.COARSE_DIRT_SLAB));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((creativeTab) -> creativeTab.accept(ModBlocks.TeddyBear));
    }
}
