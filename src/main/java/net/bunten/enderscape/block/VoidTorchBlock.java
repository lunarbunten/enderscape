package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;


public class VoidTorchBlock extends BaseTorchBlock {

    public Supplier<SimpleParticleType> flameParticle;

    public VoidTorchBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.flameParticle = EnderscapeParticles.VOID_FIRE_FLAME;
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double d0 = (double)pos.getX() + (double)0.5F;
        double d1 = (double)pos.getY() + 0.7;
        double d2 = (double)pos.getZ() + (double)0.5F;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, (double)0.0F, (double)0.0F, (double)0.0F);
        level.addParticle(this.flameParticle.get(), d0, d1, d2, (double)0.0F, (double)0.0F, (double)0.0F);
    }

    @Override
    protected MapCodec<? extends BaseTorchBlock> codec() {
        return null;
    }
}
