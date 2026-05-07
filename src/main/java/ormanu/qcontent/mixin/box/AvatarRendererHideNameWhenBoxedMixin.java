package ormanu.qcontent.mixin.box;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import ormanu.qcontent.util.BoxedRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererHideNameWhenBoxedMixin {

    @Inject(method = "submitNameDisplay", at = @At("HEAD"), cancellable = true)
    private void qcontent$hideName(AvatarRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {
        if (((BoxedRenderState)(Object) state).qcontent$isBoxed()) {
            ci.cancel();
        }
    }
}
