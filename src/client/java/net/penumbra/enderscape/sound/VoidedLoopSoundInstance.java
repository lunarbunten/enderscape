package net.penumbra.enderscape.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class VoidedLoopSoundInstance extends AbstractTickableSoundInstance {
    public final Consumer<VoidedLoopSoundInstance> remove;

    private final Minecraft minecraft;
    private float target;
    private int idleTicks;

    public VoidedLoopSoundInstance(Minecraft minecraft, float target, Consumer<VoidedLoopSoundInstance> remove) {
        super(EnderscapeEntitySounds.PLAYER_VOIDED_LOOP, SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.minecraft = minecraft;
        this.remove = remove;
        this.target = target;
        this.looping = true;

        volume = target;
        delay = 0;
    }

    @Override
    public void tick() {
        if (minecraft.level == null || minecraft.player == null || volume < 0.01F) {
            remove.accept(this);
            return;
        }

        if (idleTicks++ > 1) target = Mth.lerp(0.1F, target, 0.0F);

        volume = target;
    }

    public void setTarget(float value) {
        target = value;
        idleTicks = 0;
    }

    public void onRemove() {
        remove.accept(this);
    }

    public void remove() {
        stop();
    }
}
