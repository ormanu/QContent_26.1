package ormanu.qcontent.entity.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class CrowEntityModel extends EntityModel<CrowEntityRenderState> {
    private final ModelPart LEFT_WING;
    private final ModelPart RIGHT_WING;
    private final ModelPart tail;
    private final ModelPart head;
    private final ModelPart RIGHT_LEG;
    private final ModelPart LEFT_LEG;
    private final ModelPart body;

    private final float leftWingBaseX, leftWingBaseY, leftWingBaseZ;
    private final float rightWingBaseX, rightWingBaseY, rightWingBaseZ;
    private final float rightLegBaseX, rightLegBaseY, rightLegBaseZ;
    private final float leftLegBaseX, leftLegBaseY, leftLegBaseZ;
    private final float tailBaseX, tailBaseY, tailBaseZ;

    public CrowEntityModel(ModelPart root) {
        super(root);
        this.LEFT_WING = root.getChild("LEFT_WING");
        this.RIGHT_WING = root.getChild("RIGHT_WING");
        this.tail = root.getChild("tail");
        this.head = root.getChild("head");
        this.RIGHT_LEG = root.getChild("RIGHT_LEG");
        this.LEFT_LEG = root.getChild("LEFT_LEG");
        this.body = root.getChild("body");

        this.leftWingBaseX = LEFT_WING.x;
        this.leftWingBaseY = LEFT_WING.y;
        this.leftWingBaseZ = LEFT_WING.z;

        this.rightWingBaseX = RIGHT_WING.x;
        this.rightWingBaseY = RIGHT_WING.y;
        this.rightWingBaseZ = RIGHT_WING.z;

        this.rightLegBaseX = RIGHT_LEG.x;
        this.rightLegBaseY = RIGHT_LEG.y;
        this.rightLegBaseZ = RIGHT_LEG.z;

        this.leftLegBaseX = LEFT_LEG.x;
        this.leftLegBaseY = LEFT_LEG.y;
        this.leftLegBaseZ = LEFT_LEG.z;

        this.tailBaseX = tail.x;
        this.tailBaseY = tail.y;
        this.tailBaseZ = tail.z;
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeDeformation d = new CubeDeformation(0.0F);

        PartDefinition leftWing = root.addOrReplaceChild(
                "LEFT_WING",
                CubeListBuilder.create(),
                PartPose.offset(-3.0F, 17.5505F, -0.4142F)
        );
        leftWing.addOrReplaceChild(
                "left_wing_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.25F, -2.0F, -4.0F, 0.5F, 4.0F, 8.0F, d),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F)
        );

        PartDefinition rightWing = root.addOrReplaceChild(
                "RIGHT_WING",
                CubeListBuilder.create(),
                PartPose.offset(2.0F, 17.5505F, -0.4142F)
        );
        rightWing.addOrReplaceChild(
                "right_wing_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.25F, -2.0F, -4.0F, 0.5F, 4.0F, 8.0F, d),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F)
        );

        PartDefinition tail = root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 18.5F, 4.5F)
        );
        tail.addOrReplaceChild(
                "cube_r1",
                CubeListBuilder.create()
                        .texOffs(15, 23)
                        .addBox(-0.75F, -0.5F, -2.0F, 1.5F, 1.0F, 4.0F, d),
                PartPose.offsetAndRotation(-0.5F, -0.0305F, 1.1176F, -0.4363F, 0.0F, 0.0F)
        );

        PartDefinition head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 17.0F, -4.0F)
        );
        head.addOrReplaceChild(
                "cube_r2",
                CubeListBuilder.create()
                        .texOffs(0, 24)
                        .addBox(-0.5F, -1.0F, -1.5F, 1.0F, 2.0F, 3.0F, d),
                PartPose.offsetAndRotation(-0.5F, -1.5176F, -2.9695F, 0.4363F, 0.0F, 0.0F)
        );
        head.addOrReplaceChild(
                "cube_r3",
                CubeListBuilder.create()
                        .texOffs(16, 12)
                        .addBox(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, d),
                PartPose.offsetAndRotation(-0.5F, -1.5239F, -0.3921F, 0.4363F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "RIGHT_LEG",
                CubeListBuilder.create()
                        .texOffs(6, 24)
                        .addBox(2.0F, -5.0F, -1.0F, 1.5F, 5.0F, 0.1F, d)
                        .texOffs(4, 25)
                        .addBox(2.0F, -0.1F, -4.0F, 1.5F, 0.1F, 3.0F, d),
                PartPose.offset(-1.75F, 24.0F, 1.0F)
        );

        root.addOrReplaceChild(
                "LEFT_LEG",
                CubeListBuilder.create()
                        .texOffs(6, 24)
                        .addBox(-0.75F, -5.0F, -1.0F, 1.5F, 5.0F, 0.1F, d)
                        .texOffs(4, 25)
                        .addBox(-0.75F, -0.1F, -4.0F, 1.5F, 0.1F, 3.0F, d),
                PartPose.offset(-2.0F, 24.0F, 1.0F)
        );

        PartDefinition body = root.addOrReplaceChild(
                "body",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 20.0F, 1.0F)
        );
        body.addOrReplaceChild(
                "cube_r4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.25F, -2.0F, -4.0F, 4.5F, 4.0F, 8.0F, d),
                PartPose.offsetAndRotation(-0.5F, -2.4495F, -1.4142F, -0.2618F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }

    // --- animation constants taken from your .bbmodel ---
    private static final float FLY_LEN = 2.0f; // seconds
    private static final float[] KF_T = {0f, 0.25f, 0.625f, 0.91667f, 1.25f, 1.625f, 2f};

    // LEFT_WING rotation (deg)
    private static final float[] L_RX = {0f, -5.0931f, -14.5564f, -22.5877f, -14.5564f, -5.0931f, 0f};
    private static final float[] L_RY = {0f, -10.9577f, -15.4306f, -12.8370f, -15.4306f, -10.9577f, 0f};
    private static final float[] L_RZ = {0f,  50.9705f,  90.4252f, 122.0309f,  90.4252f,  50.9705f, 0f};

    // LEFT_WING position
    private static final float[] L_PX = {0f, 1.1f, 1.8f, 1.6f, 1.8f, 1.1f, 0f};
    private static final float[] L_PY = {0f, 0.7f, 1.7f, 2.7f, 1.7f, 0.7f, 0f};
    private static final float[] L_PZ = {0f, 0.1f, 0.4f, 0.7f, 0.4f, 0.1f, 0f};

    // RIGHT_WING rotation (deg)
    private static final float[] R_RX = {0f, -2.5f, -13.5392f, -22.0247f, -12.5392f, -2.5f, 0f};
    private static final float[] R_RY = {0f,  7.5f,  14.0656f,  10.7987f,  14.0656f,  7.5f, 0f};
    private static final float[] R_RZ = {0f, -30f, -87.3051f, -118.2571f, -87.3051f, -30f, 0f};

    // RIGHT_WING position
    private static final float[] R_PX = {0f, -1f, -1.6f, -1.6f, -1.6f, -1f, 0f};
    private static final float[] R_PY = {0f,  0.1f,  1.7f,  2.7f,  1.7f,  0.1f, 0f};
    private static final float[] R_PZ = {0f,  0f,    0.4f,  0.7f,  0.3f,  0f,   0f};

    private static float sample(float t, float[] times, float[] values) {
        if (t <= times[0]) return values[0];
        for (int i = 0; i < times.length - 1; i++) {
            float t0 = times[i];
            float t1 = times[i + 1];
            if (t <= t1) {
                float a = (t - t0) / (t1 - t0);
                return Mth.lerp(a, values[i], values[i + 1]);
            }
        }
        return values[values.length - 1];
    }

    @Override
    public void setupAnim(CrowEntityRenderState state) {
        super.setupAnim(state);

        // Always reset wings to base each frame (since we animate position)
        LEFT_WING.setPos(leftWingBaseX, leftWingBaseY, leftWingBaseZ);
        RIGHT_WING.setPos(rightWingBaseX, rightWingBaseY, rightWingBaseZ);
        LEFT_WING.xRot = LEFT_WING.yRot = LEFT_WING.zRot = 0f;
        RIGHT_WING.xRot = RIGHT_WING.yRot = RIGHT_WING.zRot = 0f;
        // ALWAYS reset legs first
        RIGHT_LEG.setPos(rightLegBaseX, rightLegBaseY, rightLegBaseZ);
        LEFT_LEG.setPos(leftLegBaseX, leftLegBaseY, leftLegBaseZ);
        RIGHT_LEG.xRot = RIGHT_LEG.yRot = RIGHT_LEG.zRot = 0f;
        LEFT_LEG.xRot  = LEFT_LEG.yRot  = LEFT_LEG.zRot  = 0f;

        if (state.onGround) {
            // walking legs
            float amp = state.walkAnimationSpeed;
            float pos = state.walkAnimationPos;
            LEFT_LEG.xRot = Mth.cos(pos * 0.2f + Mth.PI) * 1.4f * amp;
            RIGHT_LEG.xRot = Mth.cos(pos * 0.2f) * 1.4f * amp;

            // (do your ground wing pose here too if you want)
            return;
        }

        // flying: tuck legs
        float tuckUp = 4.0F;
        float tuckBack = 0.6F;
        float tuckIn = 0.4F;

        RIGHT_LEG.setPos(rightLegBaseX + tuckIn, rightLegBaseY - tuckUp, rightLegBaseZ - tuckBack);
        LEFT_LEG.setPos(leftLegBaseX - tuckIn, leftLegBaseY - tuckUp, leftLegBaseZ - tuckBack);

        RIGHT_LEG.xRot = -1.2F;
        LEFT_LEG.xRot  = -1.2F;
        RIGHT_LEG.zRot =  0.25F;
        LEFT_LEG.zRot  = -0.25F;

        // ticks -> seconds, loop 0..2
        float tSec = (state.ageInTicks / 20.0f) % FLY_LEN;

        // LEFT wing
        float lrx = sample(tSec, KF_T, L_RX);
        float lry = sample(tSec, KF_T, L_RY);
        float lrz = sample(tSec, KF_T, L_RZ);
        float lpx = sample(tSec, KF_T, L_PX);
        float lpy = sample(tSec, KF_T, L_PY);
        float lpz = sample(tSec, KF_T, L_PZ);

        LEFT_WING.xRot = -lrx * Mth.DEG_TO_RAD;
        LEFT_WING.yRot = -lry * Mth.DEG_TO_RAD;
        LEFT_WING.zRot =  lrz * Mth.DEG_TO_RAD;
        LEFT_WING.setPos(leftWingBaseX - lpx, leftWingBaseY - lpy, leftWingBaseZ + lpz);

        // RIGHT wing
        float rrx = sample(tSec, KF_T, R_RX);
        float rry = sample(tSec, KF_T, R_RY);
        float rrz = sample(tSec, KF_T, R_RZ);
        float rpx = sample(tSec, KF_T, R_PX);
        float rpy = sample(tSec, KF_T, R_PY);
        float rpz = sample(tSec, KF_T, R_PZ);

        RIGHT_WING.xRot = -rrx * Mth.DEG_TO_RAD;
        RIGHT_WING.yRot = -rry * Mth.DEG_TO_RAD;
        RIGHT_WING.zRot =  rrz * Mth.DEG_TO_RAD;
        RIGHT_WING.setPos(rightWingBaseX - rpx, rightWingBaseY - rpy, rightWingBaseZ + rpz);

    }
}