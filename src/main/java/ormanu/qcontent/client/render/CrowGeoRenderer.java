package ormanu.qcontent.client.render;

import com.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import ormanu.qcontent.entity.CrowEntity;

public class CrowGeoRenderer extends GeoEntityRenderer<CrowEntity, CrowRenderState> {
   // private final ItemModelResolver itemModelResolver;

    public CrowGeoRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CrowGeoModel());
        this.shadowRadius = 0.25f;
        this.withScale(1.15f);

    //    this.itemModelResolver = ctx.getItemModelResolver();
    //    this.withRenderLayer(new CrowItemLayer(this));
    }


    //@Override
    //public void extractRenderState(CrowEntity entity, CrowRenderState state, float partialTick) {
    //    super.extractRenderState(entity, state, partialTick);
     //   ItemStack stack = entity.getMainHandItem(); // replace with your own beak item getter
     //   state.beakItem = stack;
     //   state.hasBeakItem = !stack.isEmpty();

     //   if (state.hasBeakItem) {
     //       itemModelResolver.updateForTopItem(
      //              state.beakItemState,
      //              stack,
       //             ItemDisplayContext.HEAD,
       //             entity.level(),
       //             entity,
       //             entity.getId()
       //     );
      //  }
   // }
}