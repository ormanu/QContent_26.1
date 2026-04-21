package ormanu.qcontent.entity.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import ormanu.qcontent.entity.CrowEntity;

import java.util.EnumSet;

public class PerchGoal extends Goal {
    private final CrowEntity crow;
    private BlockPos perchPos;
    private int perchTime;

    public PerchGoal(CrowEntity crow) {
        this.crow = crow;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return crow.onGround() && crow.getRandom().nextInt(100) == 0;
    }

    @Override
    public void start() {
        perchTime = 40 + crow.getRandom().nextInt(60);
    }

    @Override
    public boolean canContinueToUse() {
        return perchTime > 0 && crow.onGround();
    }

    @Override
    public void tick() {
        perchTime--;
    }

}
