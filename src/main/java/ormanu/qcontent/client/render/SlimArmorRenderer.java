package ormanu.qcontent.client.render;


import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import ormanu.qcontent.QContent;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.Map;

public class SlimArmorRenderer implements ArmorRenderer {

    private final Map<EquipmentSlot, HumanoidModel<HumanoidRenderState>> wideModels = new EnumMap<>(EquipmentSlot.class);
    private final Map<EquipmentSlot, HumanoidModel<HumanoidRenderState>> slimModels = new EnumMap<>(EquipmentSlot.class);

    private static Field slimField;

    static {
        // Try multiple possible field names for compatibility
        for (String fieldName : new String[]{"slim", "field_3658", "f_ckbxpmwn"}) {
            try {
                slimField = PlayerModel.class.getDeclaredField(fieldName);
                slimField.setAccessible(true);
                break;
            } catch (NoSuchFieldException ignored) {
            }
        }
        if (slimField == null) {
            // Fallback: search by type
            for (Field field : PlayerModel.class.getDeclaredFields()) {
                if (field.getType() == boolean.class) {
                    field.setAccessible(true);
                    slimField = field;
                    break;
                }
            }
        }
    }

    public SlimArmorRenderer() {
        CubeDeformation outerDeformation = new CubeDeformation(0.6F);

        // Wide model (Steve)
        MeshDefinition wideMesh = HumanoidModel.createMesh(outerDeformation, 0.0F);
        LayerDefinition wideLayer = LayerDefinition.create(wideMesh, 64, 32);

        // Slim model (Alex) - 3px wide arms with correct armor UVs
        MeshDefinition slimMesh = HumanoidModel.createMesh(outerDeformation, 0.0F);
        PartDefinition slimRoot = slimMesh.getRoot();

        slimRoot.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, outerDeformation),
                PartPose.offset(-5.0F, 2.0F, 0.0F));

        slimRoot.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .mirror()
                        .texOffs(40, 16)
                        .addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, outerDeformation),
                PartPose.offset(5.0F, 2.0F, 0.0F));

        LayerDefinition slimLayer = LayerDefinition.create(slimMesh, 64, 32);

        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET
        }) {
            wideModels.put(slot, createSlotModel(wideLayer, slot));
            slimModels.put(slot, createSlotModel(slimLayer, slot));
        }
    }

    private HumanoidModel<HumanoidRenderState> createSlotModel(LayerDefinition layerDef, EquipmentSlot slot) {
        HumanoidModel<HumanoidRenderState> model = new HumanoidModel<>(layerDef.bakeRoot());

        // Set all invisible by default
        model.head.visible = false;
        model.hat.visible = false;
        model.body.visible = false;
        model.rightArm.visible = false;
        model.leftArm.visible = false;
        model.rightLeg.visible = false;
        model.leftLeg.visible = false;

        switch (slot) {
            case HEAD -> {
                model.head.visible = true;
                model.hat.visible = true;
            }
            case CHEST -> {
                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
            }
            case LEGS -> {
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            case FEET -> {
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
        }

        return model;
    }

    private boolean isSlimModel(HumanoidModel<?> contextModel) {
        if (slimField != null && contextModel instanceof PlayerModel) {
            try {
                return slimField.getBoolean(contextModel);
            } catch (IllegalAccessException e) {
                // Silently fail, default to wide
            }
        }
        return false;
    }

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                       ItemStack stack, HumanoidRenderState humanoidRenderState,
                       EquipmentSlot slot, int light,
                       HumanoidModel<HumanoidRenderState> contextModel) {

        if (!(submitNodeCollector instanceof OrderedSubmitNodeCollector orderedCollector)) {
            return;
        }

        boolean isLegs = slot == EquipmentSlot.LEGS;

        Identifier texture = Identifier.fromNamespaceAndPath(
                QContent.MOD_ID,
                "textures/entity/equipment/refined_layer_" + (isLegs ? "2" : "1") + ".png"
        );

        RenderType renderType = RenderTypes.entityCutoutCull(texture);

        // Detect slim model
        boolean isSlim = isSlimModel(contextModel);

        // Pick correct model
        HumanoidModel<HumanoidRenderState> modelToRender = isSlim
                ? slimModels.get(slot)
                : wideModels.get(slot);

        // IMPORTANT: Always reset visibility before rendering
        // This prevents state leaking between frames/players
        switch (slot) {
            case HEAD -> {
                CustomData customData = stack.getOrDefault(
                        DataComponents.CUSTOM_DATA, CustomData.EMPTY
                );
                boolean hoodDown = customData.copyTag()
                        .getBoolean("HoodDown").orElse(false);

                modelToRender.hat.visible = !hoodDown;
                modelToRender.head.visible = !hoodDown;
                modelToRender.body.visible = false;
                modelToRender.rightArm.visible = false;
                modelToRender.leftArm.visible = false;
                modelToRender.rightLeg.visible = false;
                modelToRender.leftLeg.visible = false;
            }
            case CHEST -> {
                modelToRender.head.visible = false;
                modelToRender.hat.visible = false;
                modelToRender.body.visible = true;
                modelToRender.rightArm.visible = true;
                modelToRender.leftArm.visible = true;
                modelToRender.rightLeg.visible = false;
                modelToRender.leftLeg.visible = false;
            }
            case LEGS -> {
                modelToRender.head.visible = false;
                modelToRender.hat.visible = false;
                modelToRender.body.visible = true;
                modelToRender.rightArm.visible = false;
                modelToRender.leftArm.visible = false;
                modelToRender.rightLeg.visible = true;
                modelToRender.leftLeg.visible = true;
            }
            case FEET -> {
                modelToRender.head.visible = false;
                modelToRender.hat.visible = false;
                modelToRender.body.visible = false;
                modelToRender.rightArm.visible = false;
                modelToRender.leftArm.visible = false;
                modelToRender.rightLeg.visible = true;
                modelToRender.leftLeg.visible = true;
            }
        }

        ArmorRenderer.submitTransformCopyingModel(
                contextModel,
                humanoidRenderState,
                modelToRender,
                humanoidRenderState,
                false,
                orderedCollector,
                poseStack,
                renderType,
                light,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF,
                null,
                0x00000000,
                null
        );
    }
}