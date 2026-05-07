package ormanu.qcontent.mixin.box;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Avatar;
import ormanu.qcontent.util.BoxedAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Avatar.class)
public abstract class AvatarBoxedDataMixin implements BoxedAccess {

    @Unique
    private static final EntityDataAccessor<Boolean> QCONTENT_BOXED =
            SynchedEntityData.defineId(Avatar.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void qcontent$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(QCONTENT_BOXED, false);
    }

    @Override
    public boolean qcontent$isBoxed() {
        Avatar self = (Avatar)(Object)this;
        return self.getEntityData().get(QCONTENT_BOXED);
    }

    @Override
    public void qcontent$setBoxed(boolean boxed) {
        Avatar self = (Avatar)(Object)this;

        boolean old = self.getEntityData().get(QCONTENT_BOXED);
        if (old == boxed) return;

        self.getEntityData().set(QCONTENT_BOXED, boxed);

        // apply hitbox change immediately (server + local client)
        self.refreshDimensions();
    }
}