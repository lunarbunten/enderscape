package net.bunten.enderscape.blocks;

import java.util.Random;

import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
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
            int x = MathUtil.nextInt(world.getRandom(), 6, 12);
            if (x > 0) {
                dropExperience(world, pos, x);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    protected boolean isBlockObstructed(World world, BlockPos pos) {
        int i = 0;
        for (var dir : Direction.values()) {
            var pos2 = pos.offset(dir);
            if (world.getBlockState(pos2).isOpaque()) {
                i++;
            }
        }
        return i == 6;
    }

    @Environment(EnvType.CLIENT)
    protected void tryPlayAmbientSound(BlockState state, World world, BlockPos pos, Random random) {
        final var client = MinecraftClient.getInstance();
        var pos2 = client.cameraEntity.getBlockPos();
        var distance = pos.getSquaredDistance(pos2);

        SoundEvent sound;

        if (isBlockObstructed(world, pos)) {
            sound = EnderscapeSounds.BLOCK_NEBULITE_ORE_AMBIENT_OBSTRUCTED;
        } else {
            if (distance > 70) {
                sound = EnderscapeSounds.BLOCK_NEBULITE_ORE_AMBIENT_FAR;
            } else {
                sound = EnderscapeSounds.BLOCK_NEBULITE_ORE_AMBIENT;
            }
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        world.playSound(x, y, z, sound, SoundCategory.BLOCKS, MathUtil.nextFloat(random, 0.8F, 1), MathUtil.nextFloat(random, 0.9F, 1.1F), false);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        for (int i = 0; i < 5; i++) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            double vx = MathUtil.nextFloat(random, -0.01F, 0.01F);
            double vy = MathUtil.nextFloat(random, -0.01F, 0.01F);
            double vz = MathUtil.nextFloat(random, -0.01F, 0.01F);

            Mutable mutable = new Mutable();
            mutable.set(x + MathUtil.nextInt(random, -5, 5), y + MathUtil.nextInt(random, -5, 5), z + MathUtil.nextInt(random, -5, 5));
            
            if (!world.getBlockState(mutable).isFullCube(world, mutable)) {
                world.addParticle(new DustParticleEffect(new Vec3f(Vec3d.unpackRgb(0xB328D2)), 1), mutable.getX() + random.nextDouble(), mutable.getY() + random.nextDouble(), mutable.getZ() + random.nextDouble(), vx, vy, vz);
            }
        }

        if (random.nextInt(30) == 0) {
            tryPlayAmbientSound(state, world, pos, random);
        }
    }
}