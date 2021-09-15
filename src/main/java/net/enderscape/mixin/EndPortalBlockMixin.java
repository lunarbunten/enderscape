package net.enderscape.mixin;

import net.enderscape.registry.EndSounds;
import net.enderscape.util.EndMath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EndPortalBlock.class)
public abstract class EndPortalBlockMixin extends BlockWithEntity {
    public EndPortalBlockMixin(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    @Inject(at = @At("HEAD"), method = "randomDisplayTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V", cancellable = true)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo info) {
        if (random.nextInt(50) == 0) {
            world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, EndSounds.BLOCK_END_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.3F, EndMath.nextFloat(random, 0.8F, 1.2F), false);
        }
    }
}