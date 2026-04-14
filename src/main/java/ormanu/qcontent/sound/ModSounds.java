package ormanu.qcontent.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import ormanu.qcontent.QContent;

public class ModSounds {

    public static final SoundEvent ScytheHit = registerSound("scythe_hit");
    public static final SoundEvent Parry = registerSound("parry");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(QContent.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void initialize() {
        QContent.LOGGER.info("Registering Sounds");
    }
}
