package ormanu.qcontent.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ormanu.qcontent.util.AirBoostManager;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(method = "shootProjectile", at = @At("TAIL"))
    private static void qcontent$boostPlayerAfterFireworkShot(
            LivingEntity shooter,
            Projectile projectile,
            int index,
            float speed,
            float divergence,
            float simulated,
            LivingEntity target,
            CallbackInfo ci
    ) {
        if (!(shooter instanceof Player player)) return;

        if (projectile.getType() == EntityType.FIREWORK_ROCKET) {
            Vec3 look = player.getLookAngle().normalize();

            double recoilPower = ormanu.qcontent.config.QConfig.fireworkRecoilPower;
            double upwardKick = ormanu.qcontent.config.QConfig.fireworkUpwardKick;

            Vec3 recoilBoost = new Vec3(
                    -look.x * recoilPower,
                    Math.max(0.3, -look.y * recoilPower + upwardKick),
                    -look.z * recoilPower
            );

            player.setDeltaMovement(player.getDeltaMovement().add(recoilBoost));
            player.hurtMarked = true;

            AirBoostManager.setBoostUntilGround(player.getUUID());
            //System.out.println("SET AIR BOOST FOR " + player.getName().getString());
        }
    }
}