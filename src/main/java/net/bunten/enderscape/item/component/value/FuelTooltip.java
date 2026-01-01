package net.bunten.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.network.EnderscapeCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

public record FuelTooltip(
        boolean visible,
        ResourceLocation empty,
        ResourceLocation fueled,
        Vec2 offset
) {

    public static final boolean DEFAULT_VISIBILITY = true;
    public static final ResourceLocation DEFAULT_EMPTY_SEGMENTS = Enderscape.id("nebulite_tool/tooltip/empty_segments");
    public static final ResourceLocation DEFAULT_FUELED_SEGMENT = Enderscape.id("nebulite_tool/tooltip/fueled_segment");
    public static final Vec2 DEFAULT_OFFSET = new Vec2(0, 0);
    
    public static final Codec<FuelTooltip> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("visible", DEFAULT_VISIBILITY).forGetter(FuelTooltip::visible),
                    ResourceLocation.CODEC.optionalFieldOf("empty", DEFAULT_EMPTY_SEGMENTS).forGetter(FuelTooltip::empty),
                    ResourceLocation.CODEC.optionalFieldOf("fueled", DEFAULT_FUELED_SEGMENT).forGetter(FuelTooltip::fueled),
                    Vec2.CODEC.optionalFieldOf("offset", DEFAULT_OFFSET).forGetter(FuelTooltip::offset)
            ).apply(instance, FuelTooltip::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FuelTooltip> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            FuelTooltip::visible,
            ResourceLocation.STREAM_CODEC,
            FuelTooltip::empty,
            ResourceLocation.STREAM_CODEC,
            FuelTooltip::fueled,
            EnderscapeCodecs.VEC2_STREAM,
            FuelTooltip::offset,
            FuelTooltip::new
    );

    public static final FuelTooltip DEFAULT = new FuelTooltip(
            DEFAULT_VISIBILITY,
            DEFAULT_EMPTY_SEGMENTS,
            DEFAULT_FUELED_SEGMENT,
            DEFAULT_OFFSET
    );

    public static final FuelTooltip HIDDEN = new FuelTooltip(
            false,
            DEFAULT_EMPTY_SEGMENTS,
            DEFAULT_FUELED_SEGMENT,
            DEFAULT_OFFSET
    );
}