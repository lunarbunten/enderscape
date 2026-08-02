package net.penumbra.enderscape.util;

import net.minecraft.core.Registry;
import net.minecraft.world.level.dimension.LevelStem;

public class MixinUtil {
    /// Used in {@link net.penumbra.enderscape.mixin.level.WorldDimensionsMixin}
    public static final ScopedValue<Registry<LevelStem>> BASE_REGISTRIES = ScopedValue.newInstance();
}
