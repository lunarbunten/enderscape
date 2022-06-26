package net.bunten.enderscape.util;

import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Util extends net.minecraft.util.Util {

    public static void playSound(World world, BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, category, volume, pitch);
    }

    public static void playSound(Entity entity, SoundEvent sound, float volume, float pitch) {
        entity.getEntityWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, entity.getSoundCategory(), volume, pitch);
    }

    public static void playPlaceSound(World world, BlockPos pos, BlockSoundGroup group) {
        world.playSound(null, pos, group.getPlaceSound(), SoundCategory.BLOCKS, (group.getVolume() + 1) / 2, group.getPitch() * 0.8F);
    }
}