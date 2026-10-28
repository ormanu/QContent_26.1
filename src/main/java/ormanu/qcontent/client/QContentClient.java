package ormanu.qcontent.client;

import com.mojang.blaze3d.platform.InputConstants;
import eu.pb4.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;
import ormanu.qcontent.QContent;
import ormanu.qcontent.client.particle.SculkSweepParticle;
import ormanu.qcontent.client.particle.ScytheSweepParticle;
import ormanu.qcontent.client.render.BackpackTrinketRenderer;
import ormanu.qcontent.client.render.CrowGeoRenderer;
import ormanu.qcontent.client.render.SlimArmorRenderer;
import ormanu.qcontent.entity.ModEntityModelLayers;
import ormanu.qcontent.entity.ModEntityTypes;
import ormanu.qcontent.client.render.TrainingDummyRenderer;
import ormanu.qcontent.items.ModItems;
import ormanu.qcontent.network.OpenBackpackPayload;
import ormanu.qcontent.network.ToggleHoodPayload;
import ormanu.qcontent.network.ToggleMagnetPayload;

public class QContentClient implements ClientModInitializer {

    public static KeyMapping toggleHoodKey;
    public static KeyMapping toggleMagnetKey;
    public static KeyMapping openBackpackKey;

    // 1. Leave this uninitialized at the class level so it doesn't run during DataGen
    public static KeyMapping.Category CATEGORY;

    @Override
    public void onInitializeClient() {
        // 2. Safely initialize the category only when the client environment spins up
        CATEGORY = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(QContent.MOD_ID, "qcontent")
        );

        ParticleProviderRegistry.getInstance().register(QContent.SCULK_SWEEP, SculkSweepParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(QContent.SCYTHE_SWEEP, ScytheSweepParticle.Provider::new);

        ModEntityModelLayers.registerModelLayers();
        EntityRenderers.register(ModEntityTypes.CROW, CrowGeoRenderer::new);
        EntityRenderers.register(ModEntityTypes.TRAINING_DUMMY, TrainingDummyRenderer::new);

        BackpackTrinketRenderer renderer = new BackpackTrinketRenderer();
        TrinketRendererRegistry.registerRenderer(ModItems.POUCH, renderer);

        ArmorRenderer.register(
                new SlimArmorRenderer(),
                ModItems.REFINED_HELMET,
                ModItems.REFINED_CHESTPLATE,
                ModItems.REFINED_LEGGINGS,
                ModItems.REFINED_BOOTS
        );

        // 3. The rest of your keybind registrations will now safely use the initialized CATEGORY
        toggleHoodKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.qcontent.toggle_hood",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_H,
                CATEGORY
        ));
        toggleMagnetKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.qcontent.toggle_magnet",
                InputConstants.Type.MOUSE,
                InputConstants.MOUSE_BUTTON_4,
                CATEGORY
        ));
        openBackpackKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.qcontent.open_backpack",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_B,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHoodKey.consumeClick()) {
                ClientPlayNetworking.send(new ToggleHoodPayload());
            }

            while (toggleMagnetKey.consumeClick()) {
                ClientPlayNetworking.send(new ToggleMagnetPayload());
            }

            while (openBackpackKey.consumeClick()) {
                ClientPlayNetworking.send(new OpenBackpackPayload());
            }
        });
    }
}
