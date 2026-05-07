package ormanu.qcontent.entity.render;

import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

import java.util.Map;

public class CrowRenderState extends EntityRenderState implements GeoRenderState {
    @Override
    public Map<DataTicket<?>, Object> getDataMap() {
        return Map.of();
    }
}