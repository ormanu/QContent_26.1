package ormanu.qcontent.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import ormanu.qcontent.QContent;
import ormanu.qcontent.entity.ModEntityModelLayers;
import ormanu.qcontent.entity.render.CardboardBoxModel;
import ormanu.qcontent.util.BoxedRenderState;

public class CardboardBoxLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier TEX =
            Identifier.fromNamespaceAndPath(QContent.MOD_ID, "textures/entity/cardboard_box.png");

    private final CardboardBoxModel boxModel;

    public CardboardBoxLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent, EntityModelSet modelSet) {
        super(parent);
        this.boxModel = new CardboardBoxModel(modelSet.bakeLayer(ModEntityModelLayers.CARDBOARD_BOX));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                       AvatarRenderState state, float yRot, float xRot) {
        if (!((BoxedRenderState)(Object) state).qcontent$isBoxed()) return;

        // 1) Base lift so the box doesn't clip into ground
        // 2px is a good starting point; tune 1..3 pixels
        float baseLift = 0.0f / 16.0f;

        // AvatarRenderer lowers crouching players by (scale * 2px). Undo that so box stays on ground.
        float crouchComp = state.isCrouching ? (state.scale * (-2.0f / 16.0f)) : 0.0f;

        // Create-like hop: ALWAYS positive (uses abs(sin)), so it never dips below the base
        float speed = state.walkAnimationSpeed;
        float t = state.walkAnimationPos;

        float amp = Math.min(1.0f, speed) * (1.5f / 16.0f); // hop height: 1.5px at full speed
        float hop = (speed > 0.01f) ? Math.abs(Mth.sin(t * 0.7f)) * amp : 0.0f;

        poseStack.pushPose();
        poseStack.translate(0.0, baseLift + crouchComp + hop, 0.0);

        collector.submitModel(this.boxModel, state, poseStack, TEX, light,
                OverlayTexture.NO_OVERLAY, state.outlineColor, null);

        poseStack.popPose();

        if (speed > 0.01f) {
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-3.0f));
        }
    }
}