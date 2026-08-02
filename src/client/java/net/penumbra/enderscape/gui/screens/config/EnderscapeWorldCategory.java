package net.penumbra.enderscape.gui.screens.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.config.value.EndFlashStyle;
import net.penumbra.enderscape.config.value.LightingStyle;

public class EnderscapeWorldCategory {

    public static ConfigCategory create(EnderscapeConfig defaults, EnderscapeConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder().name(Component.translatable("screen.enderscape.config.category.world"));

        structure(defaults, config, category);
        skybox(defaults, config, category);
        misc(defaults, config, category);

        return category.build();
    }

    public static void structure(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("screen.enderscape.config.group.structure"))

                .option(EnderscapeModMenu.create(
                        "structure_music_fading_enabled",
                        defaults.structureMusicFadingEnabled,
                        () -> config.structureMusicFadingEnabled,
                        value -> config.structureMusicFadingEnabled = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "supplement_vanilla_stronghold_library_loot",
                        defaults.supplementVanillaStrongholdLibraryLoot,
                        () -> config.supplementVanillaStrongholdLibraryLoot,
                        value -> config.supplementVanillaStrongholdLibraryLoot = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "supplement_vanilla_end_city_treasure_loot",
                        defaults.supplementVanillaEndCityTreasureLoot,
                        () -> config.supplementVanillaEndCityTreasureLoot,
                        value -> config.supplementVanillaEndCityTreasureLoot = value,
                        TickBoxControllerBuilder::create
                ))
                .build()
        );
    }

    public static void skybox(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("screen.enderscape.config.group.skybox"))

                .option(EnderscapeModMenu.create(
                        "skybox_update_enabled",
                        defaults.skyboxUpdateEnabled,
                        () -> config.skyboxUpdateEnabled,
                        value -> config.skyboxUpdateEnabled = value,
                        TickBoxControllerBuilder::create,
                        true
                ))
                .option(EnderscapeModMenu.create(
                        "skybox_brightness_scale_factor",
                        defaults.skyboxBrightnessScaleFactor,
                        () -> config.skyboxBrightnessScaleFactor,
                        value -> config.skyboxBrightnessScaleFactor = value,
                        option -> IntegerSliderControllerBuilder.create(option).range(0, 100).step(1)
                ))
                .option(EnderscapeModMenu.create(
                        "end_flash_style",
                        defaults.endFlashStyle,
                        () -> config.endFlashStyle,
                        value -> config.endFlashStyle = value,
                        option -> EnumControllerBuilder.create(option).enumClass(EndFlashStyle.class),
                        true
                ))
                .option(EnderscapeModMenu.create(
                        "end_flash_influences_skybox",
                        defaults.endFlashInfluencesSkybox,
                        () -> config.endFlashInfluencesSkybox,
                        value -> config.endFlashInfluencesSkybox = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "end_flash_frequency",
                        defaults.endFlashFrequency,
                        () -> config.endFlashFrequency,
                        value -> config.endFlashFrequency = value,
                        opt -> FloatSliderControllerBuilder.create(opt).range(0.5F, 10.0F).step(0.1F)
                ))
                .option(EnderscapeModMenu.create(
                        "fog_density_updated",
                        defaults.fogDensityUpdated,
                        () -> config.fogDensityUpdated,
                        value -> config.fogDensityUpdated = value,
                        TickBoxControllerBuilder::create
                ))

                .build()
        );
    }

    public static void misc(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("key.category.minecraft.misc"))

                .option(EnderscapeModMenu.create(
                        "outer_void_height_threshold",
                        defaults.outerVoidHeightTreshold,
                        () -> config.outerVoidHeightTreshold,
                        value -> config.outerVoidHeightTreshold = value,
                        option -> IntegerSliderControllerBuilder.create(option).range(-64, -1).step(1)
                ))
                .option(EnderscapeModMenu.create(
                        "lighting_style",
                        defaults.lightingStyle,
                        () -> config.lightingStyle,
                        value -> config.lightingStyle = value,
                        option -> EnumControllerBuilder.create(option).enumClass(LightingStyle.class),
                        true
                ))
                .option(EnderscapeModMenu.create(
                        "ambience_update_grass_colors",
                        defaults.ambienceUpdateGrassColors,
                        () -> config.ambienceUpdateGrassColors,
                        value -> config.ambienceUpdateGrassColors = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "ambience_update_foliage_colors",
                        defaults.ambienceUpdateFoliageColors,
                        () -> config.ambienceUpdateFoliageColors,
                        value -> config.ambienceUpdateFoliageColors = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "ambience_update_water_colors",
                        defaults.ambienceUpdateWaterColors,
                        () -> config.ambienceUpdateWaterColors,
                        value -> config.ambienceUpdateWaterColors = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "ambience_update_water_fog_colors",
                        defaults.ambienceUpdateWaterFogColors,
                        () -> config.ambienceUpdateWaterFogColors,
                        value -> config.ambienceUpdateWaterFogColors = value,
                        TickBoxControllerBuilder::create
                ))

                .build()
        );
    }
}
