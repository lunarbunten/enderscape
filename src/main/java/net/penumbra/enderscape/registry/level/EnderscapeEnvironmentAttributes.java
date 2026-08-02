package net.penumbra.enderscape.registry.level;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.AttributeRange;
import net.minecraft.world.attribute.AttributeType;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.entity.MobCategory;
import net.penumbra.enderscape.Enderscape;

import java.util.function.Function;

public class EnderscapeEnvironmentAttributes {

    public static final int DEFAULT_NEBULA_COLOR = 0x7B5B9E;
    public static final float DEFAULT_NEBULA_ALPHA = 0.05F;

    public static final int DEFAULT_STAR_COLOR = 0xE989FF;
    public static final float DEFAULT_STAR_BRIGHTNESS = 0.12F;

    public static final EnvironmentAttribute<Float> AMBIENT_ENTITY_VOIDING_RATE = gameplay(
            "ambient_entity_voiding_rate",
            AttributeTypes.FLOAT,
            0.0F,
            builder -> builder.valueRange(AttributeRange.ofFloat(0.0F, 20.0F))
    );

    public static final EnvironmentAttribute<Float> AMBIENT_VOID_DAMAGE_INTERVAL = gameplay(
            "ambient_void_damage_interval",
            AttributeTypes.FLOAT,
            5.0F,
            builder -> builder.valueRange(AttributeRange.NON_NEGATIVE_FLOAT)
    );

    public static final EnvironmentAttribute<Boolean> END_HAVEN_CORE_WORKS = gameplay(
            "end_haven_core_works",
            AttributeTypes.BOOLEAN,
            false,
            builder -> builder.notPositional().syncable()
    );

    public static final EnvironmentAttribute<Integer> MONSTER_SPAWN_CAP = gameplay(
            "monster_spawn_cap",
            AttributeTypes.INTEGER,
            MobCategory.MONSTER.getMaxInstancesPerChunk(),
            EnvironmentAttribute.Builder::notPositional
    );

    public static final EnvironmentAttribute<Boolean> PURIFIES_VOIDED_HEALTH = gameplay("purifies_voided_health", AttributeTypes.BOOLEAN, false);

    public static final EnvironmentAttribute<Integer> NEBULA_COLOR = interpolatedVisual("nebula_color", AttributeTypes.RGB_COLOR, DEFAULT_NEBULA_COLOR);
    public static final EnvironmentAttribute<Float> NEBULA_BRIGHTNESS = interpolatedVisual("nebula_brightness", AttributeTypes.FLOAT, DEFAULT_NEBULA_ALPHA);
    public static final EnvironmentAttribute<Integer> STAR_COLOR = interpolatedVisual("star_color", AttributeTypes.RGB_COLOR, DEFAULT_STAR_COLOR);
    public static final EnvironmentAttribute<Float> FOG_END_DENSITY = interpolatedVisual("fog_end_density", AttributeTypes.FLOAT, 1.0F);

    @SafeVarargs
    private static <Value> EnvironmentAttribute<Value> gameplay(String name, AttributeType<Value> type, Value defaultValue, Function<EnvironmentAttribute.Builder<Value>, EnvironmentAttribute.Builder<Value>>... modifiers) {
        return register("gameplay/enderscape/" + name, type, defaultValue, modifiers);
    }

    private static <Value> EnvironmentAttribute<Value> interpolatedVisual(String name, AttributeType<Value> type, Value defaultValue) {
        return register("visual/enderscape/" + name, type, defaultValue, builder -> builder.spatiallyInterpolated().syncable());
    }

    @SafeVarargs
    private static <Value> EnvironmentAttribute<Value> register(String name, AttributeType<Value> type, Value defaultValue, Function<EnvironmentAttribute.Builder<Value>, EnvironmentAttribute.Builder<Value>>... modifiers) {
        EnvironmentAttribute.Builder<Value> builder = EnvironmentAttribute.builder(type).defaultValue(defaultValue);
        for (var modifier : modifiers) {
            builder = modifier.apply(builder);
        }
        EnvironmentAttribute<Value> attribute = builder.build();
        Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, Enderscape.id(name), attribute);
        return attribute;
    }
}