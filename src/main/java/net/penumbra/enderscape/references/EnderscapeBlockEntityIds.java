package net.penumbra.enderscape.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeBlockEntityIds {
    public static final ResourceKey<BlockEntityType<?>> MAGNIA_SPROUT = of("magnia_sprout");
    public static final ResourceKey<BlockEntityType<?>> MAGNIA_RADIO = of("magnia_radio");
    public static final ResourceKey<BlockEntityType<?>> END_HAVEN_CORE = of("end_haven_core");

    public static ResourceKey<BlockEntityType<?>> of(String name) {
        return ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, Enderscape.id(name));
    }
}