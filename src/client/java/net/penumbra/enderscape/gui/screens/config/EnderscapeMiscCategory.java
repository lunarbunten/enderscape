package net.penumbra.enderscape.gui.screens.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.registry.EnderscapePacks;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EnderscapeMiscCategory {

    public static ConfigCategory create(EnderscapeConfig defaults, EnderscapeConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder().name(Component.translatable("key.category.minecraft.misc"));

        packs(defaults, config, category);
        safety(defaults, config, category);

        return category.build();
    }

    public static <T> Option<T> defaultPackOption(Identifier identifier, T defaultValue, Supplier<T> getter, Consumer<T> setter, Function<Option<T>, ControllerBuilder<T>> controller) {
        Component packTitle = Component.translatable(String.format("pack.%s.%s", identifier.getNamespace(), identifier.getPath()));

        return Option.<T>createBuilder()
                .name(Component.translatable("screen.enderscape.config.option.default_pack", packTitle))
                .binding(defaultValue, getter, setter)
                .description(OptionDescription.createBuilder().text(Component.translatable("screen.enderscape.config.option.default_pack.desc")).build())
                .controller(controller)
                .build();
    }

    public static void packs(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("screen.enderscape.config.group.data_resource_packs"))

                .option(defaultPackOption(
                        EnderscapePacks.FIX_LEVITATION_ADVANCEMENT,
                        defaults.defaultDataPackFixLevitationAdvancement,
                        () -> config.defaultDataPackFixLevitationAdvancement,
                        value -> config.defaultDataPackFixLevitationAdvancement = value,
                        TickBoxControllerBuilder::create
                ))
                .option(defaultPackOption(
                        EnderscapePacks.FIX_VANILLA_RECIPES,
                        defaults.defaultDataPackFixVanillaRecipes,
                        () -> config.defaultDataPackFixVanillaRecipes,
                        value -> config.defaultDataPackFixVanillaRecipes = value,
                        TickBoxControllerBuilder::create
                ))
                .option(defaultPackOption(
                        EnderscapePacks.NEW_END_CITIES,
                        defaults.defaultDataPackNewEndCities,
                        () -> config.defaultDataPackNewEndCities,
                        value -> config.defaultDataPackNewEndCities = value,
                        TickBoxControllerBuilder::create
                ))
                .option(defaultPackOption(
                        EnderscapePacks.NEW_STRONGHOLDS,
                        defaults.defaultDataPackNewStrongholds,
                        () -> config.defaultDataPackNewStrongholds,
                        value -> config.defaultDataPackNewStrongholds = value,
                        TickBoxControllerBuilder::create
                ))
                .option(defaultPackOption(
                        EnderscapePacks.NEW_TERRAIN,
                        defaults.defaultDataPackNewTerrain,
                        () -> config.defaultDataPackNewTerrain,
                        value -> config.defaultDataPackNewTerrain = value,
                        TickBoxControllerBuilder::create
                ))
                .option(defaultPackOption(
                        EnderscapePacks.IMPROVED_VISUALS,
                        defaults.defaultResourcePackImprovedVisuals,
                        () -> config.defaultResourcePackImprovedVisuals,
                        value -> config.defaultResourcePackImprovedVisuals = value,
                        TickBoxControllerBuilder::create
                ))
                .build()
        );
    }

    public static void safety(EnderscapeConfig defaults, EnderscapeConfig config, ConfigCategory.Builder category) {
        category.group(OptionGroup.createBuilder()
                .name(Component.translatable("screen.enderscape.config.group.safety"))

                .option(EnderscapeModMenu.create(
                        "edit_world_enderscape_data_packs_button",
                        defaults.editWorldEnderscapeDataPacksButton,
                        () -> config.editWorldEnderscapeDataPacksButton,
                        value -> config.editWorldEnderscapeDataPacksButton = value,
                        TickBoxControllerBuilder::create,
                        true
                ))
                .option(EnderscapeModMenu.create(
                        "vanilla_world_warning",
                        defaults.vanillaWorldWarning,
                        () -> config.vanillaWorldWarning,
                        value -> config.vanillaWorldWarning = value,
                        TickBoxControllerBuilder::create,
                        true
                ))

                .build()
        );
    }
}
