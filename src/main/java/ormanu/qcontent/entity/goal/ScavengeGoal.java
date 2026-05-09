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
    private int pathUpdateCountdown; // Added to prevent pathfinding lag/stuttering

    public ScavengeGoal(CrowEntity crow, double speed) {
        this.crow = crow;
        this.speed = speed;
        // Added Flag.LOOK so the crow actually looks at the item it's walking towards
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (crow.qcontent$isPerching()) return false;
        if (!crow.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) return false;
        if (crow.getRandom().nextInt(25) != 0) return false; // ~once/sec gate
        if (crow.isInWater()) return false;

        // Inflated the Y-axis slightly less than X/Z to prevent detecting items through thick ceilings/floors
        AABB box = crow.getBoundingBox().inflate(8.0, 4.0, 8.0);
        List<ItemEntity> items = crow.level().getEntitiesOfClass(ItemEntity.class, box,
                e -> e.isAlive()
                        && !e.hasPickUpDelay() // Ensure the item can actually be picked up
                        && !e.getItem().isEmpty()
                        && e.getItem().is(QItemTagProvider.CROW_SCAVENGE));

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
        this.pathUpdateCountdown = 0; // Reset the countdown when the goal starts
    }

    @Override
    public void tick() {
        if (target == null) return;

        // Make the crow look at the item
        crow.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Only update the path every 10 ticks (half a second).
        // This stops the crow from stuttering and saves performance!
        if (--this.pathUpdateCountdown <= 0) {
            this.pathUpdateCountdown = 10;
            crow.getNavigation().moveTo(target, speed);
        }

        // Use Bounding Box intersection instead of distanceToSqr.
        // This is much more reliable for item pickups!
        if (crow.getBoundingBox().inflate(0.5).intersects(target.getBoundingBox())) {
            ItemStack stack = target.getItem();

            ItemStack one = stack.copy();
            one.setCount(1);
            crow.setItemSlot(EquipmentSlot.MAINHAND, one);

            // IMPORTANT: Make sure the crow drops the item if it gets killed!
            crow.setDropChance(EquipmentSlot.MAINHAND, 1.0F);

            stack.shrink(1);
            if (stack.isEmpty()) {
                target.discard();
            }

            target = null;
        }
    }

    @Override
    public void stop() {
        target = null;
        crow.getNavigation().stop(); // Stop walking when the goal finishes or is interrupted
    }
}