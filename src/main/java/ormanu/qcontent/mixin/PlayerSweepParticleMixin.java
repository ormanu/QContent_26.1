package ormanu.qcontent.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import ormanu.qcontent.QContent;
import ormanu.qcontent.items.ModItems;

@Mixin(Player.class)
public class PlayerSweepParticleMixin {

    @WrapOperation(
            method = "doSweepAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"
            )
    )
    private int qcontent$replaceSweepParticle(ServerLevel level,
                                              ParticleOptions particle,
                                              double x, double y, double z,
                                              int count,
                                              double dx, double dy, double dz,
                                              double speed,
                                              Operation<Integer> original) {

        // Only replace the vanilla sweep particle
        if (particle == ParticleTypes.SWEEP_ATTACK) {
            Player player = (Player)(Object)this;
            ItemStack held = player.getMainHandItem();

            // Only for your longsword
            if (held.is(ModItems.LongSword)) {
                level.sendParticles(QContent.SCULK_SWEEP, x, y, z, count, dx, dy, dz, speed);
                return 0; // don't call vanilla
            }

            if (held.is(ModItems.Scythe)) {
                level.sendParticles(QContent.SCYTHE_SWEEP, x, y, z, count, dx, dy, dz, speed);
                return 0; // don't call vanilla
            }

            if (held.is(ModItems.RefinedSword)) {
                level.sendParticles(QContent.SCYTHE_SWEEP, x, y, z, count, dx, dy, dz, speed);
                return 0; // don't call vanilla
            }
        }

        return original.call(level, particle, x, y, z, count, dx, dy, dz, speed);
    }
}