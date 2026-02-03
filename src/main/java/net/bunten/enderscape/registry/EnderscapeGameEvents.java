package net.bunten.enderscape.registry;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class EnderscapeGameEvents {

    public static final DeferredHolder<GameEvent, GameEvent> BOUNCE = register("bounce", () -> new GameEvent(16));
    public static final DeferredHolder<GameEvent, GameEvent> DASH_JUMP = register("dash_jump", () -> new GameEvent(16));

    private static DeferredHolder<GameEvent, GameEvent> register(String name, Supplier<GameEvent> gameevent) {
        return RegistryHelper.registerForHolder(BuiltInRegistries.GAME_EVENT, Enderscape.id(name), gameevent);
    }

    static {
        put(BOUNCE.getKey(), 2);
        put(DASH_JUMP.getKey(), 6);
    }

    private static void put(ResourceKey<GameEvent> key, int frequency) {
        ((Reference2IntOpenHashMap<ResourceKey<GameEvent>>) VibrationSystem.VIBRATION_FREQUENCY_FOR_EVENT).put(key, frequency);
    }
}