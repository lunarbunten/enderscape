package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.structure.EnderscapeStructures;
import net.bunten.enderscape.sound.StructureMusic;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnderscapeStructureMusic {
    public static final List<ResourceKey<StructureMusic>> STRUCTURE_MUSIC = new ArrayList<>();

    public static final ResourceKey<StructureMusic> END_CITY = register("end_city");
    public static final ResourceKey<StructureMusic> STRONGHOLD = register("stronghold");

    public void bootstrap(BootstrapContext<StructureMusic> context) {
        register(context, END_CITY, EnderscapeMusic.STRUCTURE_END_CITY, BuiltinStructures.END_CITY.location(), Enderscape.id("end_city"));
        register(context, STRONGHOLD, EnderscapeMusic.STRUCTURE_STRONGHOLD, BuiltinStructures.STRONGHOLD.location(), EnderscapeStructures.STRONGHOLD.location());
    }

    private static void register(BootstrapContext<StructureMusic> context, ResourceKey<StructureMusic> key, Music music, ResourceLocation... locations) {
        context.register(key, new StructureMusic(music, Arrays.stream(locations).toList()));
    }

    private static ResourceKey<StructureMusic> register(String name) {
        ResourceKey<StructureMusic> key = ResourceKey.create(EnderscapeRegistries.STRUCTURE_MUSIC, Enderscape.id(name));
        STRUCTURE_MUSIC.add(key);
        return key;
    }
}