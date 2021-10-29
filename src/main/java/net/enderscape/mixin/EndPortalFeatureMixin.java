package net.enderscape.mixin;

import com.mojang.serialization.Codec;
import net.enderscape.registry.EndBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.EndPortalFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndPortalFeature.class)
public abstract class EndPortalFeatureMixin extends Feature<DefaultFeatureConfig>  {
    public EndPortalFeatureMixin(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Final
    @Shadow
    private boolean open;

    @Inject(method = "generate", at = @At("TAIL"), cancellable = true)
    private void make(FeatureContext<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        BlockPos pos = context.getOrigin().offset(Direction.DOWN);
        StructureWorldAccess world = context.getWorld();
        for (BlockPos blockpos : BlockPos.iterate(new BlockPos(pos.getX() - 4, pos.getY() - 1, pos.getZ() - 4), new BlockPos(pos.getX() + 4, pos.getY() + 32, pos.getZ() + 4))) {
            boolean flag = blockpos.isWithinDistance(pos, 2.5D);
            if (flag || blockpos.isWithinDistance(pos, 3.5D)) {
                if (blockpos.getY() < pos.getY()) {
                    if (flag) {
                        this.setBlockState(world, blockpos.up(), EndBlocks.SHADOWSTEEL.getDefaultState());
                    } else if (blockpos.getY() < pos.getY()) {
                        this.setBlockState(world, blockpos.up(), Blocks.END_STONE.getDefaultState());
                    }
                } else if (blockpos.getY() > pos.getY()) {
                    this.setBlockState(world, blockpos.up(), Blocks.AIR.getDefaultState());
                } else if (!flag) {
                    this.setBlockState(world, blockpos.up(), EndBlocks.SHADOWSTEEL.getDefaultState());
                } else if (this.open) {
                    this.setBlockState(world, blockpos.up(), Blocks.END_PORTAL.getDefaultState());
                } else {
                    this.setBlockState(world, blockpos.up(), Blocks.AIR.getDefaultState());
                }
            }
        }

        for (int i = 1; i < 6; i++) {
            if (i == 5) {
                this.setBlockState(world, pos.up(i), EndBlocks.CHISELED_SHADOWSTEEL.getDefaultState());
            } else {
                this.setBlockState(world, pos.up(i), EndBlocks.SHADOWSTEEL_PILLAR.getDefaultState());
            }
        }

        this.setBlockState(world, pos.up(2).north(2).east(2), EndBlocks.SMALL_SHADOWSTEEL_PILLAR.getDefaultState());
        this.setBlockState(world, pos.up(2).north(2).west(2), EndBlocks.SMALL_SHADOWSTEEL_PILLAR.getDefaultState());
        this.setBlockState(world, pos.up(2).south(2).east(2), EndBlocks.SMALL_SHADOWSTEEL_PILLAR.getDefaultState());
        this.setBlockState(world, pos.up(2).south(2).west(2), EndBlocks.SMALL_SHADOWSTEEL_PILLAR.getDefaultState());
        this.setBlockState(world, pos.up(3).north(2).east(2), EndBlocks.CHISELED_SHADOWSTEEL.getDefaultState());
        this.setBlockState(world, pos.up(3).north(2).west(2), EndBlocks.CHISELED_SHADOWSTEEL.getDefaultState());
        this.setBlockState(world, pos.up(3).south(2).east(2), EndBlocks.CHISELED_SHADOWSTEEL.getDefaultState());
        this.setBlockState(world, pos.up(3).south(2).west(2), EndBlocks.CHISELED_SHADOWSTEEL.getDefaultState());

        for (int i = 2; i < 6; i++) {
            this.placeholder(world, pos, Direction.byId(i));
        }

        cir.setReturnValue(true);
        cir.cancel();

    }
    private void placeholder(StructureWorldAccess world, BlockPos pos, Direction direction) {
        this.setBlockState(world, pos.offset(direction, 3).offset(direction.rotateYClockwise()).up(), EndBlocks.SHADOWSTEEL.getDefaultState());
        this.setBlockState(world, pos.offset(direction, 3).up(), EndBlocks.SHADOWSTEEL_PILLAR.getDefaultState().with(Properties.AXIS, direction.rotateYClockwise().getAxis()));
        this.setBlockState(world, pos.offset(direction, 3).offset(direction.rotateYCounterclockwise()).up(), EndBlocks.SHADOWSTEEL.getDefaultState());
    }
}
