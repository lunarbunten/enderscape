package net.bunten.enderscape.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec2;

public final class EnderscapeCodecs {
    public static final Codec<Vec2> VEC2 = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("x").forGetter(vec -> vec.x),
                    Codec.FLOAT.fieldOf("y").forGetter(vec -> vec.y)
            ).apply(instance, Vec2::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Vec2> VEC2_STREAM = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            vec -> vec.x,
            ByteBufCodecs.FLOAT,
            vec -> vec.y,
            Vec2::new
    );

    public static void writeVec2(Vec2 vec, FriendlyByteBuf buf) {
        buf.writeFloat(vec.x);
        buf.writeFloat(vec.y);
    }

    public static Vec2 readVec2(FriendlyByteBuf buf) {
        return new Vec2(buf.readFloat(), buf.readFloat());
    }
}