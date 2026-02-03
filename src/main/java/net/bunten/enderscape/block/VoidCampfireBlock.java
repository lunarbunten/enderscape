package net.bunten.enderscape.block;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class VoidCampfireBlock extends CampfireBlock {
    public VoidCampfireBlock(Properties properties) {
        super(false, 1, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VoidCampfireBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return state.getValue(LIT) ? createTickerHelper(type, EnderscapeBlockEntities.VOID_CAMPFIRE.get(), VoidCampfireBlockEntity::particleTick) : null;
        } else {
            return state.getValue(LIT) ? createTickerHelper(type, EnderscapeBlockEntities.VOID_CAMPFIRE.get(), VoidCampfireBlockEntity::cookTick) : createTickerHelper(type, EnderscapeBlockEntities.VOID_CAMPFIRE.get(), VoidCampfireBlockEntity::cooldownTick);
        }
    }
}