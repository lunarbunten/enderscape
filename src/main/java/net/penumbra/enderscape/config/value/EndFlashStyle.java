package net.penumbra.enderscape.config.value;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum EndFlashStyle implements NameableEnum {
    VANILLA, IMPROVED, DISABLED;

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.enderscape.config.option.value." + name().toLowerCase());
    }
}