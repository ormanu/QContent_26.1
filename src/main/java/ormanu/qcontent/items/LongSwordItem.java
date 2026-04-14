package ormanu.qcontent.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LongSwordItem extends Item {

    public LongSwordItem(Properties properties) {
        super(properties);
    }

    private void spawnCleaveParticles(Level level, Player player) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;

        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.position().add(0.0, player.getBbHeight() * 0.6, 0.0);

        double radius = 3.5;
        int points = 16;
        double arc = Math.toRadians(90);
        double baseAngle = Math.atan2(look.z, look.x);

        for (int i = 0; i < points; i++) {
            double progress = (double) i / (points - 1);
            double angle = baseAngle - arc / 2.0 + arc * progress;

            double x = origin.x + Math.cos(angle) * radius;
            double y = origin.y;
            double z = origin.z + Math.sin(angle) * radius;

            serverLevel.sendParticles(
                    ParticleTypes.SCULK_SOUL,
                    x, y, z,
                    1,
                    0.0, 0.0, 0.0,
                    0.0
            );
        }
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        //System.out.println("LONGSWORD RIGHT CLICKED");
        player.startUsingItem(hand);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (!(livingEntity instanceof Player player)) return false;
        if (level.isClientSide()) return false;

        int chargeTime = this.getUseDuration(stack, livingEntity) - timeLeft;

        // need at least 20 ticks = 1 second
        if (chargeTime < 20) return false;

        performCleave(level, player, chargeTime);

        player.getCooldowns().addCooldown(stack, 100); // 4 sec cooldown i think

        stack.hurtAndBreak(1, player, player.getUsedItemHand());

        spawnCleaveParticles(level, player);

        //System.out.println("LONGSWORD RELEASED");

        level.playSound(
                null,
                player.blockPosition(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.PLAYERS,
                1.0F,
                0.8F
        );
        return false;
    }
    private void performCleave(Level level, Player player, int chargeTime) {
        double range = 4.0;
        double radius = 3.0;
        float baseDamage = 7.5F;
        float bonusDamage = Math.min(chargeTime / 10.0F, 5.0F);

        Vec3 look = player.getLookAngle().normalize();
        Vec3 center = player.position().add(0.0, player.getBbHeight() * 0.5, 0.0).add(look.scale(2.0));

        AABB area = new AABB(
                center.x - radius, center.y - 1.5, center.z - radius,
                center.x + radius, center.y + 1.5, center.z + radius
        );

        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                entity -> entity != player && entity.isAlive());

        for (LivingEntity target : targets) {
            Vec3 toTarget = target.position().subtract(player.position()).normalize();
            double dot = look.dot(toTarget);

            // only hit targets in front of player
            if (dot > 0.4 && player.distanceTo(target) <= range) {
                target.hurt(player.damageSources().playerAttack(player), baseDamage + bonusDamage);
                target.knockback(0.8F, player.getX() - target.getX(), player.getZ() - target.getZ());
            }
        }
    }

}
