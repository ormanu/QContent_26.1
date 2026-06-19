package ormanu.qcontent.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import ormanu.qcontent.entity.goal.PerchGoal;
import ormanu.qcontent.entity.goal.ScavengeGoal;
import ormanu.qcontent.sound.ModSounds;

public class CrowEntity extends TamableAnimal implements GeoEntity, FlyingAnimal {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation FLY  = RawAnimation.begin().thenLoop("fly");
    // Perching flag for goals + animation
    private static final EntityDataAccessor<Boolean> QCONTENT_PERCHING =
            SynchedEntityData.defineId(CrowEntity.class, EntityDataSerializers.BOOLEAN);

    private static void qcontent$actionbar(Player player, Component msg) {
        if (player instanceof ServerPlayer sp) {
            sp.connection.send(new ClientboundSetActionBarTextPacket(msg));
        }
    }

    private int qcontent$panicCawCooldown = 0;

    public void qcontent$panicCaw() {
        if (this.level().isClientSide() || this.isSilent()) return;
        if (qcontent$panicCawCooldown > 0) return;

        qcontent$panicCawCooldown = 40; // 2 seconds

        float pitch = 1.1F + this.random.nextFloat() * 0.2F;

        this.level().playSound(
                null,
                this.blockPosition(),
                ModSounds.Caw,
                net.minecraft.sounds.SoundSource.NEUTRAL,
                1.0F,
                pitch
        );
    }

    public CrowEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FLYING_SPEED, 1)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.TEMPT_RANGE, 10.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(QCONTENT_PERCHING, false);
    }

    public boolean qcontent$isPerching() {
        return this.entityData.get(QCONTENT_PERCHING);
    }

    public void qcontent$setPerching(boolean v) {
        this.entityData.set(QCONTENT_PERCHING, v);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        // Sit command (owner)
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));

        // Wild behavior: panic + avoid players (disabled once tamed)
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(3, new CrowAvoidPlayerGoal(this, 7.0F, 1.2, 1.6));

        // Tempt with seeds (works even when tamed; remove if you want it owner-only)
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, Ingredient.of(Items.WHEAT_SEEDS), false));

        // Follow owner (only runs when tamed)
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0, 5.0F, 1.0F));

        // Natural crow stuff
        this.goalSelector.addGoal(6, new PerchGoal(this, 1.0));
        this.goalSelector.addGoal(7, new ScavengeGoal(this, 1.2));

        // Flock feel (simple)
        this.goalSelector.addGoal(8, new FollowMobGoal(this, 1.0, 3.0F, 7.0F));

        // Fallback roaming
        this.goalSelector.addGoal(9, new WaterAvoidingRandomFlyingGoal(this, 1.0));

        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        return nav;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // no fall damage
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public void tick() {
        super.tick();

        this.setCanPickUpLoot(true);

        if (!this.level().isClientSide() && qcontent$panicCawCooldown > 0) {
            qcontent$panicCawCooldown--;
        }
        // Stop "drop like a rock" behavior: damp downward speed in air (like Parrot.calculateFlapping)
        if (!this.onGround() && !this.isPassenger()) {
            Vec3 dm = this.getDeltaMovement();
            if (dm.y < 0.0) {
                this.setDeltaMovement(dm.x, dm.y * 0.6, dm.z);
            }
        }

        if (!this.level().isClientSide()) {
            // If perching flag is set while airborne, clear it (prevents hover/idle in air)
            if (qcontent$isPerching() && !this.onGround()) {
                qcontent$setPerching(false);
            }

            if (qcontent$isPerching()) {
                this.getNavigation().stop();
                this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
            }

            // drop carried item occasionally (keep your existing code)
            ItemStack held = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!held.isEmpty() && this.random.nextInt(200) == 0) {
                dropOne(held);
                held.shrink(1);
                if (held.isEmpty()) this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
        }

        if (!this.level().isClientSide() && !this.isSilent()) {
            // caw more when perched, less when flying
            int chance = this.qcontent$isPerching() ? 140 : (this.onGround() ? 260 : 400);

            if (this.random.nextInt(chance) == 0) {
                float pitch = 0.9F + this.random.nextFloat() * 0.2F;
                this.level().playSound(
                        null,
                        this.blockPosition(),
                        ModSounds.Caw,
                        SoundSource.NEUTRAL,
                        0.8F,
                        pitch
                );
            }
        }
    }

    private void dropOne(ItemStack stack) {
        if (stack.isEmpty()) return;
        ItemStack one = stack.copyWithCount(1);
        ItemEntity it = new ItemEntity(this.level(), this.getX(), this.getY() + 0.2, this.getZ(), one);
        this.level().addFreshEntity(it);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }

    // ---- Taming + owner interaction ----
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Tame with seeds
        if (!this.isTame() && stack.is(Items.WHEAT_SEEDS)) {
            if (!player.getAbilities().instabuild) stack.shrink(1);

            if (!this.level().isClientSide()) {
                if (this.random.nextInt(5) == 0) { // 20% chance
                    this.tame(player);
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte)7); // hearts
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6); // smoke
                }
            }
            return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        // owner - SHIFT-RIGHT-CLICK
        if (this.isTame() && this.isOwnedBy(player) && player.isShiftKeyDown()) {
            if (!this.level().isClientSide()) {
                boolean newSit = !this.isOrderedToSit();
                this.setOrderedToSit(newSit);
                this.getNavigation().stop();
                this.qcontent$setPerching(false);

                qcontent$actionbar(player, Component.literal(newSit ? "Crow: Sitting" : "Crow: Following"));
            }
            return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        return super.mobInteract(player, hand);
    }

    // ---- spawn rules ----
    public static boolean checkCrowSpawnRules(
            EntityType<CrowEntity> type, LevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random
    ) {
        if (!level.getBlockState(pos).isAir()) return false;

        BlockState below = level.getBlockState(pos.below());
        boolean okGround = below.is(BlockTags.DIRT) || below.is(BlockTags.SAND) || below.is(BlockTags.LEAVES) || below.is(BlockTags.GRASS_BLOCKS);

        return okGround && Mob.checkMobSpawnRules(type, level, reason, pos, random);
    }

    // ---- GeckoLib ----
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<CrowEntity>("main", 2, (AnimationTest<CrowEntity> st) -> {
            boolean moving = this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4;

            // Airborne always = fly anim
            if (!this.onGround()) {
                st.setAnimation(FLY);
            } else if (this.qcontent$isPerching()) {
                st.setAnimation(IDLE); // or PERCH if you add one
            } else if (moving) {
                st.setAnimation(WALK);
            } else {
                st.setAnimation(IDLE);
            }
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    /**
     * Avoid players only while wild (not tamed).
     */
    private static class CrowAvoidPlayerGoal extends AvoidEntityGoal<Player> {
        private final CrowEntity crow;

        public CrowAvoidPlayerGoal(CrowEntity crow, float distance, double walkSpeed, double sprintSpeed) {
            super(crow, Player.class, distance, walkSpeed, sprintSpeed);
            this.crow = crow;
        }

        @Override
        public boolean canUse() {
            return !crow.isTame() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !crow.isTame() && super.canContinueToUse();
        }
    }

    @Override
    public boolean hurtServer(net.minecraft.server.level.ServerLevel level, net.minecraft.world.damagesource.DamageSource source, float amount) {
        boolean ok = super.hurtServer(level, source, amount);
        if (ok) {
            this.qcontent$panicCaw();
            this.qcontent$setPerching(false); // optional: break perch on damage
        }
        return ok;
    }
}