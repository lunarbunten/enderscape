package net.penumbra.enderscape.entity.rustle;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum RustleConversionPhase {
    INACTIVE(0),
    BEGINNING(1),
    CONVERTING(2),
    ENDING(3);

    public static final IntFunction<RustleConversionPhase> BY_ID = ByIdMap.continuous(RustleConversionPhase::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, RustleConversionPhase> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, RustleConversionPhase::id);
    private final int id;

    RustleConversionPhase(final int j) {
        this.id = j;
    }

    public int id() {
        return this.id;
    }
}
