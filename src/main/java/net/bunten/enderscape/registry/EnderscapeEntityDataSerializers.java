package net.bunten.enderscape.registry;

import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class EnderscapeEntityDataSerializers {
    public static final EntityDataSerializer<Rubblemite.State> RUBBLEMITE_STATE_SERIALIZER = EntityDataSerializer.forValueType(Rubblemite.State.STREAM_CODEC);

    static {
        EntityDataSerializers.registerSerializer(RUBBLEMITE_STATE_SERIALIZER);
    }
}