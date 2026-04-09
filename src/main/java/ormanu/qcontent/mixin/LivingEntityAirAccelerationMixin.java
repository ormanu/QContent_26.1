package ormanu.qcontent.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ormanu.qcontent.util.AirBoostManager;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAirAccelerationMixin extends net.minecraft.world.entity.Entity {

    public LivingEntityAirAccelerationMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "travel", at = @At("TAIL"))
    private void qcontent$boostHorizontalAcceleration(Vec3 movementInput, CallbackInfo ci) {
        if (!((Object) this instanceof Player player)) return;

        if (!AirBoostManager.hasBoost(player.getUUID())) return;

        if (this.onGround()) {
            AirBoostManager.clear(player.getUUID());
            return;
        }

        AirBoostManager.tick(player.getUUID());

        if (movementInput.length() <= 0.01) return;

        double horizontalBoost = ormanu.qcontent.config.QConfig.airAcceleration;
        double maxHorizontalSpeed = ormanu.qcontent.config.QConfig.maxAirSpeed;

        float yaw = this.getYRot();
        double yawRad = Math.toRadians(yaw);

        double globalX = movementInput.x * Math.cos(yawRad) - movementInput.z * Math.sin(yawRad);
        double globalZ = movementInput.x * Math.sin(yawRad) + movementInput.z * Math.cos(yawRad);

        Vec3 boostedInput = new Vec3(globalX * horizontalBoost, 0.0, globalZ * horizontalBoost);

        Vec3 currentVelocity = this.getDeltaMovement();
        Vec3 newVelocity = currentVelocity.add(boostedInput);

        double horizontalSpeed = Math.sqrt(newVelocity.x * newVelocity.x + newVelocity.z * newVelocity.z);
        if (horizontalSpeed > maxHorizontalSpeed) {
            double scale = maxHorizontalSpeed / horizontalSpeed;
            newVelocity = new Vec3(newVelocity.x * scale, newVelocity.y, newVelocity.z * scale);
        }

        this.setDeltaMovement(newVelocity);
        this.hurtMarked = true;

        //System.out.println("AIR ACCEL ACTIVE");
    }
}