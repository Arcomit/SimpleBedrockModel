package example.client.manager;

import com.github.mcmodderanchor.simplebedrockmodel.SimpleBedrockModel;
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.bedrock.animation.BedrockAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import java.util.Map;
import java.util.Set;

@EventBusSubscriber(modid = SimpleBedrockModel.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class BedrockAnimationRegister {
    public static BedrockAnimationRegister INSTANCE = null;
    private final BedrockAnimationSet animationSet;

    private BedrockAnimationRegister(BedrockAnimationSet animationSet) {
        this.animationSet = animationSet;
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListenersEvent(RegisterClientReloadListenersEvent event) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager manager) {
            INSTANCE = new BedrockAnimationRegister(new BedrockAnimationSet());
            ModLoader.postEvent(new BedrockAnimationRegisterEvent(INSTANCE.animationSet));
            // 将注册冻结
            INSTANCE.animationSet.immutableKnowLocations();
            manager.listeners.add(INSTANCE.animationSet);
        }
    }

    public Map<String, BedrockAnimation> getAnimations(ResourceLocation location) {
        return animationSet.getAnimations().get(location);
    }

    public Set<ResourceLocation> getAllAnimationKey() {
        return animationSet.getAnimations().keySet();
    }
}
