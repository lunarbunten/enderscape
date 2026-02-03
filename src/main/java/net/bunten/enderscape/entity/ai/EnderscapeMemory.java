package net.bunten.enderscape.entity.ai;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.RegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class EnderscapeMemory extends MemoryModuleType<Object> {

    public EnderscapeMemory() {
        super(Optional.empty());
    }


    public static final Supplier<MemoryModuleType<List<LivingEntity>>> NEAREST_ENEMIES = register("rubblemite_nearest_enemies");
    public static final Supplier<MemoryModuleType<LivingEntity>> NEAREST_INTIMIDATOR = register("drifter_nearest_intimidator");
    public static final Supplier<MemoryModuleType<LivingEntity>> NEAREST_VISIBLE_ATTACKABLE_ENEMY = register("rubblemite_nearest_visible_targetable_enemy");
    public static final Supplier<MemoryModuleType<LivingEntity>> NEAREST_VISIBLE_ENEMY = register("rubblemite_nearest_visible_enemy");

    // Drifter

    public static final Supplier<MemoryModuleType<Integer>> DRIFTER_FIND_HOME_COOLDOWN = register("drifter_find_home_cooldown", Codec.INT);
    public static final Supplier<MemoryModuleType<Integer>> DRIFTER_JELLY_CHANGE_COOLDOWN = register("drifter_jelly_change_cooldown", Codec.INT);

    // Rubblemite

    public static final Supplier<MemoryModuleType<Boolean>> RUBBLEMITE_PREPARING_DASH = register("rubblemite_preparing_dash", Codec.BOOL);
    public static final Supplier<MemoryModuleType<Integer>> RUBBLEMITE_PREPARING_DASH_TIME = register("rubblemite_preparing_dash_time", Codec.INT);

    public static final Supplier<MemoryModuleType<Boolean>> RUBBLEMITE_DASH_ON_COOLDOWN = register("rubblemite_dash_on_cooldown", Codec.BOOL);
    public static final Supplier<MemoryModuleType<Boolean>> RUBBLEMITE_HIDING_ON_COOLDOWN = register("rubblemite_hiding_on_cooldown", Codec.BOOL);
    public static final Supplier<MemoryModuleType<Integer>> RUBBLEMITE_HIDING_DURATION = register("rubblemite_hiding_duration", Codec.INT);

    // Rustle

    public static final Supplier<MemoryModuleType<BlockPos>> RUSTLE_FOOD = register("rustle_food");
    public static final Supplier<MemoryModuleType<BlockPos>> RUSTLE_SLEEPING_SPOT = register("rustle_sleeping_spot");
    public static final Supplier<MemoryModuleType<Boolean>> RUSTLE_SLEEPING_ON_COOLDOWN = register("rustle_sleeping_on_cooldown", Codec.BOOL);
    public static final Supplier<MemoryModuleType<Integer>> RUSTLE_HAIR_REGROWTH_COOLDOWN = register("rustle_hair_regrowth_cooldown", Codec.INT);
    public static final Supplier<MemoryModuleType<Integer>> RUSTLE_SLEEP_TICKS = register("rustle_sleep_ticks", Codec.INT);

    public static LivingEntity getAttackTarget(LivingEntity mob) {
        return mob.getBrain().getMemory(ATTACK_TARGET).get();
    }

    protected static <U> Supplier<MemoryModuleType<U>> register(String string, Codec<U> codec) {
        return RegistryHelper.register(BuiltInRegistries.MEMORY_MODULE_TYPE, Enderscape.id(string), () -> new MemoryModuleType<U>(Optional.of(codec)));
    }

    protected static <U> Supplier<MemoryModuleType<U>> register(String string) {
        return RegistryHelper.register(BuiltInRegistries.MEMORY_MODULE_TYPE, Enderscape.id(string), () -> new MemoryModuleType<U>(Optional.empty()));
    }
}