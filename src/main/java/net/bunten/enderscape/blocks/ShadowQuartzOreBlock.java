package net.bunten.enderscape.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ShadowQuartzOreBlock extends Block {
    public ShadowQuartzOreBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, stack, dropExperience);
        if (dropExperience) {
            dropExperienceWhenMined(world, pos, stack, UniformIntProvider.create(1, 3));
        }
    }
}