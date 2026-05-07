package ormanu.qcontent.mixin.client;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import ormanu.qcontent.util.V2RiptideFlag;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements V2RiptideFlag {
    @Unique
    private boolean qcontent$v2;

    @Override public boolean qcontent$isV2Riptide() { return qcontent$v2; }
    @Override public void qcontent$setV2Riptide(boolean v) { qcontent$v2 = v; }
}
