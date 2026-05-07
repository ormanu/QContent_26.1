package ormanu.qcontent.mixin.box.client;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ormanu.qcontent.client.CardboardBoxLayer;

@Mixin(AvatarRenderer.class)
public class AvatarRendererAddBoxLayerMixin {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(method = "<init>", at = @At("TAIL"))
    private void qcontent$addCardboardBoxLayer(EntityRendererProvider.Context context, boolean slimSteve, CallbackInfo ci) {
        EntityModelSet modelSet = context.getModelSet();

        ((LivingEntityRendererLayersAccessor)(Object)this)
                .qcontent$getLayers()
                .add(new CardboardBoxLayer((AvatarRenderer)(Object)this, modelSet));
    }
}