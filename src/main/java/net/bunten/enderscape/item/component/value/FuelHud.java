package net.bunten.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.Enderscape;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record FuelHud(
        boolean visible,
        Identifier empty,
        Identifier fueled,
        Identifier invalidOverlay,
        Identifier outline,
        Identifier costOverlay
) {

    public static final boolean DEFAULT_VISIBILITY = true;
    public static final Identifier DEFAULT_EMPTY_SEGMENTS = Enderscape.id("nebulite_tool/hud/empty_segments");
    public static final Identifier DEFAULT_FUELED_SEGMENTS = Enderscape.id("nebulite_tool/hud/fueled_segments");
    public static final Identifier DEFAULT_INVALID_OVERLAY_SEGMENTS = Enderscape.id("nebulite_tool/hud/invalid_overlay_segments");
    public static final Identifier DEFAULT_OUTLINE = Enderscape.id("nebulite_tool/hud/transdimensional_outline");
    public static final Identifier DEFAULT_COST_OVERLAY_SEGMENT = Enderscape.id("nebulite_tool/hud/cost_overlay_segment");

    public static final Codec<FuelHud> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("visible", DEFAULT_VISIBILITY).forGetter(FuelHud::visible),
                    Identifier.CODEC.optionalFieldOf("empty", DEFAULT_EMPTY_SEGMENTS).forGetter(FuelHud::empty),
                    Identifier.CODEC.optionalFieldOf("fueled", DEFAULT_FUELED_SEGMENTS).forGetter(FuelHud::fueled),
                    Identifier.CODEC.optionalFieldOf("invalid_overlay", DEFAULT_INVALID_OVERLAY_SEGMENTS).forGetter(FuelHud::invalidOverlay),
                    Identifier.CODEC.optionalFieldOf("outline", DEFAULT_OUTLINE).forGetter(FuelHud::outline),
                    Identifier.CODEC.optionalFieldOf("cost_overlay", DEFAULT_COST_OVERLAY_SEGMENT).forGetter(FuelHud::costOverlay)
            ).apply(instance, FuelHud::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FuelHud> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            FuelHud::visible,
            Identifier.STREAM_CODEC,
            FuelHud::empty,
            Identifier.STREAM_CODEC,
            FuelHud::fueled,
            Identifier.STREAM_CODEC,
            FuelHud::invalidOverlay,
            Identifier.STREAM_CODEC,
            FuelHud::outline,
            Identifier.STREAM_CODEC,
            FuelHud::costOverlay,
            FuelHud::new
    );

    public static final FuelHud DEFAULT = new FuelHud(
            DEFAULT_VISIBILITY,
            DEFAULT_EMPTY_SEGMENTS,
            DEFAULT_FUELED_SEGMENTS,
            DEFAULT_INVALID_OVERLAY_SEGMENTS,
            DEFAULT_OUTLINE,
            DEFAULT_COST_OVERLAY_SEGMENT
    );

    public static final FuelHud HIDDEN = new FuelHud(
            false,
            DEFAULT_EMPTY_SEGMENTS,
            DEFAULT_FUELED_SEGMENTS,
            DEFAULT_INVALID_OVERLAY_SEGMENTS,
            DEFAULT_OUTLINE,
            DEFAULT_COST_OVERLAY_SEGMENT
    );
}