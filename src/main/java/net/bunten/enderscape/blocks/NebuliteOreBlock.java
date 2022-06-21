package net.bunten.enderscape.blocks;

import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class NebuliteOreBlock extends OreBlock {
    public NebuliteOreBlock(Settings settings) {
        super(settings, UniformIntProvider.create(6, 12));
    }

    @Environment(EnvType.CLIENT)
    protected boolean isBlockObstructed(World world, BlockPos pos) {
        int i = 0;
        for (var dir : Direction.values()) {
            var pos2 = pos.offset(dir);
            if (world.getBlockState(pos2).isOpaque()) {
                i++;

                if (i == 6) return true;
            }
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    protected SoundEvent getAmbientSound(World world, BlockPos cameraPos, BlockPos nebulitePos) {
        if (isBlockObstructed(world, nebulitePos)) {
            return EnderscapeSounds.BLOCK_NEBULITE_ORE_AMBIENT_OBSTRUCTED;
        } else {
            if (nebulitePos.isWithinDistance(cameraPos, 16)) {
                return EnderscapeSounds.BLOCK_NEBULITE_ORE_AMBIENT;
            } else {
                return EnderscapeSounds.BLOCK_NEBULITE_ORE_AMBIENT_FAR;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    protected void tryPlayAmbientSound(BlockState state, World world, BlockPos nebulitePos, Random random) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockPos cameraPos = client.cameraEntity.getBlockPos();
        
        float volume = MathUtil.nextFloat(random, 0.9F, 1.1F);
        float pitch = MathUtil.nextFloat(random, 0.9F, 1.1F);

        world.playSound(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ(), getAmbientSound(world, cameraPos, nebulitePos), SoundCategory.BLOCKS, volume, pitch, false);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        for (int i = 0; i < 5; i++) {
            Mutable mutable = new Mutable();
            mutable.set(pos.getX() + MathUtil.nextInt(random, -5, 5), pos.getY() + MathUtil.nextInt(random, -5, 5), pos.getZ()+ MathUtil.nextInt(random, -5, 5));
            
            if (!world.getBlockState(mutable).isFullCube(world, mutable)) {
                var color = new Vec3f(Vec3d.unpackRgb(0xB328D2));
                world.addParticle(new DustParticleEffect(color, 1), mutable.getX() + random.nextDouble(), mutable.getY() + random.nextDouble(), mutable.getZ() + random.nextDouble(), 0, 0, 0);
            }
        }

        if (random.nextInt(30) == 0) {
            tryPlayAmbientSound(state, world, pos, random);
        }
    }
}