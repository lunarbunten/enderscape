package net.bunten.enderscape.registry;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;

public class EnderscapeGameEvents {

    public static final Holder.Reference<GameEvent> BOUNCE = register("bounce", 16);
    public static final Holder.Reference<GameEvent> DASH_JUMP = register("dash_jump", 16);

    private static Holder.Reference<GameEvent> register(String name, int i) {
        return Registry.registerForHolder(BuiltInRegistries.GAME_EVENT, Enderscape.id(name), new GameEvent(i));
    }

    static {
        put(BOUNCE.key(), 2);
        put(DASH_JUMP.key(), 6);
    }

    private static void put(ResourceKey<GameEvent> key, int frequency) {
        ((Reference2IntOpenHashMap<ResourceKey<GameEvent>>) VibrationSystem.VIBRATION_FREQUENCY_FOR_EVENT).put(key, frequency);
    }
}