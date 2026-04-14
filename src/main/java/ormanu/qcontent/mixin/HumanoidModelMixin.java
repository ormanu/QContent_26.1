package ormanu.qcontent.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ormanu.qcontent.items.ModItems;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> {
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    private void qcontent$applyTwoHandPose(T state, CallbackInfo ci) {
        ItemStack mainHand = state.rightHandItemStack;
        ItemStack offHand  = state.leftHandItemStack;

        boolean main = qcontent$isTwoHandWeapon(mainHand);
        boolean off  = qcontent$isTwoHandWeapon(offHand);
        if (!main && !off) return;

        boolean rightHanded = main;

        if (state.isUsingItem) {
            if (mainHand.is(ModItems.RefinedSword) || offHand.is(ModItems.RefinedSword)) {
                qcontent$applyRefinedFrontParryPose(rightHanded);
            } else {
                qcontent$applyParryPose(rightHanded); // your existing longsword sideways parry
            }
            return;
        }

        if (state.attackTime > 0.0F) {
            // The main arm will already be swinging (vanilla).
            // We make the support arm "follow" so it doesn't float or snap.
            if (rightHanded) {
                qcontent$followSwingSupportArm(false, state.attackTime); // lock/follow LEFT arm
            } else {
                qcontent$followSwingSupportArm(true, state.attackTime);  // lock/follow RIGHT arm
            }
            return;
        }

        qcontent$applyIdlePose(rightHanded, state.ageInTicks);
    }

    @Unique
    private boolean qcontent$isTwoHandWeapon(ItemStack stack) {
        return stack.is(ModItems.LongSword)
                || stack.is(ModItems.RefinedSword);
    }

    @Unique
    private void qcontent$applyIdlePose(boolean rightHanded, float ageInTicks) {
        float sway  = (float) Math.sin(ageInTicks * 0.08F) * 0.03F;
        float sway2 = (float) Math.sin(ageInTicks * 0.05F) * 0.02F;

        if (rightHanded) {
            this.rightArm.xRot = -0.85F + sway;
            this.rightArm.yRot = -0.15F + sway2;
            this.rightArm.zRot = 0.0F;

            this.leftArm.xRot = -1.15F - sway;
            this.leftArm.yRot = 0.65F - sway2;
            this.leftArm.zRot = 0.0F;
        } else {
            this.rightArm.xRot = -1.15F - sway;
            this.rightArm.yRot = -0.65F + sway2;
            this.rightArm.zRot = 0.0F;

            this.leftArm.xRot = -0.85F + sway;
            this.leftArm.yRot = 0.15F - sway2;
            this.leftArm.zRot = 0.0F;
        }
    }

    /**
     * Makes the support arm follow the swinging arm during attack so it stays on the weapon.
     * lockRightArm = true -> right arm is support arm
     * lockRightArm = false -> left arm is support arm
     */
    @Unique
    private void qcontent$followSwingSupportArm(boolean lockRightArm, float attackTime) {
        // 0..1-ish. Higher = more glued to the swing.
        float follow = 0.75F;

        // Tiny offsets that define the "grip" relationship between arms.
        float xOffset = -0.25F;
        float yOffset = lockRightArm ? -0.70F : 0.70F;
        float zOffset = lockRightArm ? 0.05F : -0.05F;

        if (lockRightArm) {
            // Right arm is support -> follow left arm swing
            this.rightArm.xRot = Mth.lerp(follow, this.rightArm.xRot, this.leftArm.xRot + xOffset);
            this.rightArm.yRot = Mth.lerp(follow, this.rightArm.yRot, this.leftArm.yRot + yOffset);
            this.rightArm.zRot = Mth.lerp(follow, this.rightArm.zRot, this.leftArm.zRot + zOffset);
        } else {
            // Left arm is support -> follow right arm swing
            this.leftArm.xRot = Mth.lerp(follow, this.leftArm.xRot, this.rightArm.xRot + xOffset);
            this.leftArm.yRot = Mth.lerp(follow, this.leftArm.yRot, this.rightArm.yRot + yOffset);
            this.leftArm.zRot = Mth.lerp(follow, this.leftArm.zRot, this.rightArm.zRot + zOffset);
        }
    }

    @Unique
    private void qcontent$applyParryPose(boolean rightHanded) {
        if (rightHanded) {
            this.rightArm.xRot = -0.85F;
            this.rightArm.yRot = -0.9F;
            this.rightArm.zRot = 0.3F;

            this.leftArm.xRot = -0.8F;
            this.leftArm.yRot = 0.35F;
            this.leftArm.zRot = -0.2F;
        } else {
            this.rightArm.xRot = -0.8F;
            this.rightArm.yRot = -0.35F;
            this.rightArm.zRot = 0.2F;

            this.leftArm.xRot = -0.85F;
            this.leftArm.yRot = 0.9F;
            this.leftArm.zRot = -0.3F;
        }
    }
    @Unique
    private void qcontent$applyRefinedFrontParryPose(boolean rightHanded) {
        // "Front guard": both hands forward, close together, centered.
        // Mostly symmetric so it looks fixed in front of the player.
        if (rightHanded) {
            this.rightArm.xRot = -1.35F;
            this.rightArm.yRot = -0.10F;
            this.rightArm.zRot = 0.05F;

            this.leftArm.xRot = -1.35F;
            this.leftArm.yRot = 0.20F;
            this.leftArm.zRot = -0.05F;
        } else {
            // mirrored
            this.rightArm.xRot = -1.35F;
            this.rightArm.yRot = -0.20F;
            this.rightArm.zRot = 0.05F;

            this.leftArm.xRot = -1.35F;
            this.leftArm.yRot = 0.10F;
            this.leftArm.zRot = -0.05F;
        }
    }
}