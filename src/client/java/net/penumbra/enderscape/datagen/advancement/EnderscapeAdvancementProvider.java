package net.penumbra.enderscape.datagen.advancement;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.*;
import net.minecraft.advancements.predicates.*;
import net.minecraft.advancements.predicates.entity.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.block.state.EndHavenCoreState;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.criteria.*;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import net.penumbra.enderscape.registry.entity.EnderscapeEntities;
import net.penumbra.enderscape.registry.level.EnderscapeBiomes;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.minecraft.advancements.triggers.CriteriaTriggers.FALL_FROM_HEIGHT;
import static net.minecraft.world.item.Items.BUCKET;
import static net.minecraft.world.item.Items.FIREWORK_ROCKET;
import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;
import static net.penumbra.enderscape.registry.item.EnderscapeItems.*;
import static net.penumbra.enderscape.registry.server.EnderscapeCriteria.*;

public class EnderscapeAdvancementProvider extends FabricAdvancementProvider {

    public static final List<ResourceKey<Biome>> EXPLORE_END_BIOMES = List.of(
            Biomes.THE_END, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS,
            EnderscapeBiomes.VEILED_WOODLANDS, EnderscapeBiomes.MAGNIA_FIELDS,
            EnderscapeBiomes.CELESTIAL_GROVE, EnderscapeBiomes.CORRUPT_BARRENS,
            EnderscapeBiomes.VOID_DEPTHS, EnderscapeBiomes.VOID_SKIES, EnderscapeBiomes.VOID_SKY_ISLANDS
    );

    public static final List<ResourceKey<Level>> ALL_DIMENSION_TYPES = List.of(Level.OVERWORLD, Level.NETHER, Level.END);

