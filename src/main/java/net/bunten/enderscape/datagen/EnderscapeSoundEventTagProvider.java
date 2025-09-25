package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.tag.EnderscapeSoundEventTags.AMBIENCE_REPLACEABLE_BY_ENDERSCAPE;
import static net.minecraft.sounds.SoundEvents.*;

public class EnderscapeSoundEventTagProvider extends FabricTagProvider<SoundEvent> {

    public EnderscapeSoundEventTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.SOUND_EVENT, future);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(AMBIENCE_REPLACEABLE_BY_ENDERSCAPE).add(
                AMBIENT_CAVE.key(),
                AMBIENT_WARPED_FOREST_ADDITIONS.key(),
                AMBIENT_WARPED_FOREST_LOOP.key(),
                AMBIENT_WARPED_FOREST_MOOD.key()
        );
    }
}