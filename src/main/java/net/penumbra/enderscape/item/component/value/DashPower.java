package net.penumbra.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec2;
import net.penumbra.enderscape.network.EnderscapeCodecs;

public record DashPower(
        Vec2 base,
        float glidingMultiplier
) {

    public static final Codec<DashPower> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Vec2.CODEC.fieldOf("base").forGetter(DashPower::base),
                    ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("gliding_multiplier", 1.0F).forGetter(DashPower::glidingMultiplier)
            ).apply(instance, DashPower::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DashPower> STREAM_CODEC = StreamCodec.composite(
            EnderscapeCodecs.VEC2_STREAM,
            DashPower::base,
            ByteBufCodecs.FLOAT,
            DashPower::glidingMultiplier,
            DashPower::new
    );

    public Vec2 calculate(ServerPlayer player) {
        Vec2 value = base();
        if (player.isFallFlying()) value = value.scale(glidingMultiplier());
        return value;
    }
}