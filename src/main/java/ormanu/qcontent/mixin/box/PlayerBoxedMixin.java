package ormanu.qcontent.mixin.box;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import ormanu.qcontent.util.BoxedAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerBoxedMixin {
    private static final double BOX_MOVE_MULT = 0.60;

    // Slow movement without touching attributes (no FOV change)
    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    private Vec3 qcontent$slowWhileBoxed(Vec3 input) {
        Player self = (Player)(Object)this;
        if (!((BoxedAccess)(Object)self).qcontent$isBoxed()) return input;
        if (self.isPassenger() || self.getAbilities().flying) return input;
        return input.scale(BOX_MOVE_MULT);
    }

    // Disallow sprint
    @Inject(method = "canSprint", at = @At("HEAD"), cancellable = true)
    private void qcontent$noSprint(CallbackInfoReturnable<Boolean> cir) {
        Player self = (Player)(Object)this;
        if (((BoxedAccess)(Object)self).qcontent$isBoxed()) {
            cir.setReturnValue(false);
        }
    }

    // Force sprint off if it somehow gets enabled
    @Inject(method = "tick", at = @At("TAIL"))
    private void qcontent$forceStopSprint(CallbackInfo ci) {
        Player self = (Player)(Object)this;
        if (((BoxedAccess)(Object)self).qcontent$isBoxed() && self.isSprinting()) {
            self.setSprinting(false);
        }
    }
}