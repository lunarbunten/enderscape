package net.enderscape.blocks;

import net.enderscape.util.EndMath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class ShadowQuartzOreBlock extends Block {
    public ShadowQuartzOreBlock(Settings settings) {
        super(settings);
    }

    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
        super.onStacksDropped(state, world, pos, stack);
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            int i = EndMath.nextInt(world.getRandom(), 0, 2);
            if (i > 0) {
                dropExperience(world, pos, i);
            }
        }
    }
}