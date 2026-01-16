package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;

public class EnderscapeEnvironmentAttributes {

    public static final int DEFAULT_NEBULA_COLOR = 0x7B5B9E;
    public static final float DEFAULT_NEBULA_ALPHA = 0.05F;

    public static final int DEFAULT_STAR_COLOR = 0xE989FF;
    public static final float DEFAULT_STAR_ALPHA = 0.12F;

    public static final EnvironmentAttribute<Float> AMBIENT_LIGHT_FACTOR = register("visual/enderscape/ambient_light_factor", EnvironmentAttribute.builder(AttributeTypes.FLOAT).defaultValue(1.0F).spatiallyInterpolated().syncable());

    public static final EnvironmentAttribute<Integer> NEBULA_COLOR = register("visual/enderscape/nebula_color", EnvironmentAttribute.builder(AttributeTypes.RGB_COLOR).defaultValue(DEFAULT_NEBULA_COLOR).spatiallyInterpolated().syncable());
    public static final EnvironmentAttribute<Float> NEBULA_ALPHA = register("visual/enderscape/nebula_alpha", EnvironmentAttribute.builder(AttributeTypes.FLOAT).defaultValue(DEFAULT_NEBULA_ALPHA).spatiallyInterpolated().syncable());

    public static final EnvironmentAttribute<Integer> STAR_COLOR = register("visual/enderscape/star_color", EnvironmentAttribute.builder(AttributeTypes.RGB_COLOR).defaultValue(DEFAULT_STAR_COLOR).spatiallyInterpolated().syncable());
    public static final EnvironmentAttribute<Float> STAR_ALPHA = register("visual/enderscape/star_alpha", EnvironmentAttribute.builder(AttributeTypes.FLOAT).defaultValue(DEFAULT_STAR_ALPHA).spatiallyInterpolated().syncable());

    public static final EnvironmentAttribute<Float> FOG_END_DENSITY = register("visual/enderscape/fog_end_density", EnvironmentAttribute.builder(AttributeTypes.FLOAT).defaultValue(1.0F).spatiallyInterpolated().syncable());

    private static <Value> EnvironmentAttribute<Value> register(String name, EnvironmentAttribute.Builder<Value> builder) {
        EnvironmentAttribute<Value> attribute = builder.build();
        Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, Enderscape.id(name), attribute);
        return attribute;
    }
}