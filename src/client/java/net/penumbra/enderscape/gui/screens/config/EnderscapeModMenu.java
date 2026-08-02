package net.penumbra.enderscape.gui.screens.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.config.EnderscapeConfig;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class EnderscapeModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return EnderscapeModMenu::buildMenu;
    }

    @Environment(EnvType.CLIENT)
    private static Screen buildMenu(Screen parent) {
        return YetAnotherConfigLib.create(EnderscapeConfig.HANDLER, EnderscapeModMenu::menuBuilder).generateScreen(parent);
    }

    private static YetAnotherConfigLib.Builder menuBuilder(EnderscapeConfig defaults, EnderscapeConfig config, YetAnotherConfigLib.Builder builder) {
        return builder
                .title(Component.translatable("screen.enderscape.config"))
                .category(EnderscapeBlockCategory.create(defaults, config))
                .category(EnderscapeEntityCategory.create(defaults, config))
                .category(EnderscapeItemCategory.create(defaults, config))
                .category(EnderscapeWorldCategory.create(defaults, config))
                .category(EnderscapeMiscCategory.create(defaults, config));
    }

    public static <T> Option<T> create(String name, T defaultValue, Supplier<T> getter, Consumer<T> setter, Function<Option<T>, ControllerBuilder<T>> controller) {
        return create(name, defaultValue, getter, setter, controller, false);
    }

    public static <T> Option<T> create(String name, T defaultValue, Supplier<T> getter, Consumer<T> setter, Function<Option<T>, ControllerBuilder<T>> controller, boolean hasPreview) {
        OptionDescription.Builder builder = OptionDescription.createBuilder().text(Component.translatable("screen.enderscape.config.option." + name + ".desc"));

        if (hasPreview) {
            builder.webpImage(Enderscape.id("textures/gui/previews/" + name + ".webp"));
        }

        return Option.<T>createBuilder()
                .name(Component.translatable("screen.enderscape.config.option." + name))
                .binding(defaultValue, getter, setter)
                .description(builder.build())
                .controller(controller)
                .build();
    }
}