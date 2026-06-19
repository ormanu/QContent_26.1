package ormanu.qcontent.client.render;

import com.geckolib.cache.model.BakedGeoModel;
import com.geckolib.cache.model.GeoBone;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.base.PerBoneRender;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.GeoRenderLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import ormanu.qcontent.entity.CrowEntity;

import java.util.function.BiConsumer;

public class CrowItemLayer extends GeoRenderLayer<CrowEntity, Void, CrowRenderState> {

    public CrowItemLayer(GeoRenderer<CrowEntity, Void, CrowRenderState> renderer) {
        super(renderer);
    }

    @Override
    public void addPerBoneRender(RenderPassInfo<CrowRenderState> renderPassInfo,
                                 BiConsumer<GeoBone, PerBoneRender<CrowRenderState>> consumer) {
        CrowRenderState state = renderPassInfo.renderState();
        if (!state.hasBeakItem || state.beakItem.isEmpty()) return;

        BakedGeoModel bakedModel = getDefaultBakedModel(state);
        GeoBone beakBone = bakedModel.getBone("cioc").orElse(null);
        if (beakBone == null) return;

        consumer.accept(beakBone, (RenderPassInfo<CrowRenderState> pass, GeoBone bone, SubmitNodeCollector tasks) -> {
            CrowRenderState rs = pass.renderState();

            // IMPORTANT: PoseStack comes from RenderPassInfo in your GeckoLib build
            PoseStack poseStack = pass.poseStack(); // if this method name is red, see note below

            poseStack.pushPose();
            poseStack.translate(0.0, -0.05, -0.05);
            poseStack.scale(0.5f, 0.5f, 0.5f);

            // submit(poseStack, tasks, light, overlay, outlineColor)
            rs.beakItemState.submit(poseStack, tasks, 15728880, 0, 0);

            poseStack.popPose();
        });
    }
}