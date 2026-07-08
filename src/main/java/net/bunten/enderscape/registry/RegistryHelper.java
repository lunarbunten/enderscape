package net.bunten.enderscape.registry;

import com.mojang.datafixers.util.Pair;
import net.bunten.enderscape.Enderscape;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Mod(Enderscape.MOD_ID)
public final class RegistryHelper {
    private static final Map<Pair<String, ResourceKey<? extends Registry<?>>>, DeferredRegister<?>> REGISTERS = new ConcurrentHashMap<>();
    
    private static IEventBus MOD_BUS;
    
    @SuppressWarnings("unchecked")
    private static  <V> DeferredRegister<V> getRegister(Registry<V> registry, ResourceLocation key) {
        var registerKey = new Pair<String, ResourceKey<? extends Registry<?>>>(key.getNamespace(), registry.key());
        return (DeferredRegister<V>) REGISTERS.computeIfAbsent(registerKey, RegistryHelper::makeRegister);
    }

    @SuppressWarnings("unchecked")
    private synchronized static <T> DeferredRegister<T> makeRegister(Pair<String, ResourceKey<? extends Registry<?>>> key) {
        var register = DeferredRegister.create((ResourceKey<? extends Registry<T>>) key.getSecond(), key.getFirst());
        if (MOD_BUS != null) {
            register.register(MOD_BUS);
        }
        return register;
    }

    public static <V, T extends V> Supplier<T> register(Registry<V> registry, ResourceKey<V> key, Supplier<T> value) {
        return getRegister(registry, key.location()).register(key.location().getPath(), value);
    }

    public static <V, T extends V> Supplier<T> register(Registry<V> registry, ResourceLocation location, Supplier<T> value) {
        return getRegister(registry, location).register(location.getPath(), value);
    }

    public static <V, T extends V> DeferredHolder<V, T> registerForHolder(Registry<V> registry, ResourceKey<V> key, Supplier<T> value) {
        return getRegister(registry, key.location()).register(key.location().getPath(), value);
    }

    public static <V, T extends V> DeferredHolder<V, T> registerForHolder(Registry<V> registry, ResourceLocation location, Supplier<T> value) {
        return getRegister(registry, location).register(location.getPath(), value);
    }


    
    private static boolean ARE_ALL_READY = false;
    
    public RegistryHelper(IEventBus modBus) {
        setup(modBus);
        Set<ResourceKey<? extends Registry<?>>> keys = new HashSet<>();
        modBus.addListener(RegisterEvent.class, event -> {
            keys.add(event.getRegistry().key());
            if (keys.size() >= BuiltInRegistries.REGISTRY.keySet().size()) {
                ARE_ALL_READY = true;
            }
        });
    }
    
    private synchronized static void setup(IEventBus modBus) {
        REGISTERS.values().forEach(r -> r.register(modBus));
        MOD_BUS = modBus;
    }

    public static void checkAllReady() {
        if (!ARE_ALL_READY) {
            throw new IllegalStateException("Initialization before registry events are fired. Something is initializing registry-sensitive code too early.");
        }
    }
}
