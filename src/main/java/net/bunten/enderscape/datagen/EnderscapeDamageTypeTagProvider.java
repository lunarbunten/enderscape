package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
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

    protected TagAppender<ResourceKey<DamageType>, DamageType> tag(TagKey<DamageType> key) {
        return TagAppender.forBuilder(getOrCreateRawBuilder(key));
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(RUBBLEMITES_CAN_BLOCK).add(ARROW, ENDER_PEARL, EXPLOSION, FALLING_ANVIL, FALLING_BLOCK, FALLING_STALACTITE, MACE_SMASH, MOB_ATTACK, MOB_ATTACK_NO_AGGRO, MOB_PROJECTILE, PLAYER_ATTACK, PLAYER_EXPLOSION, SPIT, STING, TRIDENT, UNATTRIBUTED_FIREBALL, WIND_CHARGE, WITHER_SKULL, STOMP);
        tag(DAMAGES_HELMET).add(STOMP);
        tag(NO_KNOCKBACK).add(STOMP);
    }
}