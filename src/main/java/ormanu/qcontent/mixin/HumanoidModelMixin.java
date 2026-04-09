package ormanu.qcontent.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ormanu.qcontent.items.ModItems;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    private void qcontent$applyLongswordPose(T state, CallbackInfo ci) {
        ItemStack mainHand = state.rightHandItemStack;
        ItemStack offHand = state.leftHandItemStack;

        boolean main = mainHand.is(ModItems.LongSword);
        boolean off = offHand.is(ModItems.LongSword);

        if (!main && !off) return;

        boolean rightHanded = main;

        if (state.isUsingItem) {
            qcontent$applyParryPose(rightHanded);
            return;
        }

        if (state.attackTime > 0.0F) {
            if (rightHanded) {
                qcontent$applySupportArm(false);
            } else {
                qcontent$applySupportArm(true);
            }
            return;
        }

        qcontent$applyIdlePose(rightHanded, state.ageInTicks);
    }

    private void qcontent$applyIdlePose(boolean rightHanded, float ageInTicks) {
        float sway = (float) Math.sin(ageInTicks * 0.08F) * 0.03F;
        float sway2 = (float) Math.sin(ageInTicks * 0.05F) * 0.02F;

        if (rightHanded) {
            // Right Arm (Main) - Braced slightly more central
            this.rightArm.xRot = -0.85F + sway;
            this.rightArm.yRot = -0.15F + sway2;
            this.rightArm.zRot = 0.0F;

            // Left Arm (Support) - Moved further RIGHT to grab the hilt
            this.leftArm.xRot = -1.15F - sway;
            this.leftArm.yRot = 0.65F - sway2; // Increased this to bring it across the chest
            this.leftArm.zRot = 0.0F;
        } else {
            // Mirrored for left-handed players
            this.rightArm.xRot = -1.15F - sway;
            this.rightArm.yRot = -0.65F + sway2;
            this.rightArm.zRot = 0.0F;

            this.leftArm.xRot = -0.85F + sway;
            this.leftArm.yRot = 0.15F - sway2;
            this.leftArm.zRot = 0.0F;
        }
    }

    private void qcontent$applySupportArm(boolean lockRightArm) {
        if (lockRightArm) {
            this.rightArm.xRot = -1.22F;
            this.rightArm.yRot = -0.58F;
            this.rightArm.zRot = 0.18F;
        } else {
            this.leftArm.xRot = -1.22F;
            this.leftArm.yRot = 0.58F;
            this.leftArm.zRot = -0.18F;
        }
    }

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
}