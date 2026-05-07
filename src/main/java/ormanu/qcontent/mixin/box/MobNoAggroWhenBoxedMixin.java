package ormanu.qcontent.mixin.box;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import ormanu.qcontent.util.BoxedAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobNoAggroWhenBoxedMixin {

    // Prevent selecting a boxed player as a valid attack target
    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void qcontent$noAttackBoxed(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        Mob self = (Mob)(Object)this;

        if (!(self instanceof Enemy)) return; // only hostiles; remove this line if you want ALL mobs

        if (target instanceof Player p && ((BoxedAccess)(Object)p).qcontent$isBoxed()) {
            cir.setReturnValue(false);
        }
    }

    // If already targeting a boxed player, drop the target immediately
    @Inject(method = "aiStep", at = @At("TAIL"))
    private void qcontent$dropTargetIfBoxed(CallbackInfo ci) {
        Mob self = (Mob)(Object)this;

        if (!(self instanceof Enemy)) return;

        LivingEntity t = self.getTarget();
        if (t instanceof Player p && ((BoxedAccess)(Object)p).qcontent$isBoxed()) {
            self.setTarget(null);
        }
    }
}