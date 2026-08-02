package net.penumbra.enderscape.entity.magnia;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.penumbra.enderscape.block.MagniaSproutBlockEntity;
import net.penumbra.enderscape.block.properties.MagniaPolarity;
import net.penumbra.enderscape.block.state.StateProperties;

public record MagniaMovementContext<T extends Entity>(
        T entity,
        MagniaSproutBlockEntity blockEntity,
        MagniaPolarity polarity
) {

    public Direction facing() {
        return blockEntity.getBlockState().getValue(StateProperties.FACING);
    }

    public MagniaInteractionBehavior<T> behavior() {
        return MagniaInteractionBehavior.get(entity);
    }
}