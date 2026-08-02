package net.penumbra.enderscape.mixin.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.penumbra.enderscape.block.AbstractOvergrowthBlock;
import net.penumbra.enderscape.block.CelestialChanterelleBlock;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.penumbra.enderscape.block.DirectionalVegetationBlock.getFacing;

@Mixin(BoneMealItem.class)
public abstract class BoneMealItemMixin extends Item {

    public BoneMealItemMixin(Properties properties) {
        super(properties);
    }

    @ModifyVariable(method = "addGrowthParticles", at = @At("STORE"), name = "particlePos")
    private static BlockPos modifyBlockPos(BlockPos original, LevelAccessor level, BlockPos pos, int i) {
        if (level.getBlockState(pos).getBlock() instanceof AbstractOvergrowthBlock) return pos.relative(getFacing(level.getBlockState(pos)));
        return original;
    }

    @ModifyArgs(method = "addGrowthParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ParticleUtils;spawnParticleInBlock(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;ILnet/minecraft/core/particles/ParticleOptions;)V"))
    private static void Enderscape$changeGrowthParticles(Args args) {
        LevelAccessor level = args.get(0);
        BlockPos pos = args.get(1);

        if (level.getBlockState(pos).getBlock() instanceof CelestialChanterelleBlock) {
            if (level.getBlockState(pos.below()).is(EnderscapeBlockTags.CELESTIAL_CORRUPTS_ON)) args.set(3, EnderscapeParticles.VOID_POOF);
        }
    }
}