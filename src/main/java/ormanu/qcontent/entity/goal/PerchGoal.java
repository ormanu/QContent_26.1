package ormanu.qcontent.entity.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import ormanu.qcontent.entity.CrowEntity;

import java.util.EnumSet;

public class PerchGoal extends Goal {
    private final CrowEntity crow;
    private final double speed;

    private BlockPos perchPos;
    private int perchTime;

    public PerchGoal(CrowEntity crow, double speed) {
        this.crow = crow;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (crow.qcontent$isPerching()) return false;
        if (crow.isInWater()) return false;
        if (!crow.getNavigation().isDone()) return false;

        // more likely at night (optional)
        int chance = crow.level().isBrightOutside() ? 140 : 60; // tune
        if (crow.getRandom().nextInt(chance) != 0) return false;

        perchPos = findPerch();
        return perchPos != null;
    }

    @Override
    public void start() {
        crow.qcontent$setPerching(false);
        perchTime = 0;
        crow.getNavigation().moveTo(perchPos.getX() + 0.5, perchPos.getY() + 1.0, perchPos.getZ() + 0.5, speed);
    }

    @Override
    public boolean canContinueToUse() {
        if (crow.isInWater()) return false;
        if (perchPos == null) return false;

        // keep going while traveling or while resting
        return crow.qcontent$isPerching() || !crow.getNavigation().isDone();
    }

    @Override
    public void tick() {
        if (perchPos == null) return;

        // once close enough, begin perching
        Vec3 target = Vec3.atBottomCenterOf(perchPos.above());
        if (!crow.qcontent$isPerching()) {
            if (crow.position().distanceToSqr(target) < 0.25) {
                crow.getNavigation().stop();
                crow.qcontent$setPerching(true);
                perchTime = 60 + crow.getRandom().nextInt(120); // 3–9 seconds
            }
        } else {
            // resting
            perchTime--;
            if (perchTime <= 0) {
                crow.qcontent$setPerching(false);
                perchPos = null;
            }
        }
    }

    @Override
    public void stop() {
        crow.qcontent$setPerching(false);
        perchPos = null;
        perchTime = 0;
    }

    private BlockPos findPerch() {
        BlockPos base = crow.blockPosition();
        BlockPos.MutableBlockPos below = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos above = new BlockPos.MutableBlockPos();

        // scan a small area around
        for (BlockPos pos : BlockPos.betweenClosed(
                base.offset(-6, -2, -6),
                base.offset( 6,  6,  6))) {

            if (pos.equals(base)) continue;

            BlockState standOn = crow.level().getBlockState(pos);

            boolean perchable =
                    standOn.getBlock() instanceof LeavesBlock ||
                            standOn.is(BlockTags.LOGS) ||
                            standOn.is(BlockTags.FENCES) ||
                            standOn.is(BlockTags.WALLS);

            if (!perchable) continue;

            // need air above
            if (!crow.level().isEmptyBlock(above.setWithOffset(pos, Direction.UP))) continue;

            // avoid perching under blocks
            if (!crow.level().isEmptyBlock(above.setWithOffset(pos, Direction.UP))) continue;

            return pos.immutable();
        }
        return null;
    }
}