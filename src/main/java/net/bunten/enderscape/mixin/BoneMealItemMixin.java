package net.bunten.enderscape.mixin;

import net.bunten.enderscape.block.AbstractOvergrowthBlock;
import net.bunten.enderscape.block.CelestialChanterelleBlock;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.bunten.enderscape.block.DirectionalPlantBlock.getFacing;

@Mixin(BoneMealItem.class)
public abstract class BoneMealItemMixin extends Item {

    public BoneMealItemMixin(Properties properties) {
        super(properties);
    }

    @ModifyVariable(method = "addGrowthParticles", at = @At("STORE"), ordinal = 1)
    private static BlockPos modifyBlockPos(BlockPos original, LevelAccessor level, BlockPos pos, int i) {
        if (level.getBlockState(pos).getBlock() instanceof AbstractOvergrowthBlock) return pos.relative(getFacing(level.getBlockState(pos)));
        return original;
    }

    @ModifyArgs(method = "addGrowthParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ParticleUtils;spawnParticleInBlock(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;ILnet/minecraft/core/particles/ParticleOptions;)V"))
    private static void Enderscape$changeGrowthParticles(Args args) {
        LevelAccessor level = args.get(0);
        BlockPos pos = args.get(1);

        if (level.getBlockState(pos).getBlock() instanceof CelestialChanterelleBlock) {
            BlockState relative = level.getBlockState(pos.relative(getFacing(level.getBlockState(pos)).getOpposite()));
            if (relative.is(EnderscapeBlockTags.CELESTIAL_CORRUPTS_ON)) args.set(3, EnderscapeParticles.VOID_POOF);
        }
    }
}