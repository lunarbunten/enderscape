package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeDamageTypes {

    public static final List<ResourceKey<DamageType>> DAMAGE_TYPES = new ArrayList<>();

    public static final ResourceKey<DamageType> STOMP = register("stomp");

    public static void bootstrap(BootstrapContext<DamageType> context) {
        register(context, STOMP);
    }

    private static void register(BootstrapContext<DamageType> context, ResourceKey<DamageType> key) {
        context.register(key, new DamageType(key.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
    }

    private static ResourceKey<DamageType> register(String name) {
        ResourceKey<DamageType> key = ResourceKey.create(Registries.DAMAGE_TYPE, Enderscape.id(name));
        DAMAGE_TYPES.add(key);
        return key;
    }
}