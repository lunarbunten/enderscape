package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;

public class VoidTorchBlock extends BaseTorchBlock implements HasMagniaPowerSignal {
    public static final MapCodec<VoidTorchBlock> CODEC = simpleCodec(VoidTorchBlock::new);

    public VoidTorchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends VoidTorchBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(2) == 0) {
            Vec3 particlePos = pos.getCenter().add(0, 0.2, 0);

            level.addParticle(
                    EnderscapeParticles.VOID_STARS,
                    particlePos.x() + Mth.nextDouble(random, -0.2, 0.2),
                    particlePos.y(),
                    particlePos.z() + Mth.nextDouble(random, -0.2, 0.2),
                    0.0,
                    0.015,
                    0.0
            );
        }
    }

    @Override
    public int getMagniaPowerSignal(BlockState state, BlockState neighbor) {
        return 15;
    }
}