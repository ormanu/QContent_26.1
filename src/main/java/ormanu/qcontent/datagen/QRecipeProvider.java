package ormanu.qcontent.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

                shaped(RecipeCategory.COMBAT, ModItems.LongSword, 1)
                        .pattern("  e")
                        .pattern(" c ")
                        .pattern("s  ")
                        .define('e', Items.ECHO_SHARD)
                        .define('c', Items.SCULK_CATALYST)
                        .define('s', Items.STICK)
                        .unlockedBy(getHasName(ModItems.LongSword), has(ModItems.LongSword))
                        .save(output);

                shaped(RecipeCategory.COMBAT, ModItems.V2Trident, 1)
                        .pattern(" e ")
                        .pattern(" a ")
                        .pattern(" s ")
                        .define('e', Items.ECHO_SHARD)
                        .define('a', Items.AMETHYST_SHARD)
                        .define('s', Items.STICK)
                        .unlockedBy(getHasName(ModItems.V2Trident), has(ModItems.V2Trident))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "QRecipeProvider";
    }
}

