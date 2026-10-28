package ormanu.qcontent.datagen;

import eu.pb4.trinkets.api.DefaultTrinketSlotTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import ormanu.qcontent.QContent;
import ormanu.qcontent.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class QItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public static final TagKey<Item> REFINED_REPAIR = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, "refined_repair"));
    public static final TagKey<Item> EXTENDED_REACH = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, "extended_reach"));
    public static final TagKey<Item> CROW_SCAVENGE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, "crow_scavenge"));
    public static final TagKey<Item> POUCH_ENCHANTABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(QContent.MOD_ID, "pouch_enchantable"));

    public QItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(ItemTags.SWORDS)
                .add(key(ModItems.LongSword))
                .add(key(ModItems.Scythe))
                .add(key(ModItems.RefinedSword));

        builder(ItemTags.HOES)
                .add(key(ModItems.Scythe));

        builder(ItemTags.TRIDENT_ENCHANTABLE)
                .add(key(ModItems.V2Trident));

        builder(REFINED_REPAIR)
                .add(key(ModItems.Refined_Ingot));

        builder(EXTENDED_REACH)
                .add(key(ModItems.Scythe))
                .add(key(ModItems.RefinedSword));

        builder(CROW_SCAVENGE)
                .add(key(Items.WHEAT_SEEDS))
                .add(key(Items.PUMPKIN_SEEDS))
                .add(key(Items.MELON_SEEDS))
                .add(key(Items.GOLD_NUGGET))
                .add(key(Items.IRON_NUGGET))
                .add(key(Items.AMETHYST_SHARD));

        builder(ItemTags.ARMOR_ENCHANTABLE)
                .add(key(ModItems.REFINED_HELMET))
                .add(key(ModItems.REFINED_CHESTPLATE))
                .add(key(ModItems.REFINED_LEGGINGS))
                .add(key(ModItems.REFINED_BOOTS));

        builder(ItemTags.HEAD_ARMOR)
                .add(key(ModItems.REFINED_HELMET));

        builder(ItemTags.CHEST_ARMOR)
                .add(key(ModItems.REFINED_CHESTPLATE));

        builder(ItemTags.LEG_ARMOR)
                .add(key(ModItems.REFINED_LEGGINGS));

        builder(ItemTags.FOOT_ARMOR)
                .add(key(ModItems.REFINED_BOOTS));

        builder(DefaultTrinketSlotTags.CHEST_BACK)
                .add(key(ModItems.POUCH));

        builder(POUCH_ENCHANTABLE)
                .add(key(ModItems.POUCH));
    }

    private static ResourceKey<Item> key(Item item) {
        return item.builtInRegistryHolder().key();
    }
}