package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.registry.item.EnderscapeItems;

public class EndVaultBlock extends VaultBlock {
    public EndVaultBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean bl) {
        return EnderscapeItems.getEndVaultInstance();
    }
}