package net.penumbra.enderscape.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.server.EnderscapeServerNetworking;
import net.penumbra.enderscape.util.BlockUtil;

public class NebuliteOreBlock extends DropExperienceBlock {
    public NebuliteOreBlock(Properties settings) {
        super(UniformInt.of(6, 12), settings);
    }

    @Environment(EnvType.CLIENT)
    public static void makeParticles(int amount, int range, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < amount; i++) {
            BlockPos pos2 = BlockUtil.random(pos, random, range, range, range);

            if (level.getBlockState(pos2).isCollisionShapeFullBlock(level, pos2)) {
                return;
            }

            Vec3 center = Vec3.atCenterOf(pos);
            Vec3 other = Vec3.atCenterOf(pos2);

            double factor = 0.04F;

            double xs = (center.x() - other.x()) * factor;
            double ys = (center.y() - other.y()) * factor;
            double zs = (center.z() - other.z()) * factor;

            level.addParticle(EnderscapeParticles.NEBULITE_ORE, other.x(), other.y(), other.z(), xs, ys, zs);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextFloat() <= 0.5F) {
            EnderscapeServerNetworking.sendNebuliteOreSoundPayload(world, pos);
        }
    }

    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        makeParticles(Mth.nextInt(random, 1, 2), Mth.nextInt(random, 8, 12), world, pos, random);
    }
}