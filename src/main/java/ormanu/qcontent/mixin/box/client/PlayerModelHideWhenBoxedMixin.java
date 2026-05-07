package ormanu.qcontent.mixin.box.client;

import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import ormanu.qcontent.util.BoxedRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerModelHideWhenBoxedMixin {

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void qcontent$hide(AvatarRenderState state, CallbackInfo ci) {
        PlayerModel m = (PlayerModel)(Object)this;
        boolean boxed = ((BoxedRenderState)(Object)state).qcontent$isBoxed();

        if (boxed) {
            m.body.visible = false;
            m.head.visible = false;
            m.rightArm.visible = false;
            m.leftArm.visible = false;
            m.rightLeg.visible = false;
            m.leftLeg.visible = false;

            m.hat.visible = false;
            m.jacket.visible = false;
            m.leftPants.visible = false;
            m.rightPants.visible = false;
            m.leftSleeve.visible = false;
            m.rightSleeve.visible = false;
        } else {
            // restore (prevents “stuck hidden head”)
            m.head.visible = true;
            m.hat.visible = state.showHat;
        }
    }
}
