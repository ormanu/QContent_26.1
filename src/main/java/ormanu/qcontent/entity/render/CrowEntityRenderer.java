package ormanu.qcontent.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;
import ormanu.qcontent.QContent;
import ormanu.qcontent.entity.CrowEntity;
import ormanu.qcontent.entity.ModEntityModelLayers;

public class CrowEntityRenderer extends MobRenderer<CrowEntity, CrowEntityRenderState, CrowEntityModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(QContent.MOD_ID, "textures/entity/crow.png");

    public CrowEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new CrowEntityModel(context.bakeLayer(ModEntityModelLayers.CROW)), 0.375f); // 0.375 shadow radius
    }

    @Override
    public @NonNull CrowEntityRenderState createRenderState() {
        return new CrowEntityRenderState();
    }

    @Override
    public @NonNull Identifier getTextureLocation(CrowEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public void extractRenderState(CrowEntity entity, CrowEntityRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        state.onGround = entity.onGround();

        // body yaw and head yaw are separate in LivingEntity
        state.bodyYawDeg = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        state.headYawDeg = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);

        // pitch
        state.headPitchDeg = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
    }
}
