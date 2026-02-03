package net.bunten.enderscape.entity.ai;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.ai.sensing.NearestEnemiesSensor;
import net.bunten.enderscape.entity.ai.sensing.NearestIntimidatorSensor;
import net.bunten.enderscape.entity.ai.sensing.RustleNearestFoodSensor;
import net.bunten.enderscape.entity.ai.sensing.RustleNearestSleepingSpotSensor;
import net.bunten.enderscape.entity.drifter.DrifterAI;
import net.bunten.enderscape.entity.rustle.RustleAI;
import net.bunten.enderscape.registry.RegistryHelper;
import net.bunten.enderscape.registry.tag.EnderscapeEntityTags;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.DummySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Supplier;

import static net.bunten.enderscape.Enderscape.MOD_ID;

public class EnderscapeSensors extends SensorType<DummySensor> {

    public EnderscapeSensors() {
        super(DummySensor::new);
    }

    // Drifter

    public static final Supplier<SensorType<NearestIntimidatorSensor>> NEAREST_INTIMIDATOR = register("nearest_intimidator", NearestIntimidatorSensor::new);
    public static final Supplier<SensorType<TemptingSensor>> DRIFTER_TEMPTATIONS = register("drifter_temptations", () -> new TemptingSensor(DrifterAI.getTemptations()));

    // Rubblemite

    public static final Supplier<SensorType<NearestEnemiesSensor>> RUBBLEMITE_NEAREST_ENEMIES = register("rubblemite_nearest_enemies", () -> new NearestEnemiesSensor(EnderscapeEntityTags.RUBBLEMITE_HOSTILE_TOWARDS));

    // Rustle

    public static final Supplier<SensorType<RustleNearestFoodSensor>> RUSTLE_NEAREST_FOOD = register("rustle_nearest_food", RustleNearestFoodSensor::new);
    public static final Supplier<SensorType<RustleNearestSleepingSpotSensor>> RUSTLE_NEAREST_SLEEPING_SPOT = register("rustle_nearest_sleeping_spot", RustleNearestSleepingSpotSensor::new);
    public static final Supplier<SensorType<TemptingSensor>> RUSTLE_TEMPTATIONS = register("rustle_temptations", () -> new TemptingSensor(RustleAI.getTemptations()));

    protected static <U extends Sensor<?>> Supplier<SensorType<U>> register(String string, Supplier<U> supplier) {
        return RegistryHelper.register(BuiltInRegistries.SENSOR_TYPE, Enderscape.id(string), () -> new SensorType<>(supplier));
    }
}