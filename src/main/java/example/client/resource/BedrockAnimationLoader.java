package example.client.resource;

import com.github.mcmodderanchor.simplebedrockmodel.v1.client.bedrock.animation.Animations;
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.bedrock.animation.BedrockAnimation;
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.bedrock.animation.BedrockModelBoneIndexProvider;
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.bedrock.model.BedrockModel;
import com.google.common.collect.ImmutableMap;
import example.client.manager.BedrockAnimationRegister;
import example.client.manager.BedrockAnimationRegisterEvent;
import example.init.ExampleModRegister;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class BedrockAnimationLoader {
    public static final ResourceLocation TEST_ANIMATION = ExampleModRegister.modLoc("bedrock/test");

    @SubscribeEvent
    public static void onRegisterBedrockModelRenderers(BedrockAnimationRegisterEvent event) {
        event.register(TEST_ANIMATION, pojo -> {
            BedrockModel model = BedrockModelLoader.getModel(BedrockModelLoader.TEST_MODEL);
            List<BedrockAnimation> animation = Animations.createAnimation(pojo, new BedrockModelBoneIndexProvider(model));
            return ImmutableMap.copyOf(animation.stream().collect(Collectors.toMap(BedrockAnimation::getName, a -> a)));
        });
    }

    public static Map<String, BedrockAnimation> getAnimations(ResourceLocation location) {
        return BedrockAnimationRegister.INSTANCE.getAnimations(location);
    }
}
