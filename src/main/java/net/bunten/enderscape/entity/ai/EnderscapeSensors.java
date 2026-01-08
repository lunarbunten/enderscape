package net.bunten.enderscape.entity.ai;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.ai.sensing.NearestEnemiesSensor;
import net.bunten.enderscape.entity.ai.sensing.NearestIntimidatorSensor;
import net.bunten.enderscape.entity.ai.sensing.RustleNearestFoodSensor;
import net.bunten.enderscape.entity.ai.sensing.RustleNearestSleepingSpotSensor;
import net.bunten.enderscape.entity.drifter.DrifterAI;
import net.bunten.enderscape.entity.rustle.RustleAI;
import net.bunten.enderscape.registry.tag.EnderscapeEntityTags;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.DummySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;

import java.util.function.Supplier;

public class EnderscapeSensors extends SensorType<DummySensor> {

    public EnderscapeSensors() {
        super(DummySensor::new);
    }

    // Drifter

    public static final SensorType<NearestIntimidatorSensor> NEAREST_INTIMIDATOR = register("nearest_intimidator", NearestIntimidatorSensor::new);
    public static final SensorType<TemptingSensor> DRIFTER_TEMPTATIONS = register("drifter_temptations", () -> new TemptingSensor(DrifterAI.getTemptations()));

    // Rubblemite

    public static final SensorType<NearestEnemiesSensor> RUBBLEMITE_NEAREST_ENEMIES = register("rubblemite_nearest_enemies", () -> new NearestEnemiesSensor(EnderscapeEntityTags.RUBBLEMITE_HOSTILE_TOWARDS));

    // Rustle

    public static final SensorType<RustleNearestFoodSensor> RUSTLE_NEAREST_FOOD = register("rustle_nearest_food", RustleNearestFoodSensor::new);
    public static final SensorType<RustleNearestSleepingSpotSensor> RUSTLE_NEAREST_SLEEPING_SPOT = register("rustle_nearest_sleeping_spot", RustleNearestSleepingSpotSensor::new);
    public static final SensorType<TemptingSensor> RUSTLE_TEMPTATIONS = register("rustle_temptations", () -> new TemptingSensor(RustleAI.getTemptations()));

    protected static <U extends Sensor<?>> SensorType<U> register(String string, Supplier<U> supplier) {
        return Registry.register(BuiltInRegistries.SENSOR_TYPE, Enderscape.id(string), new SensorType<>(supplier));
    }
}