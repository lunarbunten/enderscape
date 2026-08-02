package net.penumbra.enderscape.block;

import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.block.properties.MagniaPolarity;

import java.util.Optional;

public class EtchedMagniaWallBlock extends WallBlock implements HasMagniaPolarity {

    protected final MagniaPolarity polarity;

    public EtchedMagniaWallBlock(MagniaPolarity polarity, Properties properties) {
        super(properties);
        this.polarity = polarity;
    }

    @Override
    public Optional<MagniaPolarity> getPolarity(BlockState state) {
        return Optional.of(polarity);
    }
}