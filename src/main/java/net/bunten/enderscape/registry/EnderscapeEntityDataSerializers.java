package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.rubblemite.RubblemiteVariant;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataSerializer;

public class EnderscapeEntityDataSerializers {
    public static final EntityDataSerializer<Holder<RubblemiteVariant>> RUBBLEMITE_VARIANT_SERIALIZER = EntityDataSerializer.forValueType(RubblemiteVariant.STREAM_CODEC);

    static {
        FabricEntityDataRegistry.register(Enderscape.id("rubblemite_variant"), RUBBLEMITE_VARIANT_SERIALIZER);
    }
}