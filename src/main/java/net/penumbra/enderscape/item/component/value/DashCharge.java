package net.penumbra.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DashCharge(
        GlidingBasedValue duration,
        boolean depleteAfterCharging
) {

    public static final Codec<DashCharge> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    GlidingBasedValue.CODEC.fieldOf("duration").forGetter(DashCharge::duration),
                    Codec.BOOL.fieldOf("deplete_after_charging").forGetter(DashCharge::depleteAfterCharging)
            ).apply(instance, DashCharge::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DashCharge> STREAM_CODEC = StreamCodec.composite(
            GlidingBasedValue.STREAM_CODEC,
            DashCharge::duration,
            ByteBufCodecs.BOOL,
            DashCharge::depleteAfterCharging,
            DashCharge::new
    );
}