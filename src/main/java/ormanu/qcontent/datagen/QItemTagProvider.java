package ormanu.qcontent.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import ormanu.qcontent.QContent;
import ormanu.qcontent.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class QItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public QItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Item> REFINED_REPAIR = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, "refined_repair"));

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(ItemTags.SWORDS)
                .add(ModItems.LongSword)
                .add(ModItems.Scythe)
                .add(ModItems.RefinedSword);
        valueLookupBuilder(ItemTags.TRIDENT_ENCHANTABLE)
                .add(ModItems.V2Trident);
        valueLookupBuilder(REFINED_REPAIR)
                .add(ModItems.Refined_Ingot);
    }
}
