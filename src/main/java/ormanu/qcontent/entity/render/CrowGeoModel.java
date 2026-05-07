package ormanu.qcontent.entity.render;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import ormanu.qcontent.QContent;
import ormanu.qcontent.entity.CrowEntity;

public class CrowGeoModel extends GeoModel<CrowEntity> {
    @Override
    public @NonNull Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(QContent.MOD_ID, "crow");
    }

    @Override
    public @NonNull Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(QContent.MOD_ID, "textures/entity/crow.png");
    }

    @Override
    public @NonNull Identifier getAnimationResource(CrowEntity animatable) {
        return Identifier.fromNamespaceAndPath(QContent.MOD_ID, "crow");
    }
}