package ormanu.qcontent.entity.goal;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import ormanu.qcontent.datagen.QItemTagProvider;
import ormanu.qcontent.entity.CrowEntity;

import java.util.EnumSet;
import java.util.List;

public class ScavengeGoal extends Goal {
    private final CrowEntity crow;
    private final double speed;
    private ItemEntity target;

    public ScavengeGoal(CrowEntity crow, double speed) {
        this.crow = crow;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (crow.qcontent$isPerching()) return false;
        if (!crow.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) return false;
        if (crow.getRandom().nextInt(25) != 0) return false; // ~once/sec gate
        if (crow.isInWater()) return false;

        AABB box = crow.getBoundingBox().inflate(8.0);
        List<ItemEntity> items = crow.level().getEntitiesOfClass(ItemEntity.class, box,
                e -> e.isAlive() && !e.getItem().isEmpty() && e.getItem().is(QItemTagProvider.CROW_SCAVENGE));

        if (items.isEmpty()) return false;

        ItemEntity best = null;
        double bestD = Double.MAX_VALUE;
        for (ItemEntity it : items) {
            double d = crow.distanceToSqr(it);
            if (d < bestD) { bestD = d; best = it; }
        }
        target = best;
        return target != null;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null
                && target.isAlive()
                && !target.getItem().isEmpty()
                && crow.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()
                && !crow.qcontent$isPerching()
                && crow.distanceToSqr(target) < 12 * 12;
    }

    @Override
    public void start() {
        crow.getNavigation().moveTo(target, speed);
    }

    @Override
    public void tick() {
        if (target == null) return;

        crow.getNavigation().moveTo(target, speed);

        if (crow.distanceToSqr(target) < 1.2 * 1.2) {
            ItemStack stack = target.getItem();

            ItemStack one = stack.copy();
            one.setCount(1);
            crow.setItemSlot(EquipmentSlot.MAINHAND, one);

            stack.shrink(1);
            if (stack.isEmpty()) target.discard();

            target = null;
        }
    }

    @Override
    public void stop() {
        target = null;
    }
}