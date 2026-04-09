package ormanu.qcontent.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import ormanu.qcontent.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class QItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public QItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(ItemTags.SWORDS)
                .add(ModItems.LongSword);
        valueLookupBuilder(ItemTags.TRIDENT_ENCHANTABLE)
                .add(ModItems.V2Trident);
    }
}
