package net.bunten.enderscape.blocks;

import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class NebuliteOreBlock extends DropExperienceBlock {
    public NebuliteOreBlock(Properties settings) {
        super(settings, UniformInt.of(6, 12));
    }

    @Environment(EnvType.CLIENT)
    public static void makeParticles(int amount, int range, Level world, BlockPos pos, RandomSource random) {
        for (int i = 0; i < amount; i++) {
            BlockPos pos2 = BlockUtil.random(pos, random, range, range, range);
            if (world.getBlockState(pos2).isCollisionShapeFullBlock(world, pos2)) return;
            float xs = (pos.getX() - pos2.getX()) * 0.06F;
            float ys = (pos.getY() - pos2.getY()) * 0.06F;
            float zs = (pos.getZ() - pos2.getZ()) * 0.06F;
            world.addParticle(EnderscapeParticles.NEBULITE_CLOUD, pos2.getX() + random.nextDouble(), pos2.getY() + random.nextDouble(), pos2.getZ() + random.nextDouble(), xs, ys, zs);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        NebuliteOrePackets.sendSoundToPlayers(world, pos);
    }

    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        makeParticles(MathUtil.nextInt(random, 2, 4), MathUtil.nextInt(random, 8, 12), world, pos, random);
    }
}