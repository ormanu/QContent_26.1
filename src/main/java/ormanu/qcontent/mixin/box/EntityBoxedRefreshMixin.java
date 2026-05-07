package ormanu.qcontent.mixin.box;

import net.minecraft.world.entity.Entity;
import ormanu.qcontent.util.BoxedAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Entity.class)
public class EntityBoxedRefreshMixin {
    @Unique private boolean qcontent$boxedInit;
    @Unique private boolean qcontent$boxedLast;

    @Inject(method = "onSyncedDataUpdated", at = @At("TAIL"))
    private void qcontent$refreshDimsWhenBoxedFlagChanges(List<?> changedValues, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        if (!(self instanceof BoxedAccess boxed)) return;

        boolean now = boxed.qcontent$isBoxed();
        if (!qcontent$boxedInit || now != qcontent$boxedLast) {
            qcontent$boxedInit = true;
            qcontent$boxedLast = now;
            self.refreshDimensions();
        }
    }
}