package net.penumbra.enderscape.config.value;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum LightingStyle implements NameableEnum {
    VANILLA, IMPROVED, MIDNIGHT;

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.enderscape.config.option.value." + name().toLowerCase());
    }
}