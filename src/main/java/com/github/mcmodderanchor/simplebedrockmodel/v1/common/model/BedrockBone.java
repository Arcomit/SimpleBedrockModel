package com.github.mcmodderanchor.simplebedrockmodel.v1.common.model;

import com.maydaymemory.mae.basic.BoneTransform;
import com.maydaymemory.mae.basic.ZYXRotationView;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.renderer.LightTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BedrockBone {
    private static final Vector3f[] NORMALS = new Vector3f[6];
    private static final int MAX_LIGHT_TEXTURE = LightTexture.pack(15, 15);

    public final ObjectList<BedrockCube> cubes = new ObjectArrayList<>();
    private final ObjectList<BedrockBone> children = new ObjectArrayList<>();
    public BedrockBone parent;
    public int index = -1;
    public float x;
    public float y;
    public float z;
    public Quaternionf rotation = new Quaternionf();
    /** 这个旋转不会应用到渲染，只会用来生成 bind pose。*/
    public Vector3f rotationInEuler = new Vector3f();
    public float xScale = 1;
    public float yScale = 1;
    public float zScale = 1;
    public boolean visible = true;
    public boolean illuminated = false;
    public boolean mirror;

    static {
        for (int i = 0; i < NORMALS.length; i++) {
            NORMALS[i] = new Vector3f();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack poseStack, VertexConsumer consumer, int lightmap, int overlay) {
        this.render(poseStack, consumer, lightmap, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack poseStack, VertexConsumer consumer, int lightmap, int overlay, float red, float green, float blue, float alpha) {
        int cubePackedLight = illuminated ? MAX_LIGHT_TEXTURE : lightmap;
        if (this.visible) {
            // 缩放过小时，直接退出渲染
            boolean xNearZero = -1E-5F < xScale && xScale < 1E-5F;
            boolean yNearZero = -1E-5F < yScale && yScale < 1E-5F;
            boolean zNearZero = -1E-5F < zScale && zScale < 1E-5F;
            if ((xNearZero && yNearZero) || (xNearZero && zNearZero) || (yNearZero && zNearZero)) {
                return;
            }

            if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
                poseStack.pushPose();
                this.translateAndRotateAndScale(poseStack);
                this.compile(poseStack.last(), consumer, cubePackedLight, overlay, red, green, blue, alpha);

                for (BedrockBone part : this.children) {
                    part.render(poseStack, consumer, cubePackedLight, overlay, red, green, blue, alpha);
                }

                poseStack.popPose();
            }
        }
    }

    public void translateAndRotateAndScale(PoseStack poseStack) {
        poseStack.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
        poseStack.last().pose().rotate(rotation);
        poseStack.last().normal().rotate(rotation);
        if (this.xScale != 0.0F || this.yScale != 0.0F || this.zScale != 0.0F) {
            poseStack.last().pose().scale(this.xScale, this.yScale, this.zScale);
            poseStack.last().normal().scale(this.xScale, this.yScale, this.zScale);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void compile(PoseStack.Pose pose, VertexConsumer consumer, int lightmap, int overlay, float red, float green, float blue, float alpha) {
        Matrix3f normal = pose.normal();
        NORMALS[0].set(-normal.m10, -normal.m11, -normal.m12);
        NORMALS[1].set(normal.m10, normal.m11, normal.m12);
        NORMALS[2].set(-normal.m20, -normal.m21, -normal.m22);
        NORMALS[3].set(normal.m20, normal.m21, normal.m22);
        NORMALS[4].set(-normal.m00, -normal.m01, -normal.m02);
        NORMALS[5].set(normal.m00, normal.m01, normal.m02);
        for (BedrockCube bedrockCube : this.cubes) {
            bedrockCube.compile(pose, NORMALS, consumer, lightmap, overlay, red, green, blue, alpha);
        }
    }

    public BoneTransform getBoneTransform() {
        return new BoneTransform(index, new Vector3f(x, y, z), new ZYXRotationView(rotation), new Vector3f(xScale, yScale, zScale));
    }

    public boolean isEmpty() {
        return this.cubes.isEmpty();
    }

    public void addChild(BedrockBone model) {
        this.children.add(model);
    }

    public ObjectList<BedrockBone> getChildren() {
        return children;
    }
}
