package ormanu.qcontent.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import ormanu.qcontent.enchantment.ModEnchantments;

import java.util.concurrent.CompletableFuture;

public class QEnchantmentGenerator extends FabricDynamicRegistryProvider {
    public QEnchantmentGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT)); // Add all bootstrapped enchantments for the current mod id
    }

    @Override
    public String getName() {
        return "Enchantments";
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        // ...
        // :::bootstrap
        // :::register-enchantment
        register(context, ModEnchantments.DEEP_POCKETS,
                Enchantment.enchantment(
                        Enchantment.definition(
                                context.lookup(Registries.ITEM).getOrThrow(QItemTagProvider.POUCH_ENCHANTABLE), // The items this enchantment can be applied to
                                10, // The weight / probability of our enchantment being available in the enchanting table
                                3, // The max level of the enchantment
                                Enchantment.dynamicCost(5, 8), // The base minimum cost of the enchantment, and the additional cost for every level
                                Enchantment.dynamicCost(20, 10), // Same as the other dynamic cost, but for the maximum instead
                                5, // The cost to apply the enchantment in an anvil, in levels
                                EquipmentSlotGroup.ANY // The slot types in which this enchantment will be able to apply its effects
                        )
                )
        );
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.identifier()));
    }
}
