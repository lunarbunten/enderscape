package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.MagniaPolarity;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class EtchedMagniaStairsBlock extends StairBlock implements HasMagniaPolarity {

    protected final MagniaPolarity polarity;

    public EtchedMagniaStairsBlock(MagniaPolarity polarity, BlockState baseState, Properties properties) {
        super(baseState, properties);
        this.polarity = polarity;
    }

    @Override
    public Optional<MagniaPolarity> getPolarity(BlockState state) {
        return Optional.of(polarity);
    }
}