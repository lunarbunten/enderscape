package net.penumbra.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.network.EnderscapeCodecs;

public record FuelTooltip(
        boolean visible,
        Identifier empty,
        Identifier fueled,
        Vec2 offset
) {

    private static final boolean DEFAULT_VISIBILITY = true;
    private static final Identifier DEFAULT_EMPTY_SEGMENTS = Enderscape.id("nebulite_tool/tooltip/empty_segments");
    private static final Identifier DEFAULT_FUELED_SEGMENT = Enderscape.id("nebulite_tool/tooltip/fueled_segment");
    private static final Vec2 DEFAULT_OFFSET = new Vec2(0, 0);
    
    public static final Codec<FuelTooltip> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("visible", DEFAULT_VISIBILITY).forGetter(FuelTooltip::visible),
                    Identifier.CODEC.optionalFieldOf("empty", DEFAULT_EMPTY_SEGMENTS).forGetter(FuelTooltip::empty),
                    Identifier.CODEC.optionalFieldOf("fueled", DEFAULT_FUELED_SEGMENT).forGetter(FuelTooltip::fueled),
                    Vec2.CODEC.optionalFieldOf("offset", DEFAULT_OFFSET).forGetter(FuelTooltip::offset)
            ).apply(instance, FuelTooltip::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FuelTooltip> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            FuelTooltip::visible,
            Identifier.STREAM_CODEC,
            FuelTooltip::empty,
            Identifier.STREAM_CODEC,
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