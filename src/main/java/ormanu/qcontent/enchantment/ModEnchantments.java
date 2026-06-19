package ormanu.qcontent.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import ormanu.qcontent.QContent;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> DEEP_POCKETS = key("deep_pockets");

    private static ResourceKey<Enchantment> key(String path) {
        Identifier id = Identifier.fromNamespaceAndPath(QContent.MOD_ID, path);
        return ResourceKey.create(Registries.ENCHANTMENT, id);
    }
}
