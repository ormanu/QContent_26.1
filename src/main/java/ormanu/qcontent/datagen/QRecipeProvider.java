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

                //blocks
                shaped(RecipeCategory.DECORATIONS, ModBlocks.TeddyBear, 1)
                        .pattern(" b ")
                        .pattern(" sw")
                        .define('b', Items.BROWN_DYE)
                        .define('s', Items.STRING)
                        .define('w', ItemTags.WOOL)
                        .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "QRecipeProvider";
    }
}

