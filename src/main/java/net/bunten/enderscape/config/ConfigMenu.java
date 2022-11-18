package net.bunten.enderscape.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.betterx.bclib.config.ConfigKeeper.BooleanEntry;
import org.betterx.bclib.config.ConfigKeeper.FloatEntry;
import org.betterx.bclib.config.ConfigKeeper.IntegerEntry;
import org.betterx.bclib.config.NamedPathConfig;
import org.betterx.bclib.config.NamedPathConfig.ConfigTokenDescription;
import org.betterx.bclib.config.NamedPathConfig.DependendConfigToken;
import org.betterx.ui.layout.components.Checkbox;
import org.betterx.ui.layout.components.HorizontalStack;
import org.betterx.ui.layout.components.LayoutComponent;
import org.betterx.ui.layout.components.VerticalStack;
import org.betterx.ui.vanilla.LayoutScreen;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.config.basic.BasicConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigMenu extends LayoutScreen {
    private final Map<Checkbox, Supplier<Boolean>> dependentWidgets = new HashMap<>();

    public ConfigMenu(@Nullable Screen parent) {
        super(parent, Component.translatable("menu." + Enderscape.MOD_ID + ".config.title"), 10, 10, 20);
    }

    protected <T> Component getPath(NamedPathConfig config, ConfigTokenDescription<T> option) {
        return Component.translatable("menu." + Enderscape.MOD_ID + ".config" + option.getPath());
    }

    protected void updateEnabledState() {
        dependentWidgets.forEach((cb, supl) -> cb.setEnabled(supl.get()));
    }

    @Override
    protected LayoutComponent<?, ?> initContent() {
        VerticalStack content = new VerticalStack(fit(), fit()).setDebugName("content");

        int i = 0;
        for (NamedPathConfig config : Config.CONFIGS) {
            if (config instanceof BasicConfig basic) {
                if (!basic.showInUI(Minecraft.getInstance())) continue;

                content.addText(fit(), fit(), basic.getComponent()).setColor(0xFEFE9F);
                content.addSpacer(8);

                basic.getAllOptions()
                .stream()
                .filter(option -> !option.hidden)
                .forEach(option -> addRow(content, basic, option));

                content.addSpacer(12);
                i++;
            }
        }

        if (i == 0) {
            content.centerHorizontal().centerVertical();
            content.addText(fit(), fit(), Component.translatable("menu." + Enderscape.MOD_ID + ".config.empty1")).setColor(0xFC9FF3).centerHorizontal();
            content.addSpacer(4);
            content.addText(fit(), fit(), Component.translatable("menu." + Enderscape.MOD_ID + ".config.empty2")).setColor(0xFC9FF3).centerHorizontal();
        }

        VerticalStack grid = new VerticalStack(fill(), fill()).setDebugName("main grid");
        
        grid.addScrollable(content);
        grid.addSpacer(8);

        grid.addButton(fixed(200), fit(), CommonComponents.GUI_DONE).onPress((button) -> {
            onClose();
        }).centerHorizontal();

        return grid;
    }

    protected <T> void addRow(VerticalStack grid, NamedPathConfig config, ConfigTokenDescription<T> option) {
        addOption(grid, config, option);
        grid.addSpacer(2);
    }

    @SuppressWarnings("unchecked")
    protected void addOption(VerticalStack grid, NamedPathConfig config, ConfigTokenDescription<?> option) {
        HorizontalStack row = grid.addRow();

        if (option.topPadding > 0) {
            grid.addSpacer(option.topPadding);
        }

        if (option.leftPadding > 0) {
            row.addSpacer(option.leftPadding);
        }

        Class<?> type = option.token.type;
        if (type.isAssignableFrom(IntegerEntry.class)) {
            ConfigTokenDescription<Integer> option2 = (ConfigTokenDescription<Integer>) option;

            row.addRange(
                    fixed(200), fit(),
                    getPath(config, option2),
                    option2.minRange,
                    option2.maxRange,
                    config.getRaw(option2.token)).onChange(
                            (caller, state) -> {
                                config.set(option2.token, state);
                            });
        }

        if (type.isAssignableFrom(FloatEntry.class)) {
            ConfigTokenDescription<Float> option2 = (ConfigTokenDescription<Float>) option;

            row.addRange(
                    fixed(200), fit(),
                    getPath(config, option),
                    option2.minRange,
                    option2.maxRange,
                    config.getRaw(option2.token)).onChange(
                            (caller, state) -> {
                                config.set(option2.token, state);
                            });
        }

        if (type.isAssignableFrom(BooleanEntry.class)) {
            ConfigTokenDescription<Boolean> option2 = (ConfigTokenDescription<Boolean>) option;

            Checkbox checkbox = row.addCheckbox(
                    fit(), fit(),
                    getPath(config, option),
                    config.getRaw(option2.token)).onChange(
                            (caller, state) -> {
                                config.set(option2.token, state);
                                updateEnabledState();
                            });

            if (option2.token instanceof DependendConfigToken) {
                dependentWidgets.put(checkbox, () -> option.token.dependenciesTrue(config));
                checkbox.setEnabled(option.token.dependenciesTrue(config));
            }
        }
    }

    @Override
    public void onClose() {
        Config.save();
        super.onClose();
    }

    @Override
    protected void renderBackground(PoseStack stack, int i, int j, float f) {
        super.renderBackground(stack, i);
    }
}