package net.penumbra.enderscape.gui.screens.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;
import net.penumbra.enderscape.config.EnderscapeConfig;

public class EnderscapeBlockCategory {

    public static ConfigCategory create(EnderscapeConfig defaults, EnderscapeConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder().name(Component.translatable("screen.enderscape.config.category.block"));

        soundTypes(defaults, config, category);
        misc(defaults, config, category);

        return category.build();
    }

    public static void soundTypes(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("screen.enderscape.config.group.sound_type"))

                .option(EnderscapeModMenu.create(
                        "update_end_portal_frame_sound_type",
                        defaults.updateEndPortalFrameSoundType,
                        () -> config.updateEndPortalFrameSoundType,
                        value -> config.updateEndPortalFrameSoundType = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "update_end_portal_sound_type",
                        defaults.updateEndPortalSoundType,
                        () -> config.updateEndPortalSoundType,
                        value -> config.updateEndPortalSoundType = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "update_end_stone_sound_type",
                        defaults.updateEndStoneSoundType,
                        () -> config.updateEndStoneSoundType,
                        value -> config.updateEndStoneSoundType = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "update_end_stone_brick_sound_type",
                        defaults.updateEndStoneBrickSoundType,
                        () -> config.updateEndStoneBrickSoundType,
                        value -> config.updateEndStoneBrickSoundType = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "update_chorus_sound_type",
                        defaults.updateChorusSoundType,
                        () -> config.updateChorusSoundType,
                        value -> config.updateChorusSoundType = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "update_purpur_sound_type",
                        defaults.updatePurpurSoundType,
                        () -> config.updatePurpurSoundType,
                        value -> config.updatePurpurSoundType = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "update_end_rod_sound_type",
                        defaults.updateEndRodSoundType,
                        () -> config.updateEndRodSoundType,
                        value -> config.updateEndRodSoundType = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "update_shulker_box_sound_type",
                        defaults.updateShulkerBoxSoundType,
                        () -> config.updateShulkerBoxSoundType,
                        value -> config.updateShulkerBoxSoundType = value,
                        TickBoxControllerBuilder::create
                ))
                .build()
        );
    }

    public static void misc(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("key.category.minecraft.misc"))

                .option(EnderscapeModMenu.create(
                        "chorus_flower_humming",
                        defaults.chorusFlowerHumming,
                        () -> config.chorusFlowerHumming,
                        value -> config.chorusFlowerHumming = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "chorus_flower_pollen",
                        defaults.chorusFlowerPollen,
                        () -> config.chorusFlowerPollen,
                        value -> config.chorusFlowerPollen = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "end_portal_update_particles",
                        defaults.endPortalUpdateParticles,
                        () -> config.endPortalUpdateParticles,
                        value -> config.endPortalUpdateParticles = value,
                        TickBoxControllerBuilder::create,
                        true
                ))
                .option(EnderscapeModMenu.create(
                        "end_portal_update_travel_sound",
                        defaults.endPortalUpdateTravelSound,
                        () -> config.endPortalUpdateTravelSound,
                        value -> config.endPortalUpdateTravelSound = value,
                        TickBoxControllerBuilder::create
                ))

                .build()
        );
    }
}
