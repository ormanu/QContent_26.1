package ormanu.qcontent;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import ormanu.qcontent.datagen.QItemTagProvider;
import ormanu.qcontent.datagen.QRecipeProvider;

public class QContentDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(QItemTagProvider::new);
		pack.addProvider(QRecipeProvider::new);
	}
}
