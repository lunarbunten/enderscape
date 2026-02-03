package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class EnderscapeEntityDataSerializers {
    public static final Supplier<EntityDataSerializer<Rubblemite.State>> RUBBLEMITE_STATE_SERIALIZER = register("rubblemite_state",
            () -> EntityDataSerializer.forValueType(Rubblemite.State.STREAM_CODEC));

    private static <T> Supplier<EntityDataSerializer<T>> register(String name, Supplier<EntityDataSerializer<T>> entry) {
        return RegistryHelper.register(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Enderscape.id(name), entry);
    }
}