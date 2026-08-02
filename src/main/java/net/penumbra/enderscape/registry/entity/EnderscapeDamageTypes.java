package net.penumbra.enderscape.registry.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.penumbra.enderscape.Enderscape;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeDamageTypes {

    public static final List<ResourceKey<DamageType>> DAMAGE_TYPES = new ArrayList<>();

    public static final ResourceKey<DamageType> IN_VOID_FIRE = register("in_void_fire");
    public static final ResourceKey<DamageType> OUTER_VOID = register("outer_void");
    public static final ResourceKey<DamageType> STOMP = register("stomp");
    public static final ResourceKey<DamageType> STUN_ATTACK = register("stun_attack");
    public static final ResourceKey<DamageType> VOID = register("void");
    public static final ResourceKey<DamageType> VOID_ATTACK = register("void_attack");
    public static final ResourceKey<DamageType> VOID_CAMPFIRE = register("void_campfire");
    public static final ResourceKey<DamageType> VOID_LACHRYMA = register("void_lachryma");
    public static final ResourceKey<DamageType> VOID_PURIFICATION = register("void_purification");

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(IN_VOID_FIRE, new DamageType("enderscape_in_void_fire", 0.1F));
        context.register(OUTER_VOID, new DamageType("enderscape_void", 0.0F));
        context.register(STOMP, new DamageType("enderscape_stomp", 0.1F));
        context.register(STUN_ATTACK, new DamageType("player", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(VOID, new DamageType("enderscape_void", 0.0F));
        context.register(VOID_ATTACK, new DamageType("enderscape_void_attack", 0.1F));
        context.register(VOID_CAMPFIRE, new DamageType("enderscape_in_void_fire", 0.0F));
        context.register(VOID_LACHRYMA, new DamageType("enderscape_void_lachryma", 0.0F));
        context.register(VOID_PURIFICATION, new DamageType("enderscape_void_purification", 0.0F));
    }

    private static ResourceKey<DamageType> register(String name) {
        ResourceKey<DamageType> key = ResourceKey.create(Registries.DAMAGE_TYPE, Enderscape.id(name));
        DAMAGE_TYPES.add(key);
        return key;
    }
}