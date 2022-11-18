package net.bunten.enderscape.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;

public interface HasTickableSoundInstance {
    
    @Environment(value=EnvType.CLIENT)
    public AbstractTickableSoundInstance getTickableSoundInstance();
}