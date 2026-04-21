package ormanu.qcontent.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ormanu.qcontent.items.ModItems;
import ormanu.qcontent.sound.ModSounds;

@Mixin(LivingEntity.class)
public class ParryMixin {
    @Unique private boolean qcontent$didParry = false;
    @Unique private Entity qcontent$parryAttacker = null;

    /**
     * 26.1: LivingEntity#hurtServer(ServerLevel, DamageSource, float) -> boolean
     * We reduce the 'amount' argument at HEAD.
     */
    @ModifyVariable(
            method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At("HEAD"),
            argsOnly = true,
            index = 3
    )
    private float qcontent$parryReduceDamage(float amount, ServerLevel level, DamageSource source) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!(self instanceof Player player)) return amount;

        if (level.isClientSide()) return amount;

        // must be holding right click with refined sword
        if (!player.isUsingItem()) return amount;

        ItemStack using = player.getUseItem();
        if (!using.is(ModItems.RefinedSword)) return amount;

        // find attacker
        Entity attacker = source.getEntity();
        if (attacker == null) attacker = source.getDirectEntity();
        if (attacker == null || attacker == player) return amount;

        // only from the front (horizontal cone check)
        Vec3 look = player.getLookAngle();
        Vec3 toAttacker = attacker.position().subtract(player.position());

        Vec3 lookFlat = new Vec3(look.x, 0.0, look.z);
        Vec3 toFlat   = new Vec3(toAttacker.x, 0.0, toAttacker.z);

        if (lookFlat.lengthSqr() < 1.0E-6 || toFlat.lengthSqr() < 1.0E-6) return amount;

        lookFlat = lookFlat.normalize();
        toFlat = toFlat.normalize();

        double dot = lookFlat.dot(toFlat);
        if (dot <= 0.20) return amount; // stricter: 0.30..0.40

        // ---- PARRY TRIGGERS ----
        qcontent$didParry = true;
        qcontent$parryAttacker = attacker;

        // durability cost
        using.hurtAndBreak(1, player, player.getUsedItemHand());

        // small cooldown (reflection to avoid signature issues)
        qcontent$addCooldown(player, using, 20);

        // sound feedback (SoundEvents are Holders in 26.1)
        level.playSound(null, player.blockPosition(), ModSounds.Parry, SoundSource.PLAYERS, 0.8F, 1.1F);

        // reduce damage (0.35 = 65% reduction)
        return amount * 0.45F;
    }

    @Inject(
            method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At("TAIL")
    )
    private void qcontent$afterHurtServer(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!qcontent$didParry) return;

        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof Player player) {
            // reduce your knockback (horizontal)
            Vec3 v = player.getDeltaMovement();
            player.setDeltaMovement(v.x * 0.25, v.y, v.z * 0.25);
            player.hurtMarked = true;

            // knock back attacker slightly
            if (qcontent$parryAttacker instanceof LivingEntity living) {
                living.knockback(0.9F, player.getX() - living.getX(), player.getZ() - living.getZ());
            }
        }

        qcontent$didParry = false;
        qcontent$parryAttacker = null;
    }

    @Unique
    private static void qcontent$addCooldown(Player player, ItemStack stack, int ticks) {
        Object cooldowns = player.getCooldowns();

        // Try addCooldown(ItemStack,int) first, then addCooldown(Item,int)
        try {
            cooldowns.getClass().getMethod("addCooldown", ItemStack.class, int.class).invoke(cooldowns, stack, ticks);
            return;
        } catch (Throwable ignored) {}

        try {
            cooldowns.getClass().getMethod("addCooldown", stack.getItem().getClass(), int.class).invoke(cooldowns, stack.getItem(), ticks);
        } catch (Throwable ignored) {
            // if both fail, no cooldown (still works)
        }
    }
}