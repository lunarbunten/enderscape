package net.enderscape.items;

import net.enderscape.registry.EndBlocks;
import net.enderscape.registry.EndCriteria;
import net.enderscape.registry.EndSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NebuliteItem extends Item {
    public NebuliteItem(Settings settings) {
        super(settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity mob = context.getPlayer();
        ItemStack stack = context.getStack();

        if (state.isOf(Blocks.CAULDRON)) {
            if (!world.isClient()) {
                world.setBlockState(pos, EndBlocks.NEBULITE_CAULDRON.getDefaultState(), 3);
                world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, EndSounds.BLOCK_NEBULITE_CAULDRON_FILL, SoundCategory.BLOCKS, 0.6F, 1);
                if (!mob.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
                if (mob instanceof ServerPlayerEntity server) {
                    EndCriteria.LIQUIFY_NEBULITE.trigger(server, stack);
                }
            }
            return ActionResult.success(world.isClient());
        } else {
            return ActionResult.PASS;
        }
    }
}