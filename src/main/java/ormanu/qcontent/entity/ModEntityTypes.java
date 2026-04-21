package ormanu.qcontent.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import ormanu.qcontent.QContent;

public class ModEntityTypes {

    public static final EntityType<CrowEntity> CROW = register(
            "crow",
            EntityType.Builder.<CrowEntity>of(CrowEntity::new, MobCategory.MISC)
                    .sized(0.75f, 0.75f)
    );

    public static final EntityType<TrainingDummyEntity> TRAINING_DUMMY = register(
            "training_dummy",
            EntityType.Builder.of(TrainingDummyEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.9f)
                    .clientTrackingRange(10)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(QContent.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerModEntityTypes() {
        QContent.LOGGER.info("Registering EntityTypes for " + QContent.MOD_ID);
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(CROW, CrowEntity.createCubeAttributes());
        FabricDefaultAttributeRegistry.register(TRAINING_DUMMY, TrainingDummyEntity.createAttributes());
    }

}
