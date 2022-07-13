package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.States;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.EndPortalFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

@Mixin(EndPortalFeature.class)
public abstract class EndPortalFeatureMixin extends Feature<DefaultFeatureConfig>  {
    public EndPortalFeatureMixin(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Final
    @Shadow
    private boolean open;

    @Inject(method = "generate", at = @At("TAIL"), cancellable = true)
    private void make(FeatureContext<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> info) {
        BlockPos pos = context.getOrigin().offset(Direction.DOWN);
        StructureWorldAccess world = context.getWorld();
        for (BlockPos blockpos : BlockPos.iterate(new BlockPos(pos.getX() - 4, pos.getY() - 1, pos.getZ() - 4), new BlockPos(pos.getX() + 4, pos.getY() + 32, pos.getZ() + 4))) {
            boolean flag = blockpos.isWithinDistance(pos, 2.5D);
            if (flag || blockpos.isWithinDistance(pos, 3.5D)) {
                if (blockpos.getY() < pos.getY()) {
                    if (flag) {
                        setBlockState(world, blockpos.up(), EnderscapeBlocks.SHADOW_STEEL_BLOCK.getDefaultState());
                    } else if (blockpos.getY() < pos.getY()) {
                        setBlockState(world, blockpos.up(), States.END_STONE);
                    }
                } else if (blockpos.getY() > pos.getY()) {
                    setBlockState(world, blockpos.up(), States.AIR);
                } else if (!flag) {
                    setBlockState(world, blockpos.up(), EnderscapeBlocks.SHADOW_STEEL_BLOCK.getDefaultState());
                } else if (open) {
                    setBlockState(world, blockpos.up(), Blocks.END_PORTAL.getDefaultState());
                } else {
                    setBlockState(world, blockpos.up(), States.AIR);
                }
            }
        }

        for (int i = 1; i < 6; i++) {
            if (i == 5) {
                setBlockState(world, pos.up(i), EnderscapeBlocks.CHISELED_SHADOW_STEEL.getDefaultState());
            } else {
                setBlockState(world, pos.up(i), EnderscapeBlocks.SHADOW_STEEL_PILLAR.getDefaultState());
            }
        }

        setBlockState(world, pos.up(2).north(2).east(2), EnderscapeBlocks.SMALL_SHADOW_STEEL_PILLAR.getDefaultState());
        setBlockState(world, pos.up(2).north(2).west(2), EnderscapeBlocks.SMALL_SHADOW_STEEL_PILLAR.getDefaultState());
        setBlockState(world, pos.up(2).south(2).east(2), EnderscapeBlocks.SMALL_SHADOW_STEEL_PILLAR.getDefaultState());
        setBlockState(world, pos.up(2).south(2).west(2), EnderscapeBlocks.SMALL_SHADOW_STEEL_PILLAR.getDefaultState());
        setBlockState(world, pos.up(3).north(2).east(2), EnderscapeBlocks.CHISELED_SHADOW_STEEL.getDefaultState());
        setBlockState(world, pos.up(3).north(2).west(2), EnderscapeBlocks.CHISELED_SHADOW_STEEL.getDefaultState());
        setBlockState(world, pos.up(3).south(2).east(2), EnderscapeBlocks.CHISELED_SHADOW_STEEL.getDefaultState());
        setBlockState(world, pos.up(3).south(2).west(2), EnderscapeBlocks.CHISELED_SHADOW_STEEL.getDefaultState());

        for (int i = 2; i < 6; i++) {
            placeholder(world, pos, Direction.byId(i));
        }

        info.setReturnValue(true);
        info.cancel();
    }

    private void placeholder(StructureWorldAccess world, BlockPos pos, Direction direction) {
        setBlockState(world, pos.offset(direction, 3).offset(direction.rotateYClockwise()).up(), EnderscapeBlocks.SHADOW_STEEL_BLOCK.getDefaultState());
        setBlockState(world, pos.offset(direction, 3).up(), EnderscapeBlocks.SHADOW_STEEL_PILLAR.getDefaultState().with(Properties.AXIS, direction.rotateYClockwise().getAxis()));
        setBlockState(world, pos.offset(direction, 3).offset(direction.rotateYCounterclockwise()).up(), EnderscapeBlocks.SHADOW_STEEL_BLOCK.getDefaultState());
    }
}
