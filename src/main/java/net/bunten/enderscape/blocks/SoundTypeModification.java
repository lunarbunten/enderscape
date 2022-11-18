package net.bunten.enderscape.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.bunten.enderscape.registry.EnderscapeModifications;
import net.minecraft.world.level.block.SoundType;

public class SoundTypeModification {
    private final SoundType soundType;
    private final List<String> list;
    private final Predicate<SoundType> predicate;

    public SoundTypeModification(SoundType soundType, Predicate<SoundType> predicate) {
        this.soundType = soundType;
        this.predicate = predicate;
        this.list = new ArrayList<>();

        EnderscapeModifications.MODIFICATIONS.add(this);
    }

    public SoundType getSoundType() {
        return soundType;
    }

    public List<String> getBlocks() {
        return list;
    }

    public boolean applies() {
        return predicate.test(soundType);
    }

    public void add(String string) {
        list.add(string);
    }
}