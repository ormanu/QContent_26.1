package ormanu.qcontent.mixin.box.client;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import ormanu.qcontent.util.BoxedRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateBoxedMixin implements BoxedRenderState {
    @Unique private boolean qcontent$boxed;

    @Override public boolean qcontent$isBoxed() { return qcontent$boxed; }
    @Override public void qcontent$setBoxed(boolean v) { qcontent$boxed = v; }
}
