package ormanu.qcontent.items.armor;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import ormanu.qcontent.QContent;
import ormanu.qcontent.datagen.QItemTagProvider;

import java.util.Map;

public class RefinedArmorMaterial {

    public static final int BASE_DURABILITY = 40;

    public static final ResourceKey<EquipmentAsset> REFINED_ARMOR_MATERIAL_KEY =
            ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(QContent.MOD_ID, "refined"));

    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    ArmorType.HELMET, 3,
                    ArmorType.CHESTPLATE, 8,
                    ArmorType.LEGGINGS, 6,
                    ArmorType.BOOTS, 3,
                    ArmorType.BODY, 20
            ),
            30,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            3.5F,
            0.3F,
            QItemTagProvider.REFINED_REPAIR,
            REFINED_ARMOR_MATERIAL_KEY
    );
}
