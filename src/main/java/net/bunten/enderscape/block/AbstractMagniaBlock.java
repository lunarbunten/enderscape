package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.MagniaPolarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public abstract class AbstractMagniaBlock extends Block implements HasMagniaPolarity {

    protected final MagniaPolarity polarity;

    public AbstractMagniaBlock(MagniaPolarity polarity, Properties properties) {
        super(properties);
        this.polarity = polarity;
    }

    @Override
    public Optional<MagniaPolarity> getPolarity(BlockState state) {
        return Optional.of(polarity);
    }
}