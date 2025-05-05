package net.bunten.enderscape.block;

import net.bunten.enderscape.registry.EnderscapeServerNetworking;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.util.BlockUtil;
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

public class NebuliteOreBlock extends DropExperienceBlock {
    public NebuliteOreBlock(Properties settings) {
        super(UniformInt.of(6, 12), settings);
    }

    @Environment(EnvType.CLIENT)
    public static void makeParticles(int amount, int range, Level world, BlockPos pos, RandomSource random) {
        for (int i = 0; i < amount; i++) {
            BlockPos pos2 = BlockUtil.random(pos, random, range, range, range);
            if (world.getBlockState(pos2).isCollisionShapeFullBlock(world, pos2)) return;
            float xs = (pos.getX() - pos2.getX()) * 0.04F;
            float ys = (pos.getY() - pos2.getY()) * 0.04F;
            float zs = (pos.getZ() - pos2.getZ()) * 0.04F;
            world.addParticle(EnderscapeParticles.NEBULITE_ORE, pos2.getX() + random.nextDouble(), pos2.getY() + random.nextDouble(), pos2.getZ() + random.nextDouble(), xs, ys, zs);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        EnderscapeServerNetworking.sendNebuliteOreSoundPayload(world, pos);
    }

    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        makeParticles(Mth.nextInt(random, 1, 2), Mth.nextInt(random, 8, 12), world, pos, random);
    }
}