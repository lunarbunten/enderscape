package net.bunten.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.Enderscape;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record FuelHud(
        boolean visible,
        ResourceLocation empty,
        ResourceLocation fueled,
        ResourceLocation invalidOverlay,
        ResourceLocation outline,
        ResourceLocation costOverlay
) {

    public static final boolean DEFAULT_VISIBILITY = true;
    public static final ResourceLocation DEFAULT_EMPTY_SEGMENTS = Enderscape.id("nebulite_tool/hud/empty_segments");
    public static final ResourceLocation DEFAULT_FUELED_SEGMENTS = Enderscape.id("nebulite_tool/hud/fueled_segments");
    public static final ResourceLocation DEFAULT_INVALID_OVERLAY_SEGMENTS = Enderscape.id("nebulite_tool/hud/invalid_overlay_segments");
    public static final ResourceLocation DEFAULT_OUTLINE = Enderscape.id("nebulite_tool/hud/transdimensional_outline");
    public static final ResourceLocation DEFAULT_COST_OVERLAY_SEGMENT = Enderscape.id("nebulite_tool/hud/cost_overlay_segment");

    public static final Codec<FuelHud> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("visible", DEFAULT_VISIBILITY).forGetter(FuelHud::visible),
                    ResourceLocation.CODEC.optionalFieldOf("empty", DEFAULT_EMPTY_SEGMENTS).forGetter(FuelHud::empty),
                    ResourceLocation.CODEC.optionalFieldOf("fueled", DEFAULT_FUELED_SEGMENTS).forGetter(FuelHud::fueled),
                    ResourceLocation.CODEC.optionalFieldOf("invalid_overlay", DEFAULT_INVALID_OVERLAY_SEGMENTS).forGetter(FuelHud::invalidOverlay),
                    ResourceLocation.CODEC.optionalFieldOf("outline", DEFAULT_OUTLINE).forGetter(FuelHud::outline),
                    ResourceLocation.CODEC.optionalFieldOf("cost_overlay", DEFAULT_COST_OVERLAY_SEGMENT).forGetter(FuelHud::costOverlay)
            ).apply(instance, FuelHud::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FuelHud> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            FuelHud::visible,
            ResourceLocation.STREAM_CODEC,
            FuelHud::empty,
            ResourceLocation.STREAM_CODEC,
            FuelHud::fueled,
            ResourceLocation.STREAM_CODEC,
            FuelHud::invalidOverlay,
            ResourceLocation.STREAM_CODEC,
            FuelHud::outline,
            ResourceLocation.STREAM_CODEC,
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