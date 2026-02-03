package net.bunten.enderscape.sound;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.List;

public record StructureMusic(Music music, List<ResourceLocation> permittedStructures) {
    public static final Codec<StructureMusic> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Music.CODEC.fieldOf("music").forGetter(StructureMusic::music),
                    Codec.list(ResourceLocation.CODEC).fieldOf("permitted_structures").forGetter(StructureMusic::permittedStructures)
            ).apply(instance, StructureMusic::new)
    );

    public static final Codec<Holder<StructureMusic>> CODEC = RegistryFixedCodec.create(EnderscapeRegistries.STRUCTURE_MUSIC);
}