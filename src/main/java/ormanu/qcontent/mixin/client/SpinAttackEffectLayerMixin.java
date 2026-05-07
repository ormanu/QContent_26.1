package ormanu.qcontent.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ormanu.qcontent.QContent;
import ormanu.qcontent.util.V2RiptideFlag;

@Mixin(SpinAttackEffectLayer.class)
public class SpinAttackEffectLayerMixin {
    @Unique
    private static final Identifier V2_TEX =
            Identifier.fromNamespaceAndPath(QContent.MOD_ID, "textures/entity/v2trident_riptide.png");

    @Redirect(
            method = "submit",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/entity/layers/SpinAttackEffectLayer;TEXTURE:Lnet/minecraft/resources/Identifier;"
            )
    )
    private Identifier qcontent$chooseTexture(PoseStack poseStack, SubmitNodeCollector collector, int light,
                                              AvatarRenderState state, float yRot, float xRot) {
        return ((V2RiptideFlag) state).qcontent$isV2Riptide() ? V2_TEX : SpinAttackEffectLayer.TEXTURE;
    }
}
