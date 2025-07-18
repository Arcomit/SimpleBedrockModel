package example.init;

import example.block.TestBlock;
import example.block.blockentity.TestBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ExampleModRegister {
    /**
     * 注册名用 example，方便 build 时排除
     */
    public static final String MOD_ID = "example";

    public static Block TEST_BLOCK;
    public static BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY_TYPE;
    public static BlockItem TEST_BLOCK_ITEM;
    public static CreativeModeTab TEST_TAB;

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();

        if (BuiltInRegistries.BLOCK.equals(registry)) {
            TEST_BLOCK = new TestBlock();
            event.register(BuiltInRegistries.BLOCK.key(), modLoc("test_block"), () -> TEST_BLOCK);
        }

        if (BuiltInRegistries.BLOCK_ENTITY_TYPE.equals(registry)) {
            TEST_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.of(TestBlockEntity::new, TEST_BLOCK).build(null);
            event.register(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), modLoc("test_block_entity_type"), () -> TEST_BLOCK_ENTITY_TYPE);
        }

        if (BuiltInRegistries.ITEM.equals(registry)) {
            TEST_BLOCK_ITEM = new BlockItem(TEST_BLOCK, new BlockItem.Properties());
            event.register(BuiltInRegistries.ITEM.key(), modLoc("test_block_item"), () -> TEST_BLOCK_ITEM);
        }

        if (Registries.CREATIVE_MODE_TAB.equals(event.getRegistryKey())) {
            TEST_TAB = CreativeModeTab.builder().title(Component.translatable("item_group.example.name"))
                    .icon(() -> TEST_BLOCK_ITEM.getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(TEST_BLOCK_ITEM);
                    }).build();
            event.register(Registries.CREATIVE_MODE_TAB, modLoc("test_tab"), () -> TEST_TAB);
        }
    }

    public static ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
