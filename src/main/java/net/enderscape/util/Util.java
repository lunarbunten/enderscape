package net.enderscape.util;

import java.util.Objects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;

public class Util extends net.minecraft.util.Util {

    public static Biome getBiome(WorldAccess world, BlockPos pos) {
        return world.getBiome(pos);
    }

    public static String getBiomeID(WorldAccess world, BlockPos pos) {
        return Objects.requireNonNull(world.getRegistryManager().get(Registry.BIOME_KEY).getId(getBiome(world, pos))).toString();
    }

    public static boolean matchesBiome(String string, LivingEntity entity) {
        return string.equals(getBiomeID(entity.world, entity.getBlockPos()));
    }

    public static boolean matchesBiome(String string, WorldAccess world, BlockPos pos) {
        return string.equals(getBiomeID(world, pos));
    }

    public static void playSound(World world, BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, category, volume, pitch);
    }

    public static void playSound(Entity entity, SoundEvent sound, float volume, float pitch) {
        entity.getEntityWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, entity.getSoundCategory(), volume, pitch);
    }
}