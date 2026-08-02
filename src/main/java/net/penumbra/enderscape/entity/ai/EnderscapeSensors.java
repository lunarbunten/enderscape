package net.penumbra.enderscape.entity.ai;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.DummySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.entity.ai.sensing.NearestAttackableSensor;
import net.penumbra.enderscape.entity.ai.sensing.drifter.DrifterNearestIntimidatorSensor;
import net.penumbra.enderscape.entity.ai.sensing.rustle.RustleNearestSleepingSpotSensor;
import net.penumbra.enderscape.entity.drifter.DrifterAI;
import net.penumbra.enderscape.entity.rustle.RustleAI;
import net.penumbra.enderscape.registry.tag.EnderscapeEntityTags;

import java.util.function.Supplier;

public class EnderscapeSensors extends SensorType<DummySensor> {

    public EnderscapeSensors() {
        super(DummySensor::new);
    }

    // Drifter

    public static final SensorType<DrifterNearestIntimidatorSensor> NEAREST_INTIMIDATOR = register("nearest_intimidator", DrifterNearestIntimidatorSensor::new);
    public static final SensorType<TemptingSensor> DRIFTER_TEMPTATIONS = register("drifter_temptations", () -> new TemptingSensor(DrifterAI.getTemptations()));

    // Rubblemite

    public static final SensorType<NearestAttackableSensor> RUBBLEMITE_NEAREST_ATTACKABLE = register("rubblemite_nearest_attackable", () -> new NearestAttackableSensor(EnderscapeEntityTags.RUBBLEMITE_ALWAYS_HOSTILES));

    // Rustle

    public static final SensorType<RustleNearestSleepingSpotSensor> RUSTLE_NEAREST_SLEEPING_SPOT = register("rustle_nearest_sleeping_spot", RustleNearestSleepingSpotSensor::new);
    public static final SensorType<TemptingSensor> RUSTLE_TEMPTATIONS = register("rustle_temptations", () -> new TemptingSensor(RustleAI.getTemptations()));

    protected static <U extends Sensor<?>> SensorType<U> register(String string, Supplier<U> supplier) {
        return Registry.register(BuiltInRegistries.SENSOR_TYPE, Enderscape.id(string), new SensorType<>(supplier));
    }
}