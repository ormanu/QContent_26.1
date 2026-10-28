package ormanu.qcontent.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import ormanu.qcontent.blocks.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class QBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public QBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(BlockTags.STAIRS)
                .add(key(ModBlocks.CALCITE_STAIRS));

        builder(BlockTags.SLABS)
                .add(key(ModBlocks.CALCITE_SLABS));

        builder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(key(ModBlocks.CALCITE_SLABS))
                .add(key(ModBlocks.CALCITE_STAIRS));

        builder(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(key(ModBlocks.DIRT_SLAB))
                .add(key(ModBlocks.COARSE_DIRT_SLAB));

        builder(BlockTags.MINEABLE_WITH_HOE)
                .add(key(ModBlocks.DIRT_SLAB))
                .add(key(ModBlocks.COARSE_DIRT_SLAB));
    }

    private static ResourceKey<Block> key(Block block) {
        return block.builtInRegistryHolder().key();
    }
}