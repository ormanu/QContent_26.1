package ormanu.qcontent.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class) // Target LivingEntity instead of Player
public class NoSelfFireworkDamageMixin {

    // Try this newer signature first
    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void qcontent$noSelfFireworkDamage(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        // Check if the entity taking damage is a player
        if (!((Object) this instanceof Player player)) return;

        Entity direct = source.getDirectEntity();
        if (!(direct instanceof FireworkRocketEntity rocket)) return;

        Entity owner = rocket.getOwner();
        if (owner == player) {
            // Cancel the damage from your own firework rocket
            cir.setReturnValue(false);
        }
    }
}