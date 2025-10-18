package net.bunten.enderscape.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.renderer.LightingStyle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.bunten.enderscape.Enderscape.IS_DEBUG;

@Environment(EnvType.CLIENT)
public class EnderscapeModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return EnderscapeModMenu::buildMenu;
    }

    @Environment(EnvType.CLIENT)
    private static Screen buildMenu(Screen parent) {
        return YetAnotherConfigLib.create(
                EnderscapeConfig.HANDLER, (defaults, config, builder) -> builder.title(Component.translatable("menu.enderscape.config"))
                        .category(EnderscapeModMenu.createMainCategory(config))
                        .category(EnderscapeModMenu.createClientsideCategory(config))
        ).generateScreen(parent);
    }

    private static <T> Option<T> boolOption(String name, T defaultValue, Supplier<T> getter, Consumer<T> setter, Function<Option<T>, ControllerBuilder<T>> builder) {
        return Option.<T>createBuilder()
                .name(Component.translatable("option.enderscape." + name))
                .binding(defaultValue, getter, setter)
                .description(OptionDescription.createBuilder().text(Component.translatable("option.enderscape." + name + ".desc")).build())
                .controller(builder)
                .build();
    }

    private static Option<Integer> intOption(String name, int defaultValue, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int step) {
        return Option.<Integer>createBuilder()
                .name(Component.translatable("option.enderscape." + name))
                .binding(defaultValue, getter, setter)
                .description(OptionDescription.createBuilder().text(Component.translatable("option.enderscape." + name + ".desc")).build())
                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(min, max).step(step))
                .build();
    }

    private static ConfigCategory createMainCategory(EnderscapeConfig config) {

        ConfigCategory.Builder main = ConfigCategory.createBuilder().name(Component.translatable("option.enderscape.category.serverside"));

        addDataPackOptions(config, main);
        addServersideAmbienceOptions(config, main);
        addServersideBlockOptions(config, main);
        addServersideEntityOptions(config, main);
        addServersideItemOptions(config, main);

        return main.build();
    }

    private static ConfigCategory createClientsideCategory(EnderscapeConfig config) {

        ConfigCategory.Builder main = ConfigCategory.createBuilder().name(Component.translatable("option.enderscape.category.clientside"));

        if (IS_DEBUG) addDebugOptions(config, main);
        addClientsideAmbienceOptions(config, main);
        addClientsideBlockOptions(config, main);
        addClientsideEntityOptions(config, main);
        addClientsideItemOptions(config, main);
        if (!IS_DEBUG) addDebugOptions(config, main);

        return main.build();
    }

    private static void addDebugOptions(EnderscapeConfig config, ConfigCategory.Builder builder) {
        Option<?> debugHudEnabled = boolOption(
                "debug_hud_enabled",
                false,
                () -> config.debugHudEnabled,
                value -> config.debugHudEnabled = value,
                TickBoxControllerBuilder::create
        );

        Option<?> debugHudClientInfo = boolOption(
                "debug_hud_client_info",
                true,
                () -> config.debugHudClientInfo,
                value -> config.debugHudClientInfo = value,
                TickBoxControllerBuilder::create
        );

        Option<?> debugHudMusicInfo = boolOption(
                "debug_hud_music_info",
                true,
                () -> config.debugHudMusicInfo,
                value -> config.debugHudMusicInfo = value,
                TickBoxControllerBuilder::create
        );

        Option<?> debugHudPlayerInfo = boolOption(
                "debug_hud_player_info",
                true,
                () -> config.debugHudPlayerInfo,
                value -> config.debugHudPlayerInfo = value,
                TickBoxControllerBuilder::create
        );

        Option<?> debugMagniaSproutHitboxes = boolOption(
                "debug_magnia_sprout_hitboxes",
                false,
                () -> config.debugMagniaSproutHitboxes,
                value -> config.debugMagniaSproutHitboxes = value,
                TickBoxControllerBuilder::create
        );

        builder.group(OptionGroup.createBuilder()
                .name(Component.translatable("option.group.enderscape.debug"))

                .option(debugHudEnabled)
                .option(debugHudClientInfo)
                .option(debugHudMusicInfo)
                .option(debugHudPlayerInfo)
                .option(debugMagniaSproutHitboxes)

                .build()
        );
    }

    private static void addDataPackOptions(EnderscapeConfig config, ConfigCategory.Builder builder) {
        Option<?> defaultDataPackFixLevitationAdvancement = boolOption(
                "default_data_pack_fix_levitation_advancement",
                true,
                () -> config.defaultDataPackFixLevitationAdvancement,
                value -> config.defaultDataPackFixLevitationAdvancement = value,
                TickBoxControllerBuilder::create
        );

        Option<?> defaultDataPackFixVanillaRecipes = boolOption(
                "default_data_pack_fix_vanilla_recipes",
                true,
                () -> config.defaultDataPackFixVanillaRecipes,
                value -> config.defaultDataPackFixVanillaRecipes = value,
                TickBoxControllerBuilder::create
        );

        Option<?> defaultDataPackNewEndCities = boolOption(
                "default_data_pack_new_end_cities",
                true,
                () -> config.defaultDataPackNewEndCities,
                value -> config.defaultDataPackNewEndCities = value,
                TickBoxControllerBuilder::create
        );

        Option<?> defaultDataPackNewTerrain = boolOption(
                "default_data_pack_new_terrain",
                true,
                () -> config.defaultDataPackNewTerrain,
                value -> config.defaultDataPackNewTerrain = value,
                TickBoxControllerBuilder::create
        );

        builder.group(OptionGroup.createBuilder()
                .name(Component.translatable("option.group.enderscape.data_packs"))

                .option(defaultDataPackFixLevitationAdvancement)
                .option(defaultDataPackFixVanillaRecipes)
                .option(defaultDataPackNewEndCities)
                .option(defaultDataPackNewTerrain)
                .build()
        );
    }

    private static void addClientsideAmbienceOptions(EnderscapeConfig config, ConfigCategory.Builder builder) {
        Option<?> skyboxUpdateEnabled = boolOption(
                "skybox_update_enabled",
                true,
                () -> config.skyboxUpdateEnabled,
                value -> config.skyboxUpdateEnabled = value,
                TickBoxControllerBuilder::create
        );

        Option<LightingStyle> lightingStyle = Option.<LightingStyle>createBuilder()
                .name(Component.translatable("option.enderscape.lighting_style"))
                .binding(LightingStyle.IMPROVED, () -> config.lightingStyle, value -> config.lightingStyle = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("option.enderscape.lighting_style.desc")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(LightingStyle.class))
                .build();

        Option<?> skyboxAddDynamicFogDensity = boolOption(
                "skybox_add_dynamic_fog_density",
                true,
                () -> config.skyboxAddDynamicFogDensity,
                value -> config.skyboxAddDynamicFogDensity = value,
                TickBoxControllerBuilder::create
        );

        Option<?> skyboxScalesBrightnessWithGamma = boolOption(
                "skybox_scales_brightness_with_gamma",
                true,
                () -> config.skyboxScalesBrightnessWithGamma,
                value -> config.skyboxScalesBrightnessWithGamma = value,
                TickBoxControllerBuilder::create
        );

        Option<?> skyboxBrightnessScaleFactor = intOption(
                "skybox_brightness_scale_factor",
                40,
                () -> config.skyboxBrightnessScaleFactor,
                value -> config.skyboxBrightnessScaleFactor = value,
                10,
                100,
                1
        );

        Option<?> flashEnabled = boolOption(
                "flash_enabled",
                true,
                () -> config.flashEnabled,
                value -> config.flashEnabled = value,
                TickBoxControllerBuilder::create
        );

        Option<?> flashUpdatedVisuals = boolOption(
                "flash_updated_visuals",
                true,
                () -> config.flashUpdatedVisuals,
                value -> config.flashUpdatedVisuals = value,
                TickBoxControllerBuilder::create
        );

        Option<?> flashInfluencesSkybox = boolOption(
                "flash_influences_skybox",
                true,
                () -> config.flashInfluencesSkybox,
                value -> config.flashInfluencesSkybox = value,
                TickBoxControllerBuilder::create
        );

        Option<?> flashFrequency = Option.<Float>createBuilder()
                .name(Component.translatable("option.enderscape.flash_frequency"))
                .binding(2.0F, () -> config.flashFrequency, value -> config.flashFrequency = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("option.enderscape.flash_frequency.desc")).build())
                .controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.5F, 4.0F).step(0.2F))
                .build();

        Option<?> structureMusicFadingEnabled = boolOption(
                "structure_music_fading_enabled",
                true,
                () -> config.structureMusicFadingEnabled,
                value -> config.structureMusicFadingEnabled = value,
                TickBoxControllerBuilder::create
        );

        builder.group(OptionGroup.createBuilder()
                .name(Component.translatable("option.group.enderscape.ambience"))

                .option(lightingStyle)
                .option(skyboxUpdateEnabled)
                .option(skyboxScalesBrightnessWithGamma)
                .option(skyboxBrightnessScaleFactor)
                .option(skyboxAddDynamicFogDensity)
                .option(flashEnabled)
                .option(flashUpdatedVisuals)
                .option(flashInfluencesSkybox)
                .option(flashFrequency)
                .option(structureMusicFadingEnabled)

                .build()
        );
    }

    private static void addServersideAmbienceOptions(EnderscapeConfig config, ConfigCategory.Builder builder) {
        Option<?> updateDefaultEndMusic = boolOption(
                "ambience_update_music_pools",
                true,
                () -> config.ambienceUpdateMusicPools,
                value -> config.ambienceUpdateMusicPools = value,
                TickBoxControllerBuilder::create
        );

        Option<?> updateDefaultEndLoop = boolOption(
                "ambience_update_loop_sounds",
                true,
                () -> config.ambienceUpdateLoopSounds,
                value -> config.ambienceUpdateLoopSounds = value,
                TickBoxControllerBuilder::create
        );

        Option<?> updateDefaultEndAdditions = boolOption(
                "ambience_update_addition_sounds",
                true,
                () -> config.ambienceUpdateAdditionSounds,
                value -> config.ambienceUpdateAdditionSounds = value,
                TickBoxControllerBuilder::create
        );

        Option<?> updateDefaultEndMood = boolOption(
                "ambience_update_mood_sounds",
                true,
                () -> config.ambienceUpdateMoodSounds,
                value -> config.ambienceUpdateMoodSounds = value,
                TickBoxControllerBuilder::create
        );

        Option<?> updateDefaultEndParticles = boolOption(
                "ambience_update_particles",
                true,
                () -> config.ambienceUpdateParticles,
                value -> config.ambienceUpdateParticles = value,
                TickBoxControllerBuilder::create
        );

        Option<?> updateDefaultEndSkyColor = boolOption(
                "ambience_update_sky_colors",
                true,
                () -> config.ambienceUpdateSkyColors,
                value -> config.ambienceUpdateSkyColors = value,
                TickBoxControllerBuilder::create
        );

        Option<?> updateDefaultEndFogColor = boolOption(
                "ambience_update_fog_colors",
                true,
                () -> config.ambienceUpdateFogColors,
                value -> config.ambienceUpdateFogColors = value,
                TickBoxControllerBuilder::create
        );

        Option<?> updateDefaultEndGrassColor = boolOption(
                "ambience_update_grass_colors",
                true,
                () -> config.ambienceUpdateGrassColors,
                value -> config.ambienceUpdateGrassColors = value,
                TickBoxControllerBuilder::create
        );

        Option<?> updateDefaultEndFoliageColor = boolOption(
                "ambience_update_foliage_colors",
                true,
                () -> config.ambienceUpdateFoliageColors,
                value -> config.ambienceUpdateFoliageColors = value,
                TickBoxControllerBuilder::create
        );

        Option<?> updateDefaultEndWaterColor = boolOption(
                "ambience_update_water_colors",
                true,
                () -> config.ambienceUpdateWaterColors,
                value -> config.ambienceUpdateWaterColors = value,
                TickBoxControllerBuilder::create
        );

        Option<?> updateDefaultEndWaterFogColor = boolOption(
                "ambience_update_water_fog_colors",
                true,
                () -> config.ambienceUpdateWaterFogColors,
                value -> config.ambienceUpdateWaterFogColors = value,
                TickBoxControllerBuilder::create
        );

        builder.group(OptionGroup.createBuilder()
                .name(Component.translatable("option.group.enderscape.ambience"))

                .option(updateDefaultEndMusic)
                .option(updateDefaultEndLoop)
                .option(updateDefaultEndAdditions)
                .option(updateDefaultEndMood)
                .option(updateDefaultEndParticles)
                .option(updateDefaultEndSkyColor)
                .option(updateDefaultEndFogColor)
                .option(updateDefaultEndGrassColor)
                .option(updateDefaultEndFoliageColor)
                .option(updateDefaultEndWaterColor)
                .option(updateDefaultEndWaterFogColor)

                .build()
        );
    }

    private static void addServersideBlockOptions(EnderscapeConfig config, ConfigCategory.Builder builder) {
        Option<?> blockSoundsUpdateChorus = boolOption(
                "block_sounds_update_chorus",
                true,
                () -> config.blocksSoundUpdateChorus,
                value -> config.blocksSoundUpdateChorus = value,
                TickBoxControllerBuilder::create
        );

        Option<?> blocksSoundUpdatePurpur = boolOption(
                "block_sounds_update_purpur",
                true,
                () -> config.blocksSoundUpdatePurpur,
                value -> config.blocksSoundUpdatePurpur = value,
                TickBoxControllerBuilder::create
        );

        Option<?> blockSoundUpdateEndPortalFrame = boolOption(
                "block_sounds_update_end_portal_frame",
                true,
                () -> config.blockSoundUpdateEndPortalFrame,
                value -> config.blockSoundUpdateEndPortalFrame = value,
                TickBoxControllerBuilder::create
        );

        Option<?> blockSoundUpdateEndPortals = boolOption(
                "block_sounds_update_end_portals",
                true,
                () -> config.blockSoundUpdateEndPortals,
                value -> config.blockSoundUpdateEndPortals = value,
                TickBoxControllerBuilder::create
        );

        Option<?> blockSoundsUpdateEndRods = boolOption(
                "block_sounds_update_end_rods",
                true,
                () -> config.blockSoundsUpdateEndRods,
                value -> config.blockSoundsUpdateEndRods = value,
                TickBoxControllerBuilder::create
        );

        Option<?> blockSoundUpdateEndStone = boolOption(
                "block_sounds_update_end_stone",
                true,
                () -> config.blockSoundUpdateEndStone,
                value -> config.blockSoundUpdateEndStone = value,
                TickBoxControllerBuilder::create
        );

        Option<?> blockSoundUpdateEndStoneBricks = boolOption(
                "block_sounds_update_end_stone_bricks",
                true,
                () -> config.blockSoundUpdateEndStoneBricks,
                value -> config.blockSoundUpdateEndStoneBricks = value,
                TickBoxControllerBuilder::create
        );

        Option<?> blockSoundUpdateShulkerBoxes = boolOption(
                "block_sounds_update_shulker_boxes",
                true,
                () -> config.blockSoundUpdateShulkerBoxes,
                value -> config.blockSoundUpdateShulkerBoxes = value,
                TickBoxControllerBuilder::create
        );

        builder.group(OptionGroup.createBuilder()
                .name(Component.translatable("option.group.enderscape.blocks"))

                .option(blockSoundsUpdateChorus)
                .option(blockSoundUpdateEndPortalFrame)
                .option(blockSoundUpdateEndPortals)
                .option(blockSoundsUpdateEndRods)
                .option(blockSoundUpdateEndStoneBricks)
                .option(blockSoundUpdateEndStone)
                .option(blocksSoundUpdatePurpur)
                .option(blockSoundUpdateShulkerBoxes)

                .build()
        );
    }

    private static void addClientsideBlockOptions(EnderscapeConfig config, ConfigCategory.Builder builder) {

        Option<?> chorusFlowerHumming = boolOption(
                "chorus_flower_humming",
                true,
                () -> config.chorusFlowerHumming,
                value -> config.chorusFlowerHumming = value,
                TickBoxControllerBuilder::create
        );

        Option<?> chorusFlowerPollen = boolOption(
                "chorus_flower_pollen",
                true,
                () -> config.chorusFlowerPollen,
                value -> config.chorusFlowerPollen = value,
                TickBoxControllerBuilder::create
        );

        Option<?> endPortalUpdateParticles = boolOption(
                "end_portal_update_particles",
                true,
                () -> config.endPortalUpdateParticles,
                value -> config.endPortalUpdateParticles = value,
                TickBoxControllerBuilder::create
        );

        Option<?> endPortalUpdateTravelSound = boolOption(
                "end_portal_update_travel_sound",
                true,
                () -> config.endPortalUpdateTravelSound,
                value -> config.endPortalUpdateTravelSound = value,
                TickBoxControllerBuilder::create
        );

        builder.group(OptionGroup.createBuilder()
                .name(Component.translatable("option.group.enderscape.blocks"))

                .option(chorusFlowerHumming)
                .option(chorusFlowerPollen)
                .option(endPortalUpdateParticles)
                .option(endPortalUpdateTravelSound)

                .build()
        );
    }

    private static void addServersideEntityOptions(EnderscapeConfig config, ConfigCategory.Builder builder) {

        Option<?> endermanStereoStareSound = boolOption(
                "enderman_stereo_stare_sound",
                true,
                () -> config.endermanStereoStareSound,
                value -> config.endermanStereoStareSound = value,
                TickBoxControllerBuilder::create
        );

        Option<?> endermiteExpandHitRange = boolOption(
                "endermite_expand_hit_range",
                true,
                () -> config.endermiteExpandHitRange,
                value -> config.endermiteExpandHitRange = value,
                TickBoxControllerBuilder::create
        );

        Option<?> endermiteUpdateSounds = boolOption(
                "endermite_update_sounds",
                true,
                () -> config.endermiteUpdateSounds,
                value -> config.endermiteUpdateSounds = value,
                TickBoxControllerBuilder::create
        );

        Option<?> rubblemiteExpandHitRange = boolOption(
                "rubblemite_expand_hit_range",
                true,
                () -> config.rubblemiteExpandHitRange,
                value -> config.rubblemiteExpandHitRange = value,
                TickBoxControllerBuilder::create
        );

        Option<?> shulkerBulletEnforceTimeLimit = intOption(
                "shulker_bullet_enforce_time_limit",
                30,
                () -> config.shulkerBulletEnforceTimeLimit,
                value -> config.shulkerBulletEnforceTimeLimit = value,
                0,
                60,
                1
        );

        Option<?> shulkerBulletEnforceDistanceLimit = intOption(
                "shulker_bullet_enforce_distance_limit",
                30,
                () -> config.shulkerBulletEnforceDistanceLimit,
                value -> config.shulkerBulletEnforceDistanceLimit = value,
                0,
                100,
                1
        );

        Option<?> shulkerBulletEnforceOwnerLimit = boolOption(
                "shulker_bullet_enforce_owner_limit",
                true,
                () -> config.shulkerBulletEnforceOwnerLimit,
                value -> config.shulkerBulletEnforceOwnerLimit = value,
                TickBoxControllerBuilder::create
        );

        Option<?> shulkerBulletRebalanceLevitation = boolOption(
                "shulker_bullet_rebalance_levitation",
                true,
                () -> config.shulkerBulletRebalanceLevitation,
                value -> config.shulkerBulletRebalanceLevitation = value,
                TickBoxControllerBuilder::create
        );

        Option<?> shulkerBulletEnforceCountLimit = intOption(
                "shulker_bullet_enforce_count_limit",
                3,
                () -> config.shulkerBulletEnforceCountLimit,
                value -> config.shulkerBulletEnforceCountLimit = value,
                0,
                8,
                1
        );

        Option<?> shulkerHurtByPiercing = boolOption(
                "shulker_hurt_by_piercing",
                true,
                () -> config.shulkerHurtByPiercing,
                value -> config.shulkerHurtByPiercing = value,
                TickBoxControllerBuilder::create
        );

        Option<?> silverfishExpandHitRange = boolOption(
                "silverfish_expand_hit_range",
                true,
                () -> config.silverfishExpandHitRange,
                value -> config.silverfishExpandHitRange = value,
                TickBoxControllerBuilder::create
        );

        Option<?> voidPoofParticlesUponDeath = boolOption(
                "void_poof_particles_upon_death",
                true,
                () -> config.voidPoofParticlesUponDeath,
                value -> config.voidPoofParticlesUponDeath = value,
                TickBoxControllerBuilder::create
        );

        builder.group(OptionGroup.createBuilder()
                .name(Component.translatable("option.group.enderscape.entity"))

                .option(endermanStereoStareSound)
                .option(endermiteExpandHitRange)
                .option(endermiteUpdateSounds)
                .option(rubblemiteExpandHitRange)
                .option(shulkerBulletEnforceCountLimit)
                .option(shulkerBulletEnforceDistanceLimit)
                .option(shulkerBulletEnforceOwnerLimit)
                .option(shulkerBulletEnforceTimeLimit)
                .option(shulkerBulletRebalanceLevitation)
                .option(shulkerHurtByPiercing)
                .option(silverfishExpandHitRange)
                .option(voidPoofParticlesUponDeath)

                .build()
        );
    }

    private static void addClientsideEntityOptions(EnderscapeConfig config, ConfigCategory.Builder builder) {
        Option<?> portalParticleEmissive = boolOption(
                "portal_particle_emissive",
                true,
                () -> config.portalParticleEmissive,
                value -> config.portalParticleEmissive = value,
                TickBoxControllerBuilder::create
        );

        Option<?> endermiteEmissiveEyes = boolOption(
                "endermite_emissive_eyes",
                true,
                () -> config.endermiteEmissiveEyes,
                value -> config.endermiteEmissiveEyes = value,
                TickBoxControllerBuilder::create
        );

        Option<?> shulkerBulletLoopSound = boolOption(
                "shulker_bullet_loop_sound",
                true,
                () -> config.shulkerBulletLoopSound,
                value -> config.shulkerBulletLoopSound = value,
                TickBoxControllerBuilder::create
        );

        Option<?> endermanStaticSound = boolOption(
                "enderman_static_sound",
                true,
                () -> config.endermanStaticSound,
                value -> config.endermanStaticSound = value,
                TickBoxControllerBuilder::create
        );

        Option<?> endermanStaticOverlay = boolOption(
                "enderman_static_overlay",
                true,
                () -> config.endermanStaticOverlay,
                value -> config.endermanStaticOverlay = value,
                TickBoxControllerBuilder::create
        );

        builder.group(OptionGroup.createBuilder()
                .name(Component.translatable("option.group.enderscape.entity"))

                .option(endermanStaticOverlay)
                .option(endermanStaticSound)
                .option(endermiteEmissiveEyes)
                .option(portalParticleEmissive)
                .option(shulkerBulletLoopSound)

                .build()
        );
    }

    private static void addServersideItemOptions(EnderscapeConfig config, ConfigCategory.Builder builder) {

        Option<?> elytraAddOpenCloseSounds = boolOption(
                "elytra_add_open_close_sounds",
                true,
                () -> config.elytraAddOpenCloseSounds,
                value -> config.elytraAddOpenCloseSounds = value,
                TickBoxControllerBuilder::create
        );

        Option<?> elytraSneakToStopGliding = boolOption(
                "elytra_sneak_to_stop_gliding",
                true,
                () -> config.elytraSneakToStopGliding,
                value -> config.elytraSneakToStopGliding = value,
                TickBoxControllerBuilder::create
        );

        Option<?> elytraUpdateEquipSound = boolOption(
                "elytra_update_equip_sound",
                true,
                () -> config.elytraUpdateEquipSound,
                value -> config.elytraUpdateEquipSound = value,
                TickBoxControllerBuilder::create
        );

        Option<?> enderPearlBreakParticles = boolOption(
                "ender_pearl_break_particles",
                true,
                () -> config.enderPearlBreakParticles,
                value -> config.enderPearlBreakParticles = value,
                TickBoxControllerBuilder::create
        );

        Option<?> enderPearlUpdateTeleportSound = boolOption(
                "ender_pearl_update_teleport_sound",
                true,
                () -> config.enderPearlUpdateTeleportSound,
                value -> config.enderPearlUpdateTeleportSound = value,
                TickBoxControllerBuilder::create
        );

        Option<?> enderPearlUpdateThrowSound = boolOption(
                "ender_pearl_update_throw_sound",
                true,
                () -> config.enderPearlUpdateThrowSound,
                value -> config.enderPearlUpdateThrowSound = value,
                TickBoxControllerBuilder::create
        );

        Option<?> tridentsReturnFromVoid = boolOption(
                "tridents_return_from_void",
                true,
                () -> config.tridentsReturnFromVoid,
                value -> config.tridentsReturnFromVoid = value,
                TickBoxControllerBuilder::create
        );

        builder.group(OptionGroup.createBuilder()
                .name(Component.translatable("option.group.enderscape.item"))

                .option(elytraAddOpenCloseSounds)
                .option(elytraSneakToStopGliding)
                .option(elytraUpdateEquipSound)
                .option(enderPearlBreakParticles)
                .option(enderPearlUpdateTeleportSound)
                .option(enderPearlUpdateThrowSound)
                .option(tridentsReturnFromVoid)

                .build()
        );
    }

    private static void addClientsideItemOptions(EnderscapeConfig config, ConfigCategory.Builder builder) {
        Option<?> elytraAddGlidingSound = boolOption(
                "elytra_add_gliding_sound",
                true,
                () -> config.elytraAddGlidingSound,
                value -> config.elytraAddGlidingSound = value,
                TickBoxControllerBuilder::create
        );

        Option<?> elytraAddFovEffects = boolOption(
                "elytra_add_fov_effects",
                true,
                () -> config.elytraAddFovEffects,
                value -> config.elytraAddFovEffects = value,
                TickBoxControllerBuilder::create
        );

        Option<?> elytraFovEffectIntensity = intOption(
                "elytra_fov_effect_intensity",
                100,
                () -> config.elytraFovEffectIntensity,
                value -> config.elytraFovEffectIntensity = value,
                0,
                200,
                1
        );

        Option<?> enderPearlAddParticles = boolOption(
                "ender_pearl_add_particles",
                true,
                () -> config.enderPearlAddParticles,
                value -> config.enderPearlAddParticles = value,
                TickBoxControllerBuilder::create
        );

        Option<?> mirrorTooltipEnabled = boolOption(
                "mirror_tooltip_enabled",
                true,
                () -> config.mirrorTooltipEnabled,
                value -> config.mirrorTooltipEnabled = value,
                TickBoxControllerBuilder::create
        );

        Option<?> mirrorTooltipDisplayCoordinates = boolOption(
                "mirror_tooltip_display_coordinates",
                false,
                () -> config.mirrorTooltipDisplayCoordinates,
                value -> config.mirrorTooltipDisplayCoordinates = value,
                TickBoxControllerBuilder::create
        );

        Option<?> mirrorTooltipDisplayDimension = boolOption(
                "mirror_tooltip_display_dimension",
                true,
                () -> config.mirrorTooltipDisplayDimension,
                value -> config.mirrorTooltipDisplayDimension = value,
                TickBoxControllerBuilder::create
        );

        Option<?> mirrorTooltipDisplayDistance = boolOption(
                "mirror_tooltip_display_distance",
                true,
                () -> config.mirrorTooltipDisplayDistance,
                value -> config.mirrorTooltipDisplayDistance = value,
                TickBoxControllerBuilder::create
        );

        Option<?> mirrorTooltipShiftToDisplay = boolOption(
                "mirror_tooltip_shift_to_display",
                false,
                () -> config.mirrorTooltipShiftToDisplay,
                value -> config.mirrorTooltipShiftToDisplay = value,
                TickBoxControllerBuilder::create
        );

        Option<?> mirrorScreenEffectEnabled = boolOption(
                "mirror_screen_effect_enabled",
                true,
                () -> config.mirrorScreenEffectEnabled,
                value -> config.mirrorScreenEffectEnabled = value,
                TickBoxControllerBuilder::create
        );

        Option<?> mirrorScreenEffectOverlayIntensity = intOption(
                "mirror_screen_effect_overlay_intensity",
                50,
                () -> config.mirrorScreenEffectOverlayIntensity,
                value -> config.mirrorScreenEffectOverlayIntensity = value,
                0,
                100,
                1
        );

        Option<?> mirrorScreenEffectVignetteIntensity = intOption(
                "mirror_screen_effect_vignette_intensity",
                50,
                () -> config.mirrorScreenEffectVignetteIntensity,
                value -> config.mirrorScreenEffectVignetteIntensity = value,
                0,
                100,
                1
        );

        Option<?> nebuliteToolHudEnabled = boolOption(
                "nebulite_tool_hud_enabled",
                true,
                () -> config.nebuliteToolHudEnabled,
                value -> config.nebuliteToolHudEnabled = value,
                TickBoxControllerBuilder::create
        );

        Option<?> nebuliteToolHudOffset = intOption(
                "nebulite_tool_hud_offset",
                0,
                () -> config.nebuliteToolHudOffset,
                value -> config.nebuliteToolHudOffset = value,
                0,
                50,
                1
        );

        Option<?> nebuliteToolHudOpacity = intOption(
                "nebulite_tool_hud_opacity",
                100,
                () -> config.nebuliteToolHudOpacity,
                value -> config.nebuliteToolHudOpacity = value,
                10,
                100,
                1
        );

        builder.group(OptionGroup.createBuilder()
                .name(Component.translatable("option.group.enderscape.item"))

                .option(elytraAddFovEffects)
                .option(elytraFovEffectIntensity)
                .option(elytraAddGlidingSound)
                .option(enderPearlAddParticles)
                .option(mirrorTooltipEnabled)
                .option(mirrorTooltipShiftToDisplay)
                .option(mirrorTooltipDisplayCoordinates)
                .option(mirrorTooltipDisplayDimension)
                .option(mirrorTooltipDisplayDistance)
                .option(mirrorScreenEffectEnabled)
                .option(mirrorScreenEffectOverlayIntensity)
                .option(mirrorScreenEffectVignetteIntensity)
                .option(nebuliteToolHudEnabled)
                .option(nebuliteToolHudOffset)
                .option(nebuliteToolHudOpacity)

                .build()
        );
    }
}