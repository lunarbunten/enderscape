package net.penumbra.enderscape.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.tags.DamageTypeTags.*;
import static net.minecraft.world.damagesource.DamageTypes.*;
import static net.penumbra.enderscape.registry.entity.EnderscapeDamageTypes.*;
import static net.penumbra.enderscape.registry.tag.EnderscapeDamageTypeTags.*;

public class EnderscapeDamageTypeTagProvider extends FabricTagsProvider<DamageType> {

    public EnderscapeDamageTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.DAMAGE_TYPE, future);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(RUBBLEMITES_CAN_BLOCK).add(
                ARROW,
                ENDER_PEARL,
                EXPLOSION,
                FALLING_ANVIL,
                FALLING_BLOCK,
                FALLING_STALACTITE,
                MACE_SMASH,
                MOB_ATTACK,
                MOB_ATTACK_NO_AGGRO,
                MOB_PROJECTILE,
                PLAYER_ATTACK,
                PLAYER_EXPLOSION,
                SPIT,
                STING,
                STOMP,
                TRIDENT,
                UNATTRIBUTED_FIREBALL,
                WIND_CHARGE,
                WITHER_SKULL
        );

        tag(IS_VOID).add(
                IN_VOID_FIRE,
                OUTER_VOID,
                VOID,
                VOID_ATTACK,
                VOID_CAMPFIRE,
                VOID_LACHRYMA
        );

        tag(NO_KNOCKBACK).add(
                IN_VOID_FIRE,
                OUTER_VOID,
                STOMP,
                VOID,
                VOID_CAMPFIRE,
                VOID_LACHRYMA,
                VOID_PURIFICATION
        );

        tag(ALWAYS_MOST_SIGNIFICANT_FALL).add(OUTER_VOID);
        tag(BYPASSES_ARMOR).forceAddTag(IS_VOID).add(VOID_PURIFICATION);
        tag(BYPASSES_COOLDOWN).add(OUTER_VOID);
        tag(BYPASSES_EFFECTS).forceAddTag(IS_VOID);
        tag(BYPASSES_ENCHANTMENTS).forceAddTag(IS_VOID);
        tag(BYPASSES_INVULNERABILITY).add(OUTER_VOID);
        tag(BYPASSES_RESISTANCE).forceAddTag(IS_VOID);
        tag(BYPASSES_SHIELD).add(OUTER_VOID);
        tag(BYPASSES_WOLF_ARMOR).forceAddTag(IS_VOID).add(VOID_PURIFICATION);
        tag(CAN_BREAK_ARMOR_STAND).add(STUN_ATTACK);
        tag(DAMAGES_HELMET).add(STOMP);
        tag(IS_PLAYER_ATTACK).add(STUN_ATTACK);
        tag(PANIC_CAUSES).add(VOID_ATTACK);
        tag(PANIC_ENVIRONMENTAL_CAUSES).add(IN_VOID_FIRE);
        tag(VOIDS_HEALTH).addTag(IS_VOID);
    }
}