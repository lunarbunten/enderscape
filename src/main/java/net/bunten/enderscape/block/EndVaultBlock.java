package net.bunten.enderscape.block;

import net.bunten.enderscape.registry.EnderscapeItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.state.BlockState;

public class EndVaultBlock extends VaultBlock {
    public EndVaultBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return EnderscapeItems.getEndVaultInstance();
    }
}