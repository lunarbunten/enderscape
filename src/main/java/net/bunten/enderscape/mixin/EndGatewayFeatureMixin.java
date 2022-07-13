package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.States;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.EndGatewayFeature;
import net.minecraft.world.gen.feature.EndGatewayFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

@Mixin(EndGatewayFeature.class)
public abstract class EndGatewayFeatureMixin extends Feature<EndGatewayFeatureConfig> {
    public EndGatewayFeatureMixin(Codec<EndGatewayFeatureConfig> configCodec) {
        super(configCodec);
    }
    
    @Inject(method = "generate", at = @At("TAIL"), cancellable = true)
    private void make(FeatureContext<EndGatewayFeatureConfig> context, CallbackInfoReturnable<Boolean> info) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        EndGatewayFeatureConfig config = context.getConfig();

        for (BlockPos pos2 : BlockPos.iterate(pos.add(-1, -2, -1), pos.add(1, 2, 1))) {
            boolean flag = pos2.getX() == pos.getX();
            boolean flag1 = pos2.getY() == pos.getY();
            boolean flag2 = pos2.getZ() == pos.getZ();
            boolean flag3 = Math.abs(pos2.getY() - pos.getY()) == 2;
            
            if (flag && flag1 && flag2) {
                BlockPos pos3 = pos2.toImmutable();
                world.setBlockState(pos3, Blocks.END_GATEWAY.getDefaultState(), 2);
                config.getExitPos().ifPresent((pos2x) -> {
                    BlockEntity blockEntity = world.getBlockEntity(pos3);
                    if (blockEntity instanceof EndGatewayBlockEntity gateway) {
                        gateway.setExitPortalPos(pos2x, config.isExact());
                        blockEntity.markDirty();
                    }
                });
            } else if (flag1) {
                world.setBlockState(pos2, States.AIR, 2);
            } else if (flag3 && flag && flag2) {
                world.setBlockState(pos2, EnderscapeBlocks.SHADOW_STEEL_BLOCK.getDefaultState(), 2);
            } else if ((flag || flag2) && !flag3) {
                world.setBlockState(pos2, EnderscapeBlocks.SHADOW_STEEL_BLOCK.getDefaultState(), 2);
            } else {
                world.setBlockState(pos2, States.AIR, 2);
            }
        }

        world.setBlockState(pos.down(3), EnderscapeBlocks.SMALL_SHADOW_STEEL_PILLAR.getDefaultState(), 2);
        world.setBlockState(pos.up(3), EnderscapeBlocks.SMALL_SHADOW_STEEL_PILLAR.getDefaultState(), 2);
    }
}
