package ormanu.qcontent;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ormanu.qcontent.blocks.ModBlocks;
import ormanu.qcontent.config.QConfig;
import ormanu.qcontent.entity.CrowEntity;
import ormanu.qcontent.entity.ModEntityTypes;
import ormanu.qcontent.items.ModItems;
import ormanu.qcontent.sound.ModSounds;

public class QContent implements ModInitializer {
	public static final String MOD_ID = "qcontent";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final SimpleParticleType SCULK_SWEEP = FabricParticleTypes.simple();
	public static final SimpleParticleType SCYTHE_SWEEP = FabricParticleTypes.simple();

	@Override
	public void onInitialize() {

		eu.midnightdust.lib.config.MidnightConfig.init("qcontent", QConfig.class);
		ModItems.initialize();
		ModSounds.initialize();
		ModBlocks.initialize();

		ModEntityTypes.registerModEntityTypes();
		ModEntityTypes.registerAttributes();

		LOGGER.info("QContent Main Class Init");

		Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "sculk_sweep"), SCULK_SWEEP);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "scythe_sweep"), SCYTHE_SWEEP);

		SpawnPlacements.register(
				ModEntityTypes.CROW,
				SpawnPlacementTypes.ON_GROUND,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				CrowEntity::checkCrowSpawnRules
		);
		BiomeModifications.addSpawn(
				BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA),
				MobCategory.CREATURE,
				ModEntityTypes.CROW,
				8, 2, 5
		);
	}

}