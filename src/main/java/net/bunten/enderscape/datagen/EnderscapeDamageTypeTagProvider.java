package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeDamageTypes.STOMP;
import static net.bunten.enderscape.registry.tag.EnderscapeDamageTypeTags.RUBBLEMITES_CAN_BLOCK;
import static net.minecraft.tags.DamageTypeTags.DAMAGES_HELMET;
import static net.minecraft.tags.DamageTypeTags.NO_KNOCKBACK;
import static net.minecraft.world.damagesource.DamageTypes.*;

public class EnderscapeDamageTypeTagProvider extends FabricTagProvider<DamageType> {

    public EnderscapeDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.DAMAGE_TYPE, future);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(RUBBLEMITES_CAN_BLOCK).add(ARROW, EXPLOSION, FALLING_ANVIL, FALLING_BLOCK, FALLING_STALACTITE, MOB_ATTACK, MOB_ATTACK_NO_AGGRO, MOB_PROJECTILE, PLAYER_ATTACK, PLAYER_EXPLOSION, SPIT, STING, TRIDENT, UNATTRIBUTED_FIREBALL, WIND_CHARGE, WITHER_SKULL, STOMP);
        getOrCreateTagBuilder(DAMAGES_HELMET).add(STOMP);
        getOrCreateTagBuilder(NO_KNOCKBACK).add(STOMP);
    }
}