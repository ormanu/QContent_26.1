package ormanu.qcontent.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import ormanu.qcontent.items.custom.BackpackItem;

public class BackpackTrinketRenderer implements TrinketRenderer {
    private final ItemStackRenderState itemRenderState = new ItemStackRenderState();
    @Override
    public void submit(ItemStack stack,
                       TrinketSlotAccess slotAccess,
                       EntityModel<? extends LivingEntityRenderState> contextModel,
                       PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector,
                       int light,
                       LivingEntityRenderState renderState,
                       float limbAngle,
                       float limbDistance) {

        if (!(contextModel instanceof HumanoidModel<?> humanoidModel)) {
            return;
        }

        poseStack.pushPose();

        // Follow player body
        humanoidModel.body.translateAndRotate(poseStack);

            // ===== HIP POUCH - Lower belt, under arm, slightly behind =====
            poseStack.translate(0.25F, 0.7F, 0.15F);
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(-20.0F));
            poseStack.scale(0.9F, 0.9F, 0.9F);


        // New item rendering path
        ItemModelResolver resolver = Minecraft.getInstance().getItemModelResolver();

        // Depending on mappings/version, IntelliJ may suggest a slightly different overload here.
        resolver.updateForNonLiving(
                this.itemRenderState,
                stack,
                ItemDisplayContext.FIXED,
                Minecraft.getInstance().player
        );

        // Depending on mappings/version, IntelliJ may suggest submit(...) or render(...)
        this.itemRenderState.submit(
                poseStack,
                submitNodeCollector,
                light,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();
    }
}