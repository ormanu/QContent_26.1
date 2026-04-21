package ormanu.qcontent.entity.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TrainingDummyModel extends EntityModel<TrainingDummyRenderState> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart arms;
    private final ModelPart wood;

    public TrainingDummyModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.arms = root.getChild("arms");
        this.wood = root.getChild("wood");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.0F, -22.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 16.0F, 0.0F)
        );

        PartDefinition body = partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-3.0F, -15.0F, -2.0F, 6.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 16.0F, 0.0F)
        );

        body.addOrReplaceChild(
                "cube_r1",
                CubeListBuilder.create()
                        .texOffs(0, 27)
                        .addBox(0.0F, -11.0F, -1.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.6F, -4.2F, -1.0F, 0.0F, 0.0F, 0.1745F)
        );

        body.addOrReplaceChild(
                "cube_r2",
                CubeListBuilder.create()
                        .texOffs(24, 0)
                        .addBox(0.0F, -11.0F, -1.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.6F, -3.9F, -1.0F, 0.0F, 0.0F, -0.1745F)
        );

        PartDefinition arms = partdefinition.addOrReplaceChild(
                "arms",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 16.0F, 0.0F)
        );

        arms.addOrReplaceChild(
                "cube_r3",
                CubeListBuilder.create()
                        .texOffs(12, 27)
                        .addBox(-1.0F, -3.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(7.6F, -11.6F, 0.0F, 0.0F, 0.0F, -0.9599F)
        );

        arms.addOrReplaceChild(
                "cube_r4",
                CubeListBuilder.create()
                        .texOffs(28, 18)
                        .addBox(-1.0F, -3.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.6F, -11.9F, 0.0F, 0.0F, 0.0F, 0.9599F)
        );

        partdefinition.addOrReplaceChild(
                "wood",
                CubeListBuilder.create()
                        .texOffs(20, 18)
                        .addBox(-1.0F, -4.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 12)
                        .addBox(-2.0F, 6.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 7)
                        .addBox(-1.0F, -16.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 16.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(TrainingDummyRenderState state) {
        // keep static (or add small idle wobble later)
    }

}