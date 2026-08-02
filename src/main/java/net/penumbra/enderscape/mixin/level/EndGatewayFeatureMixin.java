package net.penumbra.enderscape.mixin.level;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.levelgen.feature.EndGatewayFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

@Mixin(EndGatewayFeature.class)
public abstract class EndGatewayFeatureMixin extends Feature<EndGatewayConfiguration> {

    public EndGatewayFeatureMixin(Codec<EndGatewayConfiguration> codec) {
        super(codec);
    }

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    public void Enderscape$place(FeaturePlaceContext<EndGatewayConfiguration> context, CallbackInfoReturnable<Boolean> info) {
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        EndGatewayConfiguration config = context.config();

        setBlock(level, origin, Blocks.END_GATEWAY.defaultBlockState());
        config.getExit().ifPresent(exitPos -> {
            if (level.getBlockEntity(origin) instanceof TheEndGatewayBlockEntity gateway) gateway.setExitPosition(exitPos, config.isExitExact());
        });

        setBlock(level, origin.above(), Blocks.BEDROCK.defaultBlockState());
        setBlock(level, origin.below(), Blocks.BEDROCK.defaultBlockState());

        for (Direction dir : Direction.values()) {
            if (dir.getAxis() == Direction.Axis.Y) continue;
            setBlock(level, origin.above().relative(dir), EnderscapeBlocks.CUT_SHADOLINE_STAIRS.defaultBlockState().setValue(HALF, Half.BOTTOM).setValue(STAIRS_SHAPE, StairsShape.STRAIGHT).setValue(HorizontalDirectionalBlock.FACING, dir.getOpposite()));
            setBlock(level, origin.below().relative(dir), EnderscapeBlocks.CUT_SHADOLINE_STAIRS.defaultBlockState().setValue(HALF, Half.TOP).setValue(STAIRS_SHAPE, StairsShape.STRAIGHT).setValue(HorizontalDirectionalBlock.FACING, dir.getOpposite()));
        }

        setBlock(level, origin.above(2), EnderscapeBlocks.CHISELED_SHADOLINE.defaultBlockState());
        setBlock(level, origin.below(2), EnderscapeBlocks.CHISELED_SHADOLINE.defaultBlockState());

        setBlock(level, origin.above(3), EnderscapeBlocks.SHADOLINE_BLOCK_WALL.defaultBlockState().setValue(EAST_WALL, WallSide.NONE).setValue(NORTH_WALL, WallSide.NONE).setValue(SOUTH_WALL, WallSide.NONE).setValue(WEST_WALL, WallSide.NONE).setValue(UP, Boolean.TRUE));
        setBlock(level, origin.below(3), EnderscapeBlocks.SHADOLINE_BLOCK_WALL.defaultBlockState().setValue(EAST_WALL, WallSide.NONE).setValue(NORTH_WALL, WallSide.NONE).setValue(SOUTH_WALL, WallSide.NONE).setValue(WEST_WALL, WallSide.NONE).setValue(UP, Boolean.TRUE));

        info.setReturnValue(true);
    }
}
