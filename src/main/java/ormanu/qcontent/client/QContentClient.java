package ormanu.qcontent.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import ormanu.qcontent.QContent;
import ormanu.qcontent.client.particle.SculkSweepParticle;

public class QContentClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleProviderRegistry.getInstance().register(QContent.SCULK_SWEEP, SculkSweepParticle.Provider::new);
    }
}
