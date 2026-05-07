package ormanu.qcontent.mixin.box.client;

import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import ormanu.qcontent.util.BoxedAccess;
import ormanu.qcontent.util.BoxedRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererExtractBoxedMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void qcontent$extract(Avatar avatar, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        boolean boxed = avatar instanceof Player p && ((BoxedAccess)(Object)p).qcontent$isBoxed();
        ((BoxedRenderState)(Object)state).qcontent$setBoxed(boxed);
    }
}
