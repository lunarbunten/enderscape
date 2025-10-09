package net.bunten.enderscape.client.renderer;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum LightingStyle implements NameableEnum {
    VANILLA, IMPROVED, MIDNIGHT;

    @Override
    public Component getDisplayName() {
        return Component.translatable("option.enderscape.value." + name().toLowerCase());
    }
}