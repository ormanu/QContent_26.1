package ormanu.qcontent.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ormanu.qcontent.items.ModItems;

@Mixin(Player.class)
public class PlayerScythePullMixin {

    @Inject(method = "attack", at = @At("TAIL"))
    private void qcontent$scythePull(Entity target, CallbackInfo ci) {
        Player player = (Player)(Object)this;

        if (player.level().isClientSide()) return;
        if (!(target instanceof LivingEntity living)) return;

        ItemStack held = player.getMainHandItem();

        // IMPORTANT: change this to your actual scythe item field name
        if (!held.is(ModItems.Scythe)) return;

        // don't pull if already basically touching
        if (player.distanceToSqr(living) < 1.0 * 1.0) return;

        float charge = player.getAttackStrengthScale(0.5F); // 0..1
        boolean crit = isCrit(player);

        // Mostly horizontal pull strength (tune these)
        double pullStrength = 0.55 + 0.35 * charge + (crit ? 0.20 : 0.0); // ~0.65..1.20
        double lift = crit ? 0.05 : 0.03; // small vertical only

        // Horizontal direction FROM target TO player
        Vec3 dir = player.position().subtract(living.position());
        Vec3 flat = new Vec3(dir.x, 0.0, dir.z);

        double flatLen = flat.length();
        if (flatLen < 1.0E-4) return;

        flat = flat.scale(1.0 / flatLen); // normalize

        Vec3 pull = flat.scale(pullStrength);

        // Dampen existing horizontal motion (this prevents "it just hops")
        Vec3 vel = living.getDeltaMovement();
        double damp = 0.25; // lower = stronger override, higher = smoother

        living.setDeltaMovement(
                vel.x * damp + pull.x,
                vel.y + lift,
                vel.z * damp + pull.z
        );
        living.hurtMarked = true;
    }

    private static boolean isCrit(Player player) {
        return player.fallDistance > 0.0F
                && !player.onGround()
                && !player.onClimbable()
                && !player.isInWater()
                && !player.isPassenger();
    }
}