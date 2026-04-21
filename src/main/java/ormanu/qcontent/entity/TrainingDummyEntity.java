package ormanu.qcontent.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import ormanu.qcontent.items.ModItems;

public class TrainingDummyEntity extends PathfinderMob {
    private boolean yawLocked = false;
    private float lockedYaw;

    public TrainingDummyEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setNoAi(true);              // no wandering
        this.setPersistenceRequired();    // don’t despawn
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2048.0)            // basically never dies
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public void tick() {
        super.tick();

        // Lock its facing so it doesn't rotate weirdly when pushed.
        // (Armor stands don't auto-turn either.)
        if (!yawLocked) {
            yawLocked = true;
            lockedYaw = this.getYRot();
        }

        this.setYRot(lockedYaw);
        this.setYHeadRot(lockedYaw);
        this.yBodyRot = lockedYaw;
        this.setXRot(0.0F);

        // optional: keep it always "healthy"
        if (!this.level().isClientSide()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    // Don’t get launched by hits (still pushable by collision)
    @Override
    public void knockback(double strength, double x, double z) {
        // no-op
    }

    // Don’t let water/lava currents slide it around (feels more “stand-like”)
    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        // optional: only allow pickup with empty hand
        // if (!player.getItemInHand(hand).isEmpty()) return InteractionResult.PASS;

        if (!this.level().isClientSide()) {
            ItemStack stack = ModItems.TRAINING_DUMMY_SPAWN_EGG.getDefaultInstance();
            if (!player.addItem(stack)) player.drop(stack, false);
            this.discard();
        }

        // replacement for sidedSuccess(level.isClientSide)
        return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        float before = this.getHealth();
        boolean result = super.hurtServer(level, source, amount);
        float dealt = Math.max(0.0F, before - this.getHealth());

        if (source.getEntity() instanceof ServerPlayer sp) {
            sp.connection.send(new ClientboundSetActionBarTextPacket(
                    Component.literal(String.format("Damage: %.2f", dealt))
            ));
        }

        // particles at “chest height”
        double px = this.getX();
        double py = this.getY() + this.getBbHeight() * 0.7;
        double pz = this.getZ();

        level.sendParticles(ParticleTypes.WHITE_ASH, px, py, pz, 2, 0.25, 0.25, 0.25, 0.02);
        level.sendParticles(ParticleTypes.CRIT,            px, py, pz, 4,     0.15, 0.15, 0.15, 0.01);

        // keep it from ever dying
        this.setHealth(this.getMaxHealth());

        return result;
    }
}