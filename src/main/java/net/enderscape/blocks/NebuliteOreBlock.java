package net.enderscape.blocks;

import java.util.Random;

import net.enderscape.registry.EndSounds;
import net.enderscape.util.EndMath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class NebuliteOreBlock extends Block {
    public NebuliteOreBlock(Settings settings) {
        super(settings);
    }

    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
        super.onStacksDropped(state, world, pos, stack);
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            int x = EndMath.nextInt(world.getRandom(), 6, 12);
            if (x > 0) {
                dropExperience(world, pos, x);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (random.nextInt(30) == 0) {
            world.playSound(x, y, z, EndSounds.BLOCK_NEBULITE_ORE_AMBIENT, SoundCategory.BLOCKS, EndMath.nextFloat(random, 0.8F, 1), EndMath.nextFloat(random, 0.9F, 1.1F), false);
        }

        for (int i = 0; i < 5; i++) {
            double vx = EndMath.nextFloat(random, -0.01F, 0.01F);
            double vy = EndMath.nextFloat(random, -0.01F, 0.01F);
            double vz = EndMath.nextFloat(random, -0.01F, 0.01F);

            Mutable mutable = new Mutable();
            mutable.set(x + EndMath.nextInt(random, -5, 5), y + EndMath.nextInt(random, -5, 5), z + EndMath.nextInt(random, -5, 5));
            if (!world.getBlockState(mutable).isFullCube(world, mutable)) {
                world.addParticle(new DustParticleEffect(new Vec3f(Vec3d.unpackRgb(11741394)), 1), mutable.getX() + random.nextDouble(), mutable.getY() + random.nextDouble(), mutable.getZ() + random.nextDouble(), vx, vy, vz);
            }
        }
    }
}