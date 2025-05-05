package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour {
    public BlockMixin(Properties settings) {
        super(settings);
    }

    /*
     * Chorus Humming and Pollen
     */
    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    public void Enderscape$animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo info) {
        if (state.getBlock() instanceof ChorusFlowerBlock && state.getValue(ChorusFlowerBlock.AGE) == 5) {
            info.cancel();
            if (EnderscapeConfig.getInstance().chorusFlowerPollen && random.nextFloat() < 0.7F) {
                level.addParticle(EnderscapeParticles.CHORUS_POLLEN, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0.25, random.nextGaussian() * 0.025, 0.25);
            }
            if (EnderscapeConfig.getInstance().chorusFlowerHumming && random.nextInt(18) == 0) {
                level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, EnderscapeBlockSounds.CHORUS_FLOWER_IDLE, SoundSource.AMBIENT, 0.15F + random.nextFloat(), random.nextFloat() + 0.3F, false);
            }
        }
    }
}