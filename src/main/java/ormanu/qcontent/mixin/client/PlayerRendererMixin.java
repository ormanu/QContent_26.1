package ormanu.qcontent.mixin.client;

import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ormanu.qcontent.items.ModItems;
import ormanu.qcontent.util.V2RiptideFlag;

@Mixin(AvatarRenderer.class)
public class PlayerRendererMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void qcontent$setFlag(Avatar avatar, AvatarRenderState state, float partialTick, CallbackInfo ci) {
        boolean v2 = false;

        if (avatar instanceof Player player) {
            ItemStack main = player.getMainHandItem();
            ItemStack off  = player.getOffhandItem();
            ItemStack using = player.getUseItem();

            // Only set true when the spin effect is active, and the player is holding/using V2 trident.
            v2 = state.isAutoSpinAttack && (main.is(ModItems.V2Trident) || off.is(ModItems.V2Trident) || using.is(ModItems.V2Trident));
        }

        ((V2RiptideFlag)(Object) state).qcontent$setV2Riptide(v2);
    }
}