    public EnderscapeAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    @SuppressWarnings({"removal", "unused"})
    public void generateAdvancement(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) {

        HolderGetter<Item> items = provider.lookupOrThrow(Registries.ITEM);
        HolderGetter<Block> blocks = provider.lookupOrThrow(Registries.BLOCK);
        HolderLookup.RegistryLookup<Biome> biomes = provider.lookupOrThrow(Registries.BIOME);
        HolderLookup.RegistryLookup<EntityType<?>> entities = provider.lookupOrThrow(Registries.ENTITY_TYPE);

        ResourceKey<Advancement> endGatewayKey = ResourceKey.create(Registries.ADVANCEMENT, Identifier.withDefaultNamespace("end/enter_end_gateway"));
        ResourceKey<Advancement> findEndCityKey = ResourceKey.create(Registries.ADVANCEMENT, Identifier.withDefaultNamespace("end/find_end_city"));
        ResourceKey<Advancement> elytraKey = ResourceKey.create(Registries.ADVANCEMENT, Identifier.withDefaultNamespace("end/elytra"));

        Advancement.Builder.advancement()
                .parent(endGatewayKey.identifier())
                .display(
                        CELESTIAL_CAP,
                        Component.translatable("advancement.enderscape.fall_onto_chanterelle_cap"),
                        Component.translatable("advancement.enderscape.fall_onto_chanterelle_cap.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion(
                        "fall_onto_cap",
                        FALL_FROM_HEIGHT.createCriterion(new DistanceTrigger.TriggerInstance(
                                Optional.of(ContextAwarePredicate.create(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder
                                                        .entity()
                                                        .steppingOn(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, EnderscapeBlockTags.CHANTERELLE_CAP_BLOCKS)))
                                                        .flags(EntityFlagsPredicate.Builder.flags().setIsFlying(false).setSwimming(false).setOnGround(true))
                                        ).build()
                                )),
                                Optional.empty(),
                                Optional.of(DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(50))))
                        )
                )
                .save(consumer, Enderscape.id("fall_onto_chanterelle_cap").toString());

        AdvancementHolder rubbleShieldDash = Advancement.Builder.advancement()
                .parent(endGatewayKey.identifier())
                .display(
                        RUBBLE_SHIELD,
                        Component.translatable("advancement.enderscape.rubble_shield_dash"),
                        Component.translatable("advancement.enderscape.rubble_shield_dash.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion(
                        "rubble_shield_dash",
                        DASH_JUMP.createCriterion(new DashJumpCriterion.Conditions(
                                Optional.empty(),
                                Optional.of(ItemPredicate.Builder.item().of(items, RUBBLE_SHIELD).build()),
                                Optional.empty()
                        ))
                )
                .save(consumer, Enderscape.id("rubble_shield_dash").toString());

        AdvancementHolder rustleBucket = Advancement.Builder.advancement()
                .parent(endGatewayKey.identifier())
                .display(
                        RUSTLE_BUCKET,
                        Component.translatable("advancement.enderscape.rustle_bucket"),
                        Component.translatable("advancement.enderscape.rustle_bucket.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion(
                        BuiltInRegistries.ITEM.getKey(RUSTLE_BUCKET).getPath(),
                        PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                                Optional.empty(),
                                ItemPredicate.Builder.item().of(items, BUCKET),
                                Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(entities, EnderscapeEntities.RUSTLE)))
                        )
                )
                .save(consumer, Enderscape.id("rustle_bucket").toString());

        AdvancementHolder bliss = Advancement.Builder.advancement()
                .parent(rustleBucket)
                .display(
                        MUSIC_DISC_BLISS,
                        Component.translatable("advancement.enderscape.music_disc_bliss"),
                        Component.translatable("advancement.enderscape.music_disc_bliss.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )
                .addCriterion("obtained", InventoryChangeTrigger.TriggerInstance.hasItems(MUSIC_DISC_BLISS))
                .save(consumer, Enderscape.id("music_disc_bliss").toString());

        AdvancementHolder unlockEndVault = Advancement.Builder.advancement()
                .parent(findEndCityKey.identifier())
                .display(
                        END_CITY_KEY,
                        Component.translatable("advancement.enderscape.unlock_end_vault"),
                        Component.translatable("advancement.enderscape.unlock_end_vault.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, true
                )
                .addCriterion(
                        "unlock_end_vault",
                        ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                                LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, END_VAULT)),
                                ItemPredicate.Builder.item().of(items, END_CITY_KEY)
                        )
                )
                .save(consumer, Enderscape.id("unlock_end_vault").toString());

        Advancement.Builder exploreEnd = Advancement.Builder.advancement()
                .parent(endGatewayKey.identifier())
                .display(SHADOLINE_BOOTS,
                        Component.translatable("advancement.enderscape.explore_end"),
                        Component.translatable("advancement.enderscape.explore_end.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false)
                .rewards(AdvancementRewards.Builder.experience(1000));

        EXPLORE_END_BIOMES.forEach(biome ->
                exploreEnd.addCriterion(
                        biome.identifier().toString(),
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome(biomes.getOrThrow(biome)))
                )
        );

        exploreEnd.save(consumer, Enderscape.id("explore_end").toString());

        AdvancementHolder obtainNebulite = Advancement.Builder.advancement()
                .parent(endGatewayKey.identifier())
                .display(
                        NEBULITE,
                        Component.translatable("advancement.enderscape.obtain_nebulite"),
                        Component.translatable("advancement.enderscape.obtain_nebulite.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("obtained", RecipeCraftedTrigger.TriggerInstance.craftedItem(ResourceKey.create(Registries.RECIPE, Enderscape.id("nebulite_from_shards"))))
                .save(consumer, Enderscape.id("craft_nebulite").toString());

        AdvancementHolder bottleDriftJelly = Advancement.Builder.advancement()
                .parent(endGatewayKey.identifier())
                .display(
                        DRIFT_JELLY_BOTTLE,
                        Component.translatable("advancement.enderscape.bottle_drift_jelly"),
                        Component.translatable("advancement.enderscape.bottle_drift_jelly.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("obtained", InventoryChangeTrigger.TriggerInstance.hasItems(DRIFT_JELLY_BOTTLE))
                .save(consumer, Enderscape.id("bottle_drift_jelly").toString());

        AdvancementHolder driftLeggings = Advancement.Builder.advancement()
                .parent(bottleDriftJelly)
                .display(
                        DRIFT_LEGGINGS,
                        Component.translatable("advancement.enderscape.drift_leggings"),
                        Component.translatable("advancement.enderscape.drift_leggings.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("obtained", InventoryChangeTrigger.TriggerInstance.hasItems(DRIFT_LEGGINGS))
                .save(consumer, Enderscape.id("drift_leggings").toString());

        AdvancementHolder glideOntoDrifter = Advancement.Builder.advancement()
                .parent(driftLeggings)
                .display(
                        new ItemStackTemplate(FIREWORK_ROCKET),
                        Component.translatable("advancement.enderscape.glide_onto_drifter"),
                        Component.translatable("advancement.enderscape.glide_onto_drifter.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("bounced", BOUNCE_ON_DRIFTER.createCriterion(new BounceOnDrifterCriterion.Conditions(
                        Optional.of(
                                ContextAwarePredicate.create(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder
                                                        .entity()
                                                        .moving(MovementPredicate.speed(MinMaxBounds.Doubles.atLeast(40)))
                                                        .flags(EntityFlagsPredicate.Builder.flags().setIsFlying(true))
                                        ).build()
                                )
                        ),
                        Optional.empty()
                )))
                .save(consumer, Enderscape.id("glide_onto_drifter").toString());

        Optional<ItemPredicate> mirrorItemPredicate = Optional.of(ItemPredicate.Builder.item().of(items, MIRROR).build());

        AdvancementHolder mirrorTeleport = Advancement.Builder.advancement()
                .parent(obtainNebulite)
                .display(
                        mirrorTemplate(null),
                        Component.translatable("advancement.enderscape.mirror_teleport"),
                        Component.translatable("advancement.enderscape.mirror_teleport.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("teleported", LODESTONE_TELEPORTATION.createCriterion(new LodestoneTeleportationCriterion.Conditions(
                        Optional.empty(),
                        mirrorItemPredicate,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )))
                .save(consumer, Enderscape.id("mirror_teleport").toString());

        AdvancementHolder longDistance = Advancement.Builder.advancement()
                .parent(mirrorTeleport)
                .display(
                        mirrorTemplate(DyeColor.MAGENTA),
                        Component.translatable("advancement.enderscape.long_distance"),
                        Component.translatable("advancement.enderscape.long_distance.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false
                )
                .rewards(AdvancementRewards.Builder.experience(50))
                .addCriterion("teleported", LODESTONE_TELEPORTATION.createCriterion(new LodestoneTeleportationCriterion.Conditions(
                        Optional.empty(),
                        mirrorItemPredicate,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(
                                DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(2000))
                        ),
                        Optional.of(false)
                )))
                .save(consumer, Enderscape.id("long_distance").toString());

        Advancement.Builder.advancement()
                .parent(longDistance)
                .display(
                        mirrorTemplate(DyeColor.PINK),
                        Component.translatable("advancement.enderscape.transdimensional"),
                        Component.translatable("advancement.enderscape.transdimensional.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false
                )
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("teleported", LODESTONE_TELEPORTATION.createCriterion(new LodestoneTeleportationCriterion.Conditions(
                        Optional.empty(),
                        mirrorItemPredicate,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(true)
                )))
                .save(consumer, Enderscape.id("transdimensional").toString());

        AdvancementHolder pullItemWithAttractor = Advancement.Builder.advancement()
                .parent(obtainNebulite)
                .display(
                        magniaAttractorTemplate(),
                        Component.translatable("advancement.enderscape.pull_item_with_attractor"),
                        Component.translatable("advancement.enderscape.pull_item_with_attractor.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("pulled_item", PULL_ENTITY.createCriterion(new PullEntityCriterion.Conditions(
                        Optional.empty(),
                        Optional.of(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entities, EntityTypes.ITEM)).build()),
                        Optional.empty()
                )))
                .save(consumer, Enderscape.id("pull_item_with_attractor").toString());

        AdvancementHolder stunAttack = Advancement.Builder.advancement()
                .parent(obtainNebulite)
                .display(
                        daggerTemplate(),
                        Component.translatable("advancement.enderscape.stun_attack"),
                        Component.translatable("advancement.enderscape.stun_attack.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion(
                        "stun_attack",
                        STUN_ATTACK.createCriterion(new StunAttackCriterion.Conditions(
                                Optional.empty(),
                                Optional.empty(),
                                Optional.of(List.of(
                                        EntityPredicate.Builder.entity().build()
                                ))
                        ))
                )
                .save(consumer, Enderscape.id("stun_attack").toString());

        AdvancementHolder activateEndHaven = Advancement.Builder.advancement()
                .parent(obtainNebulite)
                .display(
                        DUSK_PURPUR_BLOCK,
                        Component.translatable("advancement.enderscape.activate_end_haven"),
                        Component.translatable("advancement.enderscape.activate_end_haven.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion(
                        "activate_end_haven",
                        ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                                LocationPredicate.Builder.location().setBlock(
                                        BlockPredicate.Builder.block()
                                                .of(blocks, EnderscapeBlocks.END_HAVEN_CORE)
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StateProperties.END_HAVEN_CORE_STATE, EndHavenCoreState.ACTIVE))
                                ),
                                ItemPredicate.Builder.item()
                        )
                )
                .save(consumer, Enderscape.id("activate_end_haven").toString());

//        Advancement.Builder hearMagniaRadioSongs = Advancement.Builder.advancement()
//                .parent(endGatewayKey.identifier())
//                .display(MAGNIA_RADIO,
//                        Component.translatable("advancement.enderscape.hear_magnia_radio_songs"),
//                        Component.translatable("advancement.enderscape.hear_magnia_radio_songs.description"),
//                        null,
//                        AdvancementType.GOAL,
//                        true, true, false)
//                .rewards(AdvancementRewards.Builder.experience(50));
//
//        ALL_DIMENSION_TYPES.forEach(dimension ->
//                hearMagniaRadioSongs.addCriterion(
//                        dimension.identifier().toString(),
//                        HEAR_MAGNIA_RADIO_SONG.createCriterion(new HearMagniaRadioSongCriterion.Conditions(Optional.empty(), Optional.of(LocationPredicate.Builder.inDimension(dimension).build()), Optional.empty()))
//                )
//        );
//
//        hearMagniaRadioSongs.save(consumer, Enderscape.id("hear_magnia_radio_songs").toString());
    }

    private static ItemStackTemplate mirrorTemplate(DyeColor color) {
        DataComponentPatch.Builder builder = DataComponentPatch.builder();
        if (color != null) {
            builder.set(EnderscapeDataComponents.DYE_COLOR, color);
        }
        return new ItemStackTemplate(MIRROR, builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).build());
    }

    private static ItemStackTemplate magniaAttractorTemplate() {
        return new ItemStackTemplate(MAGNIA_ATTRACTOR, DataComponentPatch.builder().set(EnderscapeDataComponents.CURRENT_FUEL, 1).build());
    }

    private static ItemStackTemplate daggerTemplate() {
        return new ItemStackTemplate(DAGGER, DataComponentPatch.builder().set(EnderscapeDataComponents.CURRENT_FUEL, 1).build());
    }
}
