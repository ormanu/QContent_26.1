package ormanu.qcontent;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import ormanu.qcontent.datagen.*;

public class QContentDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(QItemTagProvider::new);
		pack.addProvider(QRecipeProvider::new);
		pack.addProvider(QBlockTagProvider::new);
		pack.addProvider(QBlockLootTableProvider::new);
		pack.addProvider(QEnchantmentGenerator::new);
		pack.addProvider(QEnchantmentTagProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.ENCHANTMENT, QEnchantmentGenerator::bootstrap);
	}
}
