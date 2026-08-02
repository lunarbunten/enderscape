package net.penumbra.enderscape.registry.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeBannerPatternTags {

    public static final TagKey<BannerPattern> PATTERN_ITEM_CRESCENT = create("pattern_item/crescent");

    private static TagKey<BannerPattern> create(String string) {
        return TagKey.create(Registries.BANNER_PATTERN, Enderscape.id(string));
    }
}