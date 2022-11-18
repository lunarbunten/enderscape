package net.bunten.enderscape.items;

import net.bunten.enderscape.blocks.AbstractVineBlock;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class AbstractVineItem extends ItemNameBlockItem {
    
    private static final BooleanProperty ATTACHED = AbstractVineBlock.ATTACHED;
    private static final IntegerProperty AGE = AbstractVineBlock.AGE;
    private static final int MAX_AGE = AbstractVineBlock.MAX_AGE;

    public AbstractVineItem(Block block, Item.Properties settings) {
        super(block, settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var world = context.getLevel();
        var pos = context.getClickedPos();
        var vine = getBlock();

        if (world.getBlockState(pos).is(vine)) {
            MutableBlockPos mutable = pos.mutable();
            for (var i = mutable.getY(); i > world.getMinBuildHeight(); mutable.move(Direction.DOWN)) {
                if (mutable.getY() >= world.getMinBuildHeight()) {
                    if (world.getBlockState(mutable).isAir()) {
                        var state2 = world.getBlockState(mutable.above());
                        if (state2.is(vine) && !state2.getValue(ATTACHED)) {
                            world.setBlockAndUpdate(mutable.above(), state2.setValue(ATTACHED, true));
                            place(new BlockPlaceContext(new UseOnContext(context.getPlayer(), context.getHand(), new BlockHitResult(new Vec3(mutable.getX() + 0.5 + Direction.DOWN.getStepX() * 0.5, mutable.getY() + 0.5 + Direction.DOWN.getStepY() * 0.5, mutable.getZ() + 0.5 + Direction.DOWN.getStepZ() * 0.5), Direction.DOWN, mutable, false))));
                            return InteractionResult.sidedSuccess(world.isClientSide());
                        }
                    } else {
                        continue;
                    }
                } else {
                    break;
                }
            }
        }

        return super.useOn(context);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        var world = context.getLevel();
        var pos = context.getClickedPos();

        BlockState state = super.getPlacementState(context);
        BlockState state2 = world.getBlockState(pos.above());

        return state2.is(getBlock()) ? state.setValue(AGE, Math.min(state2.getValue(AGE) + 1, MAX_AGE)) : state;
    }
}