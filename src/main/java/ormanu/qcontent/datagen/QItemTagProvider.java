package ormanu.qcontent.datagen;

import eu.pb4.trinkets.api.DefaultTrinketSlotTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import ormanu.qcontent.QContent;
import ormanu.qcontent.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class QItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public QItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Item> REFINED_REPAIR = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, "refined_repair"));
    public static final TagKey<Item> EXTENDED_REACH = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, "extended_reach"));
    public static final TagKey<Item> CROW_SCAVENGE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, "crow_scavenge"));
    public static final TagKey<Item> POUCH_ENCHANTABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, "pouch_enchantable"));


    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(ItemTags.SWORDS)
                .add(ModItems.LongSword)
                .add(ModItems.Scythe)
                .add(ModItems.RefinedSword);
        valueLookupBuilder(ItemTags.HOES)
                .add(ModItems.Scythe);
        valueLookupBuilder(ItemTags.TRIDENT_ENCHANTABLE)
                .add(ModItems.V2Trident);
        valueLookupBuilder(REFINED_REPAIR)
                .add(ModItems.Refined_Ingot);
        valueLookupBuilder(EXTENDED_REACH)
                .add(ModItems.Scythe)
                .add(ModItems.RefinedSword);
        valueLookupBuilder(CROW_SCAVENGE)
                .add(Items.WHEAT_SEEDS)
                .add(Items.PUMPKIN_SEEDS)
                .add(Items.MELON_SEEDS)
                .add(Items.GOLD_NUGGET)
                .add(Items.IRON_NUGGET)
                .add(Items.AMETHYST_SHARD);
        valueLookupBuilder(ItemTags.ARMOR_ENCHANTABLE)
                .add(ModItems.REFINED_HELMET)
                .add(ModItems.REFINED_CHESTPLATE)
                .add(ModItems.REFINED_LEGGINGS)
                .add(ModItems.REFINED_BOOTS);
        valueLookupBuilder(ItemTags.HEAD_ARMOR)
                .add(ModItems.REFINED_HELMET);
        valueLookupBuilder(ItemTags.CHEST_ARMOR)
                .add(ModItems.REFINED_CHESTPLATE);
        valueLookupBuilder(ItemTags.LEG_ARMOR)
                .add(ModItems.REFINED_LEGGINGS);
        valueLookupBuilder(ItemTags.FOOT_ARMOR)
                .add(ModItems.REFINED_BOOTS);
        valueLookupBuilder(DefaultTrinketSlotTags.CHEST_BACK)
                .add(ModItems.POUCH);
        valueLookupBuilder(QItemTagProvider.POUCH_ENCHANTABLE)
                .add(ModItems.POUCH);

    }
}
