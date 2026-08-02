package net.penumbra.enderscape.gui.screens.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;
import net.penumbra.enderscape.config.EnderscapeConfig;

public class EnderscapeEntityCategory {

    public static ConfigCategory create(EnderscapeConfig defaults, EnderscapeConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder().name(Component.translatable("screen.enderscape.config.category.entity"));

        silverfish(defaults, config, category);
        endermite(defaults, config, category);
        enderman(defaults, config, category);
        shulker(defaults, config, category);
        misc(defaults, config, category);

        return category.build();
    }

    public static void silverfish(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("entity.minecraft.silverfish"))

                .option(EnderscapeModMenu.create(
                        "silverfish_expand_hit_range",
                        defaults.silverfishExpandHitRange,
                        () -> config.silverfishExpandHitRange,
                        value -> config.silverfishExpandHitRange = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "silverfish_natural_spawns_obey_light_level",
                        defaults.silverfishNaturalSpawnsObeyLightLevel,
                        () -> config.silverfishNaturalSpawnsObeyLightLevel,
                        value -> config.silverfishNaturalSpawnsObeyLightLevel = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "silverfish_delay_before_infesting_stone",
                        defaults.silverfishDelayBeforeInfestingStone,
                        () -> config.silverfishDelayBeforeInfestingStone,
                        value -> config.silverfishDelayBeforeInfestingStone = value,
                        TickBoxControllerBuilder::create
                ))
                .build()
        );
    }

    public static void endermite(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("entity.minecraft.endermite"))

                .option(EnderscapeModMenu.create(
                        "endermite_expand_hit_range",
                        defaults.endermiteExpandHitRange,
                        () -> config.endermiteExpandHitRange,
                        value -> config.endermiteExpandHitRange = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "endermite_natural_spawns_obey_light_level",
                        defaults.endermiteNaturalSpawnsObeyLightLevel,
                        () -> config.endermiteNaturalSpawnsObeyLightLevel,
                        value -> config.endermiteNaturalSpawnsObeyLightLevel = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "endermite_emissive_eyes",
                        defaults.endermiteEmissiveEyes,
                        () -> config.endermiteEmissiveEyes,
                        value -> config.endermiteEmissiveEyes = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "endermite_update_sounds",
                        defaults.endermiteUpdateSounds,
                        () -> config.endermiteUpdateSounds,
                        value -> config.endermiteUpdateSounds = value,
                        TickBoxControllerBuilder::create
                ))
                .build()
        );
    }

    public static void enderman(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("entity.minecraft.enderman"))

                .option(EnderscapeModMenu.create(
                        "enderman_anger_overlay",
                        defaults.endermanAngerOverlay,
                        () -> config.endermanAngerOverlay,
                        value -> config.endermanAngerOverlay = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "enderman_stereo_stare_sound",
                        defaults.endermanStereoStareSound,
                        () -> config.endermanStereoStareSound,
                        value -> config.endermanStereoStareSound = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "enderman_update_renderer",
                        defaults.endermanUpdateRenderer,
                        () -> config.endermanUpdateRenderer,
                        value -> config.endermanUpdateRenderer = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "enderman_light_sensitive_eyes",
                        defaults.endermanLightSensitiveEyes,
                        () -> config.endermanLightSensitiveEyes,
                        value -> config.endermanLightSensitiveEyes = value,
                        TickBoxControllerBuilder::create,
                        true
                ))
                .build()
        );
    }

    public static void shulker(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("entity.minecraft.shulker"))

                .option(EnderscapeModMenu.create(
                        "shulker_hurt_by_piercing",
                        defaults.shulkerHurtByPiercing,
                        () -> config.shulkerHurtByPiercing,
                        value -> config.shulkerHurtByPiercing = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "shulker_bullet_enforce_owner_limit",
                        defaults.shulkerBulletEnforceOwnerLimit,
                        () -> config.shulkerBulletEnforceOwnerLimit,
                        value -> config.shulkerBulletEnforceOwnerLimit = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "shulker_bullet_enforce_count_limit",
                        defaults.shulkerBulletEnforceCountLimit,
                        () -> config.shulkerBulletEnforceCountLimit,
                        value -> config.shulkerBulletEnforceCountLimit = value,
                        option -> IntegerSliderControllerBuilder.create(option).range(0, 8).step(1)
                ))
                .option(EnderscapeModMenu.create(
                        "shulker_bullet_enforce_distance_limit",
                        defaults.shulkerBulletEnforceDistanceLimit,
                        () -> config.shulkerBulletEnforceDistanceLimit,
                        value -> config.shulkerBulletEnforceDistanceLimit = value,
                        option -> IntegerSliderControllerBuilder.create(option).range(0, 100).step(1)
                ))
                .option(EnderscapeModMenu.create(
                        "shulker_bullet_enforce_time_limit",
                        defaults.shulkerBulletEnforceTimeLimit,
                        () -> config.shulkerBulletEnforceTimeLimit,
                        value -> config.shulkerBulletEnforceTimeLimit = value,
                        option -> IntegerSliderControllerBuilder.create(option).range(0, 60).step(1)
                ))
                .option(EnderscapeModMenu.create(
                        "shulker_bullet_rebalance_levitation",
                        defaults.shulkerBulletRebalanceLevitation,
                        () -> config.shulkerBulletRebalanceLevitation,
                        value -> config.shulkerBulletRebalanceLevitation = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "shulker_bullet_loop_sound",
                        defaults.shulkerBulletLoopSound,
                        () -> config.shulkerBulletLoopSound,
                        value -> config.shulkerBulletLoopSound = value,
                        TickBoxControllerBuilder::create
                ))
                .build()
        );
    }

    public static void misc(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("key.category.minecraft.misc"))

                .option(EnderscapeModMenu.create(
                        "rubblemite_expand_hit_range",
                        defaults.rubblemiteExpandHitRange,
                        () -> config.rubblemiteExpandHitRange,
                        value -> config.rubblemiteExpandHitRange = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "portal_particle_emissive",
                        defaults.portalParticleEmissive,
                        () -> config.portalParticleEmissive,
                        value -> config.portalParticleEmissive = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "entity_update_portal_particles",
                        defaults.entityUpdatePortalParticles,
                        () -> config.entityUpdatePortalParticles,
                        value -> config.entityUpdatePortalParticles = value,
                        TickBoxControllerBuilder::create
                ))
                .option(EnderscapeModMenu.create(
                        "void_poof_particles_upon_death",
                        defaults.voidPoofParticlesUponDeath,
                        () -> config.voidPoofParticlesUponDeath,
                        value -> config.voidPoofParticlesUponDeath = value,
                        TickBoxControllerBuilder::create
                ))
                .build()
        );
    }
}
