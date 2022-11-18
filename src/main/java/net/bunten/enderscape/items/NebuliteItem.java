package net.bunten.enderscape.items;

import net.bunten.enderscape.blocks.NebuliteCauldronBlock;
import net.bunten.enderscape.criteria.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class NebuliteItem extends Item {
    public NebuliteItem(Properties settings) {
        super(settings);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        Player mob = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (state.is(Blocks.CAULDRON)) {
            mob.awardStat(Stats.FILL_CAULDRON);
            world.gameEvent(null, GameEvent.FLUID_PLACE, pos);

            if (!world.isClientSide()) {
                world.setBlock(pos, EnderscapeBlocks.NEBULITE_CAULDRON.defaultBlockState(), 3);
                world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, EnderscapeSounds.NEBULITE_CAULDRON_FILL, SoundSource.BLOCKS, 0.6F, 1);

                if (!mob.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                if (mob instanceof ServerPlayer server) {
                    EnderscapeCriteria.LIQUIFY_NEBULITE.trigger(server, stack);
                }
            }

            return InteractionResult.SUCCESS;
        }
        
        if (state.is(EnderscapeBlocks.NEBULITE_CAULDRON) && NebuliteCauldronBlock.canLevel(world, pos, state)) {
            mob.awardStat(Stats.FILL_CAULDRON);
            world.gameEvent(null, GameEvent.FLUID_PLACE, pos);

            if (!world.isClientSide()) {
                NebuliteCauldronBlock.addLevel(world, pos, state);

                if (!mob.getAbilities().instabuild) {
                    stack.shrink(1);
                }
    
                if (mob instanceof ServerPlayer server) {
                    EnderscapeCriteria.LIQUIFY_NEBULITE.trigger(server, stack);
                }
            }

            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.PASS;
    }
}