package ormanu.qcontent.entity;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import ormanu.qcontent.QContent;
import ormanu.qcontent.entity.render.CardboardBoxModel;
import ormanu.qcontent.entity.render.TrainingDummyModel;

public class ModEntityModelLayers {
    public static final ModelLayerLocation TRAINING_DUMMY = createMain("training_dummy");
    public static final ModelLayerLocation CARDBOARD_BOX = createMain("cardboard_box");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(QContent.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.TRAINING_DUMMY, TrainingDummyModel::getTexturedModelData);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.CARDBOARD_BOX, CardboardBoxModel::createLayer);
    }
}
