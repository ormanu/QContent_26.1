package ormanu.qcontent.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import ormanu.qcontent.blocks.ModBlocks;
import ormanu.qcontent.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class QRecipeProvider extends FabricRecipeProvider {
    public QRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);

                //items
                shaped(RecipeCategory.COMBAT, ModItems.LongSword, 1)
                        .pattern(" e ")
                        .pattern(" c ")
                        .pattern(" s ")
                        .define('e', Items.ECHO_SHARD)
                        .define('c', Items.SCULK_CATALYST)
                        .define('s', Items.STICK)
                        .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.V2Trident, 1)
                        .pattern(" e ")
                        .pattern(" a ")
                        .pattern(" s ")
                        .define('e', Items.ECHO_SHARD)
                        .define('a', Items.AMETHYST_SHARD)
                        .define('s', Items.STICK)
                        .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
                        .save(output);
                shaped(RecipeCategory.MISC, ModItems.Refined_Ingot, 1)
                        .pattern(" g ")
                        .pattern(" dn")
                        .define('g', Items.GOLD_INGOT)
                        .define('d', Items.DIAMOND)
                        .define('n', Items.NETHERITE_SCRAP)
                        .unlockedBy(getHasName(Items.NETHERITE_SCRAP), has(Items.NETHERITE_SCRAP))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.Scythe, 1)
                        .pattern(" rr")
                        .pattern(" r ")
                        .pattern(" s ")
                        .define('r', ModItems.Refined_Ingot)
                        .define('s', Items.STICK)
                        .unlockedBy(getHasName(ModItems.Refined_Ingot), has(ModItems.Refined_Ingot))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.RefinedSword, 1)
                        .pattern(" r ")
                        .pattern(" r ")
                        .pattern(" s ")
                        .define('r', ModItems.Refined_Ingot)
                        .define('s', Items.STICK)
                        .unlockedBy(getHasName(ModItems.Refined_Ingot), has(ModItems.Refined_Ingot))
                        .save(output);
                shaped(RecipeCategory.DECORATIONS, ModItems.TRAINING_DUMMY_SPAWN_EGG, 1)
                        .pattern(" h ")
                        .pattern(" s ")
                        .pattern("lll")
                        .define('h', Items.HAY_BLOCK)
                        .define('s', Items.STICK)
                        .define('l', ItemTags.WOODEN_SLABS)
                        .unlockedBy(getHasName(Items.HAY_BLOCK), has(Items.HAY_BLOCK))
                        .save(output);
                shaped(RecipeCategory.MISC, ModItems.POUCH, 1)
                        .pattern("ldl")
                        .pattern("lbl")
                        .define('d', Items.DIAMOND)
                        .define('b', Items.BUNDLE)
                        .define('l', Items.LEATHER)
                        .unlockedBy(getHasName(Items.BUNDLE), has(Items.BUNDLE))
                        .save(output);
                //blocks
                shaped(RecipeCategory.DECORATIONS, ModBlocks.TeddyBear, 1)
                        .pattern(" b ")
                        .pattern(" sw")
                        .define('b', Items.BROWN_DYE)
                        .define('s', Items.STRING)
                        .define('w', ItemTags.WOOL)
                        .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                        .save(output);
                shaped(RecipeCategory.TOOLS, ModItems.MAGNET, 1)
                        .pattern("r r")
                        .pattern("i i")
                        .pattern("iii")
                        .define('i', Items.IRON_INGOT)
                        .define('r', Items.REDSTONE)
                        .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                        .save(output);
                shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CALCITE_STAIRS, 4)
                        .pattern("c  ")
                        .pattern("cc ")
                        .pattern("ccc")
                        .define('c', Items.CALCITE)
                        .unlockedBy(getHasName(Items.CALCITE), has(Items.CALCITE))
                        .save(output);
                shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CALCITE_SLABS, 6)
                        .pattern("ccc")
                        .define('c', Items.CALCITE)
                        .unlockedBy(getHasName(Items.CALCITE), has(Items.CALCITE))
                        .save(output);
                shaped(RecipeCategory.BUILDING_BLOCKS, Items.CALCITE, 4)
                        .pattern("dw")
                        .pattern("wd")
                        .define('d', Items.DIORITE)
                        .define('w', Items.WHITE_DYE)
                        .unlockedBy(getHasName(Items.DIORITE), has(Items.DIORITE ))
                        .save(output);
                shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIRT_SLAB, 6)
                        .pattern("ddd")
                        .define('d', Items.DIRT)
                        .unlockedBy(getHasName(Items.DIRT), has(Items.DIRT))
                        .save(output);
                shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.COARSE_DIRT_SLAB, 6)
                        .pattern("ddd")
                        .define('d', Items.COARSE_DIRT)
                        .unlockedBy(getHasName(Items.COARSE_DIRT), has(Items.COARSE_DIRT))
                        .save(output);

                //armor
                shaped(RecipeCategory.COMBAT, ModItems.REFINED_HELMET, 1)
                        .pattern("rrr")
                        .pattern("r r")
                        .define('r', ModItems.Refined_Ingot)
                        .unlockedBy(getHasName(ModItems.Refined_Ingot), has(ModItems.Refined_Ingot))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.REFINED_CHESTPLATE, 1)
                        .pattern("r r")
                        .pattern("rrr")
                        .pattern("rrr")
                        .define('r', ModItems.Refined_Ingot)
                        .unlockedBy(getHasName(ModItems.Refined_Ingot), has(ModItems.Refined_Ingot))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.REFINED_LEGGINGS, 1)
                        .pattern("rrr")
                        .pattern("r r")
                        .pattern("r r")
                        .define('r', ModItems.Refined_Ingot)
                        .unlockedBy(getHasName(ModItems.Refined_Ingot), has(ModItems.Refined_Ingot))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.REFINED_BOOTS, 1)
                        .pattern("r r")
                        .pattern("r r")
                        .define('r', ModItems.Refined_Ingot)
                        .unlockedBy(getHasName(ModItems.Refined_Ingot), has(ModItems.Refined_Ingot))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "QRecipeProvider";
    }
}

