package ormanu.qcontent.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ormanu.qcontent.entity.TrainingDummyEntity;
import ormanu.qcontent.items.ModItems;

@Mixin(Player.class)
public class PlayerScythePullMixin {

    @Unique private boolean qcontent$doPull;
    @Unique private float qcontent$attackStrength;   // captured BEFORE vanilla resets it
    @Unique private boolean qcontent$critPossible;   // captured BEFORE vanilla changes pose/flags

    @Inject(method = "attack", at = @At("HEAD"))
    private void qcontent$captureAttackState(Entity target, CallbackInfo ci) {
        Player player = (Player)(Object)this;

        qcontent$doPull = false;
        qcontent$attackStrength = 0.0F;
        qcontent$critPossible = false;

        if (player.level().isClientSide()) return;
        if (!(target instanceof LivingEntity)) return;
        if (target instanceof TrainingDummyEntity) return;

        ItemStack held = player.getMainHandItem();
        if (!held.is(ModItems.Scythe)) return;

        // capture pre-attack values (vanilla resets attack strength during attack)
        qcontent$attackStrength = player.getAttackStrengthScale(0.5F);

        qcontent$critPossible =
                player.fallDistance > 0.0F
                        && !player.onGround()
                        && !player.onClimbable()
                        && !player.isInWater()
                        && !player.isMobilityRestricted()
                        && !player.isPassenger()
                        && !player.isSprinting();

        qcontent$doPull = true;
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void qcontent$scythePull(Entity target, CallbackInfo ci) {
        if (!qcontent$doPull) return;

        Player player = (Player)(Object)this;
        if (!(target instanceof LivingEntity living)) return;

        // crit-only: vanilla crit requires full-strength too
        if (!(qcontent$attackStrength > 0.9F && qcontent$critPossible)) return;

        // don't pull if already basically touching
        if (player.distanceToSqr(living) < 1.0 * 1.0) return;

        // Pull strength scales with the captured charge
        double pullStrength = 0.60 + 0.35 * qcontent$attackStrength; // ~1.25 max
        double lift = 0.05;

        Vec3 dir = player.position().subtract(living.position());
        Vec3 flat = new Vec3(dir.x, 0.0, dir.z);
        double flatLen = flat.length();
        if (flatLen < 1.0E-4) return;

        flat = flat.scale(1.0 / flatLen);
        Vec3 pull = flat.scale(pullStrength);

        Vec3 vel = living.getDeltaMovement();
        double damp = 0.55;

        living.setDeltaMovement(
                vel.x * damp + pull.x,
                vel.y + lift,
                vel.z * damp + pull.z
        );
        living.hurtMarked = true;
    }
}