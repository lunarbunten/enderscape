package net.bunten.enderscape.block;

import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class VoidCampfireBlockEntity extends CampfireBlockEntity {
    public VoidCampfireBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return EnderscapeBlockEntities.VOID_CAMPFIRE.get();
    }
}
