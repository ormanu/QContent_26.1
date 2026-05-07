package ormanu.qcontent.mixin.box;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import ormanu.qcontent.util.BoxedAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityBoxedDimensionsMixin {

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    private void qcontent$boxedDims(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        LivingEntity self = (LivingEntity)(Object)this;

        if (self instanceof BoxedAccess boxed && boxed.qcontent$isBoxed()) {
            // <= 1.0 height fits in 1-block spaces
            cir.setReturnValue(EntityDimensions.scalable(0.9F, 0.6F));
        }
    }
}