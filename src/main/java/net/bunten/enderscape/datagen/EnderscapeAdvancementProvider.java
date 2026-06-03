package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.criteria.*;
import net.bunten.enderscape.registry.EnderscapeBiomes;
import net.bunten.enderscape.registry.EnderscapeDataComponents;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeCriteria.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.advancements.triggers.CriteriaTriggers.FALL_FROM_HEIGHT;
import static net.minecraft.world.item.Items.BUCKET;
import static net.minecraft.world.item.Items.FIREWORK_ROCKET;

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

        HolderGetter<Item> itemRegistry = provider.lookupOrThrow(Registries.ITEM);
        HolderGetter<Block> blockRegistry = provider.lookupOrThrow(Registries.BLOCK);
        HolderLookup.RegistryLookup<EntityType<?>> entityRegistry = provider.lookupOrThrow(Registries.ENTITY_TYPE);

        ResourceKey<Advancement> endGatewayKey = ResourceKey.create(Registries.ADVANCEMENT, Identifier.withDefaultNamespace("end/enter_end_gateway"));
        ResourceKey<Advancement> findEndCityKey = ResourceKey.create(Registries.ADVANCEMENT, Identifier.withDefaultNamespace("end/find_end_city"));
        ResourceKey<Advancement> elytraKey = ResourceKey.create(Registries.ADVANCEMENT, Identifier.withDefaultNamespace("end/elytra"));

        ItemStackTemplate glintMirror = new ItemStackTemplate(MIRROR, DataComponentPatch.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .build());

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
                                                        .steppingOn(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blockRegistry, EnderscapeBlockTags.CHANTERELLE_CAP_BLOCKS)))
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
                        END_STONE_RUBBLE_SHIELD,
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
                                Optional.of(ItemPredicate.Builder.item().of(itemRegistry, EnderscapeItemTags.RUBBLE_SHIELDS).build()),
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
                                ItemPredicate.Builder.item().of(itemRegistry, BUCKET),
                                Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(entityRegistry, EnderscapeEntities.RUSTLE)))
                        )
                )
                .save(consumer, Enderscape.id("rustle_bucket").toString());

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
                                LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blockRegistry, END_VAULT)),
                                ItemPredicate.Builder.item().of(itemRegistry, END_CITY_KEY)
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
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome(provider.lookupOrThrow(Registries.BIOME).getOrThrow(biome)))
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
                        FIREWORK_ROCKET,
                        Component.translatable("advancement.enderscape.glide_onto_drifter"),
                        Component.translatable("advancement.enderscape.glide_onto_drifter.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false
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

        Optional<ItemPredicate> mirrorItemPredicate = Optional.of(ItemPredicate.Builder.item().of(itemRegistry, MIRROR).build());

        AdvancementHolder mirrorTeleport = Advancement.Builder.advancement()
                .parent(obtainNebulite)
                .display(
                        glintMirror,
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
                        glintMirror,
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
                        glintMirror,
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

        ItemStackTemplate attractor = new ItemStackTemplate(MAGNIA_ATTRACTOR, DataComponentPatch.builder()
                .set(EnderscapeDataComponents.CURRENT_FUEL, 1).build());

        AdvancementHolder pullItemWithAttractor = Advancement.Builder.advancement()
                .parent(obtainNebulite)
                .display(
                        attractor,
                        Component.translatable("advancement.enderscape.pull_item_with_attractor"),
                        Component.translatable("advancement.enderscape.pull_item_with_attractor.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("pulled_item", PULL_ENTITY.createCriterion(new PullEntityCriterion.Conditions(
                        Optional.empty(),
                        Optional.of(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityRegistry, EntityTypes.ITEM)).build()),
                        Optional.empty()
                )))
                .save(consumer, Enderscape.id("pull_item_with_attractor").toString());

        ItemStackTemplate dagger = new ItemStackTemplate(DAGGER, DataComponentPatch.builder().set(EnderscapeDataComponents.CURRENT_FUEL, 1).build());

        AdvancementHolder stunAttack = Advancement.Builder.advancement()
                .parent(obtainNebulite)
                .display(
                        dagger,
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
                                Optional.empty(),
                                Optional.empty(),
                                Optional.of(true)
                        ))
                )
                .save(consumer, Enderscape.id("stun_attack").toString());

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
}
