package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/*
 * Chorus Humming and Pollen
 */
@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerBlockMixin extends Block {
    public ChorusFlowerBlockMixin(Properties settings) {
        super(settings);
    }

    @Shadow
    @Final
    public static IntegerProperty AGE;

    @Override
    @Unique
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(AGE) == 5) {
            if (random.nextFloat() < 0.7F && Config.AMBIENCE.chorusFlowerPollen()) {
                world.addParticle(EnderscapeParticles.CHORUS_POLLEN, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0.25, random.nextGaussian() * 0.025, 0.25);
            }
            if (random.nextInt(18) == 0 && Config.AMBIENCE.chorusFlowerHumming()) {
                world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, EnderscapeSounds.CHORUS_FLOWER_HUM, SoundSource.AMBIENT, 0.15F + random.nextFloat(), random.nextFloat() + 0.3F, false);
            }
        }
    }
}