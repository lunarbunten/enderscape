package net.penumbra.enderscape.sound;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.attribute.BackgroundMusic;
import net.penumbra.enderscape.registry.EnderscapeRegistries;

import java.util.List;

public record StructureMusic(BackgroundMusic music, List<Identifier> permittedStructures) {
    public static final Codec<StructureMusic> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BackgroundMusic.CODEC.fieldOf("music").forGetter(StructureMusic::music),
                    Codec.list(Identifier.CODEC).fieldOf("permitted_structures").forGetter(StructureMusic::permittedStructures)
            ).apply(instance, StructureMusic::new)
    );

    public static final Codec<Holder<StructureMusic>> CODEC = RegistryFixedCodec.create(EnderscapeRegistries.STRUCTURE_MUSIC);
}