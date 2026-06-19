package ormanu.qcontent.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import ormanu.qcontent.blocks.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class QBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public QBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(BlockTags.STAIRS).add(ModBlocks.CALCITE_STAIRS);
        valueLookupBuilder(BlockTags.SLABS).add(ModBlocks.CALCITE_SLABS);

        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.CALCITE_SLABS)
                .add(ModBlocks.CALCITE_STAIRS);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.DIRT_SLAB)
                .add(ModBlocks.COARSE_DIRT_SLAB);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocks.DIRT_SLAB)
                .add(ModBlocks.COARSE_DIRT_SLAB);
    }
}
