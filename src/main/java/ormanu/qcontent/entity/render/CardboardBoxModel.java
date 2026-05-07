package ormanu.qcontent.entity.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public class CardboardBoxModel extends EntityModel<AvatarRenderState> {
    private final ModelPart box;

    public CardboardBoxModel(ModelPart root) {
        super(root);
        this.box = root.getChild("box");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeDeformation d = new CubeDeformation(0.0F);

        PartDefinition box = root.addOrReplaceChild(
                "box",
                CubeListBuilder.create()
                        .texOffs(0, 15)
                        .addBox(-1.0F, -10.0F, -14.0F, 1.0F, 10.0F, 16.0F, d)
                        .texOffs(34, 15)
                        .addBox(12.0F, -10.0F, -14.0F, 1.0F, 10.0F, 16.0F, d),
                PartPose.offset(-6.0F, 24.0F, 6.0F)
        );

        box.addOrReplaceChild(
                "cube_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -1.0F, -12.0F, 16.0F, 1.0F, 14.0F, d)
                        .texOffs(26, 41)
                        .addBox(-1.0F, 0.0F, -11.0F, 1.0F, 10.0F, 12.0F, d),
                PartPose.offsetAndRotation(1.0F, -10.0F, -13.0F, 0.0F, -1.5708F, 0.0F)
        );

        box.addOrReplaceChild(
                "cube_r2",
                CubeListBuilder.create()
                        .texOffs(0, 41)
                        .addBox(-1.0F, -10.0F, -11.0F, 1.0F, 10.0F, 12.0F, d),
                PartPose.offsetAndRotation(1.0F, 0.0F, 2.0F, 0.0F, -1.5708F, 0.0F)
        );

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(AvatarRenderState state) {
        // static; your RenderLayer can add bounce by translating the PoseStack
    }
}
