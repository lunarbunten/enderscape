package net.bunten.enderscape.items;

import net.bunten.enderscape.blocks.NebuliteCauldronBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

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
            mob.incrementStat(Stats.FILL_CAULDRON);
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);

            if (!world.isClient()) {
                world.setBlockState(pos, EnderscapeBlocks.NEBULITE_CAULDRON.getDefaultState(), 3);
                world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, EnderscapeSounds.BLOCK_NEBULITE_CAULDRON_FILL, SoundCategory.BLOCKS, 0.6F, 1);

                if (!mob.getAbilities().creativeMode) {
                    stack.decrement(1);
                }

                if (mob instanceof ServerPlayerEntity server) {
                    EnderscapeCriteria.LIQUIFY_NEBULITE.trigger(server, stack);
                }
            }

            return ActionResult.SUCCESS;
        }
        
        if (state.isOf(EnderscapeBlocks.NEBULITE_CAULDRON) && NebuliteCauldronBlock.canLevel(world, pos, state)) {
            mob.incrementStat(Stats.FILL_CAULDRON);
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);

            if (!world.isClient()) {
                NebuliteCauldronBlock.addLevel(world, pos, state);

                if (!mob.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
    
                if (mob instanceof ServerPlayerEntity server) {
                    EnderscapeCriteria.LIQUIFY_NEBULITE.trigger(server, stack);
                }
            }

            return ActionResult.SUCCESS;
        }
        
        return ActionResult.PASS;
    }
}