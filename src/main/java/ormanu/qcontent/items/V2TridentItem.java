package ormanu.qcontent.items;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class V2TridentItem extends TridentItem {
    public V2TridentItem(Properties properties) {
        super(properties);
    }

    private static Holder<Enchantment> riptideHolder(Level level) {
        return level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.RIPTIDE);
    }

    private static int getRiptideLevel(Level level, ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(riptideHolder(level), stack);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (getRiptideLevel(level , stack) <= 0) {
            // Riptide-only: do nothing, prevents throwing
            return InteractionResult.FAIL;
        }

        player.startUsingItem(hand);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (!(livingEntity instanceof Player player)) return false;

        int riptide = getRiptideLevel(level, stack);
        if (riptide <= 0) return false;

        int chargeTime = this.getUseDuration(stack, livingEntity) - timeLeft;
        if (chargeTime < 10) return false;

        // Cost if NOT in water/rain
        if (!player.isInWaterOrRain()) {
            if (!level.isClientSide()) {
                if (player.totalExperience > 0) {
                    player.giveExperienceLevels(-1); // remove 1 XP level
                } else {
                    player.hurt(player.damageSources().magic(), 2.0F); // 1 heart = 2.0F
                }
            }
        }

        // --- Riptide launch ---
        float yaw = player.getYRot();
        float pitch = player.getXRot();

        float x = -Mth.sin(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
        float y = -Mth.sin(pitch * ((float)Math.PI / 180F));
        float z =  Mth.cos(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));

        float len = Mth.sqrt(x * x + y * y + z * z);
        float power = 3.0F * ((1.0F + (float) riptide) / 4.0F);

        x *= power / len;
        y *= power / len;
        z *= power / len;

        player.push(x, y, z);
        player.hurtMarked = true;

        // 26.1 signature: (durationTicks, damage, itemStack)
        player.startAutoSpinAttack(20, 8.0F, stack);

        if (player.onGround()) {
            player.move(MoverType.SELF, new Vec3(0.0, 1.2, 0.0));
        }

        Holder<SoundEvent> soundHolder = switch (riptide) {
            case 1 -> SoundEvents.TRIDENT_RIPTIDE_1;
            case 2 -> SoundEvents.TRIDENT_RIPTIDE_2;
            default -> SoundEvents.TRIDENT_RIPTIDE_3;
        };

        level.playSound(null, player.blockPosition(), soundHolder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

        if (!level.isClientSide()) {
            stack.hurtAndBreak(1, player, player.getUsedItemHand());
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return false;
    }
}