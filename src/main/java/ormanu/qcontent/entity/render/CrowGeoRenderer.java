package ormanu.qcontent.entity.render;

import com.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import ormanu.qcontent.entity.CrowEntity;

public class CrowGeoRenderer extends GeoEntityRenderer<CrowEntity, CrowRenderState> {
    public CrowGeoRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CrowGeoModel());
        this.shadowRadius = 0.25f;
        this.withScale(1.15f);
    }
}