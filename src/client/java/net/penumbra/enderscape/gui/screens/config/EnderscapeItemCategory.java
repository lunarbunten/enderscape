package net.penumbra.enderscape.gui.screens.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;
import net.penumbra.enderscape.config.EnderscapeConfig;

public class EnderscapeItemCategory {

    public static ConfigCategory create(EnderscapeConfig defaults, EnderscapeConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder().name(Component.translatable("screen.enderscape.config.category.item"));

        creativeMode(defaults, config, category);
        enderPearl(defaults, config, category);
        elytra(defaults, config, category);
        mirror(defaults, config, category);
        nebuliteTool(defaults, config, category);
        misc(defaults, config, category);

        return category.build();
    }

    public static void creativeMode(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("gameMode.creative"))

                .option(EnderscapeModMenu.create(
                        "creative_tab_enabled",
                        defaults.creativeTabEnabled,
                        () -> config.creativeTabEnabled,
                        value -> config.creativeTabEnabled = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "include_items_in_vanilla_creative_tabs",
                        defaults.includeItemsInVanillaCreativeTabs,
                        () -> config.includeItemsInVanillaCreativeTabs,
                        value -> config.includeItemsInVanillaCreativeTabs = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "include_colored_mirrors_in_creative_tabs",
                        defaults.includeColoredMirrorsInCreativeTabs,
                        () -> config.includeColoredMirrorsInCreativeTabs,
                        value -> config.includeColoredMirrorsInCreativeTabs = value,
                        TickBoxControllerBuilder::create
                ))
                .build()
        );
    }

    public static void misc(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("key.category.minecraft.misc"))

                .option(EnderscapeModMenu.create(
                        "rubble_shield_fov_effect_intensity",
                        defaults.rubbleShieldFovEffectIntensity,
                        () -> config.rubbleShieldFovEffectIntensity,
                        value -> config.rubbleShieldFovEffectIntensity = value,
                        option -> IntegerSliderControllerBuilder.create(option).range(0, 200).step(1)
                ))
                .option(EnderscapeModMenu.create(
                        "tridents_return_from_void",
                        defaults.tridentsReturnFromVoid,
                        () -> config.tridentsReturnFromVoid,
                        value -> config.tridentsReturnFromVoid = value,
                        TickBoxControllerBuilder::create
                ))
                .build()
        );
    }

    public static void enderPearl(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("item.minecraft.ender_pearl"))

                .option(EnderscapeModMenu.create(
                        "ender_pearl_add_flying_particles",
                        defaults.enderPearlAddFlyingParticles,
                        () -> config.enderPearlAddFlyingParticles,
                        value -> config.enderPearlAddFlyingParticles = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "ender_pearl_add_shattering_particles",
                        defaults.enderPearlAddShatteringParticles,
                        () -> config.enderPearlAddShatteringParticles,
                        value -> config.enderPearlAddShatteringParticles = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "ender_pearl_update_teleport_sound",
                        defaults.enderPearlUpdateTeleportSound,
                        () -> config.enderPearlUpdateTeleportSound,
                        value -> config.enderPearlUpdateTeleportSound = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "ender_pearl_update_throw_sound",
                        defaults.enderPearlUpdateThrowSound,
                        () -> config.enderPearlUpdateThrowSound,
                        value -> config.enderPearlUpdateThrowSound = value,
                        TickBoxControllerBuilder::create
                ))

                .build()
        );
    }

    public static void elytra(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("item.minecraft.elytra"))

                .option(EnderscapeModMenu.create(
                        "elytra_hunger_exhaustion",
                        defaults.elytraHungerExhaustion,
                        () -> config.elytraHungerExhaustion,
                        value -> config.elytraHungerExhaustion = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "elytra_sneak_to_stop_gliding",
                        defaults.elytraSneakToStopGliding,
                        () -> config.elytraSneakToStopGliding,
                        value -> config.elytraSneakToStopGliding = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "elytra_fov_effect_intensity",
                        defaults.elytraFovEffectIntensity,
                        () -> config.elytraFovEffectIntensity,
                        value -> config.elytraFovEffectIntensity = value,
                        option -> IntegerSliderControllerBuilder.create(option).range(0, 200).step(1)
                ))
                .option(EnderscapeModMenu.create(
                        "elytra_add_open_close_sounds",
                        defaults.elytraAddOpenCloseSounds,
                        () -> config.elytraAddOpenCloseSounds,
                        value -> config.elytraAddOpenCloseSounds = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "elytra_add_gliding_sound",
                        defaults.elytraAddGlidingSound,
                        () -> config.elytraAddGlidingSound,
                        value -> config.elytraAddGlidingSound = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "elytra_update_equip_sound",
                        defaults.elytraUpdateEquipSound,
                        () -> config.elytraUpdateEquipSound,
                        value -> config.elytraUpdateEquipSound = value,
                        TickBoxControllerBuilder::create
                ))
                .build()
        );
    }

    public static void mirror(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("item.enderscape.mirror"))

                .option(EnderscapeModMenu.create(
                        "mirror_tooltip_shift_to_display",
                        defaults.mirrorTooltipShiftToDisplay,
                        () -> config.mirrorTooltipShiftToDisplay,
                        value -> config.mirrorTooltipShiftToDisplay = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "mirror_tooltip_display_coordinates",
                        defaults.mirrorTooltipDisplayCoordinates,
                        () -> config.mirrorTooltipDisplayCoordinates,
                        value -> config.mirrorTooltipDisplayCoordinates = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "mirror_tooltip_display_dimension",
                        defaults.mirrorTooltipDisplayDimension,
                        () -> config.mirrorTooltipDisplayDimension,
                        value -> config.mirrorTooltipDisplayDimension = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "mirror_tooltip_display_distance",
                        defaults.mirrorTooltipDisplayDistance,
                        () -> config.mirrorTooltipDisplayDistance,
                        value -> config.mirrorTooltipDisplayDistance = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "mirror_screen_effect_intensity",
                        defaults.mirrorScreenEffectIntensity,
                        () -> config.mirrorScreenEffectIntensity,
                        value -> config.mirrorScreenEffectIntensity = value,
                        option -> IntegerSliderControllerBuilder.create(option).range(0, 100).step(1)
                ))
                .build()
        );
    }

    public static void nebuliteTool(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("screen.enderscape.config.group.nebulite_tool"))

                .option(EnderscapeModMenu.create(
                        "nebulite_tool_hud_enabled",
                        defaults.nebuliteToolHudEnabled,
                        () -> config.nebuliteToolHudEnabled,
                        value -> config.nebuliteToolHudEnabled = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "nebulite_tool_hud_offset",
                        defaults.nebuliteToolHudOffset,
                        () -> config.nebuliteToolHudOffset,
                        value -> config.nebuliteToolHudOffset = value,
                        option -> IntegerSliderControllerBuilder.create(option).range(-25, 25).step(1)
                ))
                .option(EnderscapeModMenu.create(
                        "nebulite_tool_hud_opacity",
                        defaults.nebuliteToolHudOpacity,
                        () -> config.nebuliteToolHudOpacity,
                        value -> config.nebuliteToolHudOpacity = value,
                        option -> IntegerSliderControllerBuilder.create(option).range(0, 100).step(1)
                ))
                .build()
        );
    }
}