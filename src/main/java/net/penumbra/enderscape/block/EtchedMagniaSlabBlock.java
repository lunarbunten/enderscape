package net.penumbra.enderscape.block;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.block.properties.MagniaPolarity;

import java.util.Optional;

public class EtchedMagniaSlabBlock extends SlabBlock implements HasMagniaPolarity {

    protected final MagniaPolarity polarity;

    public EtchedMagniaSlabBlock(MagniaPolarity polarity, Properties properties) {
        super(properties);
        this.polarity = polarity;
    }

    @Override
    public Optional<MagniaPolarity> getPolarity(BlockState state) {
        return Optional.of(polarity);
    }
}