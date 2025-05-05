package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.DirectionProperties;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CelestialPathBlock extends AbstractOvergrowthBlock {

    public static final MapCodec<CelestialPathBlock> CODEC = simpleCodec(CelestialPathBlock::new);

    public CelestialPathBlock(BlockBehaviour.Properties settings) {
        super(true, Blocks.END_STONE, DirectionProperties.create().up(), settings);
    }

    @Override
    public MapCodec<CelestialPathBlock> codec() {
        return CODEC;
    }
}