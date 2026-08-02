package net.penumbra.enderscape.registry.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.penumbra.enderscape.Enderscape;

public class EnderscapePoiTags {

    public static final TagKey<PoiType> DRIFTER_HOME = register("drifter_home");
    public static final TagKey<PoiType> RUSTLE_SLEEPING_SPOT = register("rustle_sleeping_spot");

    private static TagKey<PoiType> register(String name) {
        return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, Enderscape.id(name));
    }
}
