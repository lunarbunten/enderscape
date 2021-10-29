package net.enderscape.mixin;

import com.mojang.serialization.Codec;
import net.enderscape.registry.EndBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.EndGatewayFeature;
import net.minecraft.world.gen.feature.EndGatewayFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndGatewayFeature.class)
public abstract class EndGatewayFeatureMixin extends Feature<EndGatewayFeatureConfig> {
    public EndGatewayFeatureMixin(Codec<EndGatewayFeatureConfig> configCodec) {
        super(configCodec);
    }
    @Inject(method = "generate", at = @At("TAIL"), cancellable = true)
    private void make(FeatureContext<EndGatewayFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        EndGatewayFeatureConfig endGatewayFeatureConfig = context.getConfig();

        for (BlockPos blockpos : BlockPos.iterate(pos.add(-1, -2, -1), pos.add(1, 2, 1))) {
            boolean flag = blockpos.getX() == pos.getX();
            boolean flag1 = blockpos.getY() == pos.getY();
            boolean flag2 = blockpos.getZ() == pos.getZ();
            boolean flag3 = Math.abs(blockpos.getY() - pos.getY()) == 2;
            if (flag && flag1 && flag2) {
                BlockPos blockPos1 = blockpos.toImmutable();
                world.setBlockState(blockPos1, Blocks.END_GATEWAY.getDefaultState(), 2);
                endGatewayFeatureConfig.getExitPos().ifPresent((blockPos2x) -> {
                    BlockEntity blockEntity = world.getBlockEntity(blockPos1);
                    if (blockEntity instanceof EndGatewayBlockEntity) {
                        EndGatewayBlockEntity endGatewayBlockEntity = (EndGatewayBlockEntity)blockEntity;
                        endGatewayBlockEntity.setExitPortalPos(blockPos2x, endGatewayFeatureConfig.isExact());
                        blockEntity.markDirty();
                    }

                });
            } else if (flag1) {
                world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
            } else if (flag3 && flag && flag2) {
                world.setBlockState(blockpos, EndBlocks.SHADOWSTEEL.getDefaultState(), 2);
            } else if ((flag || flag2) && !flag3) {
                world.setBlockState(blockpos, EndBlocks.SHADOWSTEEL.getDefaultState(), 2);
            } else {
                world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
            }
        }

        world.setBlockState(pos.down(3), EndBlocks.SMALL_SHADOWSTEEL_PILLAR.getDefaultState(), 2);
        world.setBlockState(pos.up(3), EndBlocks.SMALL_SHADOWSTEEL_PILLAR.getDefaultState(), 2);

    }
}
