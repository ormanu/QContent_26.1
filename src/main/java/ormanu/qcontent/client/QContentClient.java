package ormanu.qcontent.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import ormanu.qcontent.QContent;
import ormanu.qcontent.client.particle.SculkSweepParticle;
import ormanu.qcontent.client.particle.ScytheSweepParticle;
import ormanu.qcontent.entity.render.CrowEntityRenderer;
import ormanu.qcontent.entity.ModEntityModelLayers;
import ormanu.qcontent.entity.ModEntityTypes;
import ormanu.qcontent.entity.render.TrainingDummyModel;
import ormanu.qcontent.entity.render.TrainingDummyRenderer;

public class QContentClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleProviderRegistry.getInstance().register(QContent.SCULK_SWEEP, SculkSweepParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(QContent.SCYTHE_SWEEP, ScytheSweepParticle.Provider::new);

        ModEntityModelLayers.registerModelLayers();
        EntityRenderers.register(ModEntityTypes.CROW, CrowEntityRenderer::new);
        EntityRenderers.register(ModEntityTypes.TRAINING_DUMMY, TrainingDummyRenderer::new);
    }
}
