package com.github.mcmodderanchor.simplebedrockmodel.v1.client.bedrock;

import com.github.mcmodderanchor.simplebedrockmodel.v1.client.bedrock.pojo.FaceItem;
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.bedrock.pojo.FaceUVsItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class BedrockModelUtil {
    public static FaceUVsItem singleSouthFace() {
        return new FaceUVsItem(emptyFace(), emptyFace(), emptyFace(), single16xFace(), emptyFace(), emptyFace());
    }

    public static FaceItem single16xFace() {
        return new FaceItem(new float[]{0, 0}, new float[]{16, 16});
    }

    public static FaceItem emptyFace() {
        return new FaceItem(new float[]{0, 0}, new float[]{0, 0});
    }
}
