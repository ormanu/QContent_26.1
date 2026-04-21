package ormanu.qcontent.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import ormanu.qcontent.QContent;
import ormanu.qcontent.entity.ModEntityModelLayers;
import ormanu.qcontent.entity.TrainingDummyEntity;

public class TrainingDummyRenderer extends MobRenderer<TrainingDummyEntity, TrainingDummyRenderState, TrainingDummyModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(QContent.MOD_ID, "textures/entity/training_dummy.png");

    public TrainingDummyRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new TrainingDummyModel(ctx.bakeLayer(ModEntityModelLayers.TRAINING_DUMMY)), 0.35f);
    }

    @Override
    public @NonNull TrainingDummyRenderState createRenderState() {
        return new TrainingDummyRenderState();
    }

    @Override
    public @NonNull Identifier getTextureLocation(TrainingDummyRenderState state) {
        return TEXTURE;
    }
}