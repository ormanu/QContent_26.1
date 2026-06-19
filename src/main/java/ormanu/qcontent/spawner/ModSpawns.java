package ormanu.qcontent.spawner;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import ormanu.qcontent.entity.CrowEntity;
import ormanu.qcontent.entity.ModEntityTypes;

public class ModSpawns {
    public static void initialize() {
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(
                        Biomes.PLAINS, Biomes.FOREST, Biomes.MEADOW, Biomes.BIRCH_FOREST, Biomes.FLOWER_FOREST
                ),
                MobCategory.CREATURE,
                ModEntityTypes.CROW,
                12, 2, 3
        );

        SpawnPlacements.register(
                ModEntityTypes.CROW,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                CrowEntity::checkCrowSpawnRules
        );
    }
}
