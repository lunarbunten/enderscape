package net.bunten.enderscape.items;

import net.bunten.enderscape.blocks.FlangerBerryVine;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class FlangerBerryItem extends BlockItem {
    
    private static final BooleanProperty ATTACHED = FlangerBerryVine.ATTACHED;
    private static final IntProperty AGE = FlangerBerryVine.AGE;
    private static final int MAX_AGE = FlangerBerryVine.MAX_AGE;

    private final Block VINE = getBlock();

    public FlangerBerryItem(Item.Settings settings) {
        super(EnderscapeBlocks.FLANGER_BERRY_VINE, settings.food(EnderscapeItems.FLANGER_BERRY_COMPONENT));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos();

        if (world.getBlockState(pos).isOf(VINE)) {
            Mutable mutable = pos.mutableCopy();
            for (var i = mutable.getY(); i > world.getBottomY(); mutable.move(Direction.DOWN)) {
                if (mutable.getY() >= world.getBottomY()) {
                    if (world.getBlockState(mutable).isAir()) {
                        var state2 = world.getBlockState(mutable.up());
                        if (state2.isOf(VINE) && !state2.get(ATTACHED)) {
                            world.setBlockState(mutable.up(), state2.with(ATTACHED, true));
                            place(new ItemPlacementContext(new ItemUsageContext(context.getPlayer(), context.getHand(), new BlockHitResult(new Vec3d(mutable.getX() + 0.5 + Direction.DOWN.getOffsetX() * 0.5, mutable.getY() + 0.5 + Direction.DOWN.getOffsetY() * 0.5, mutable.getZ() + 0.5 + Direction.DOWN.getOffsetZ() * 0.5), Direction.DOWN, mutable, false))));
                            return ActionResult.success(world.isClient());
                        }
                    } else {
                        continue;
                    }
                } else {
                    break;
                }
            }
        }

        return super.useOnBlock(context);
    }

    @Override
    protected BlockState getPlacementState(ItemPlacementContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos();

        BlockState state = super.getPlacementState(context);
        BlockState state2 = world.getBlockState(pos.up());

        return state2.isOf(VINE) ? state.with(AGE, Math.min(state2.get(AGE) + 1, MAX_AGE)) : state;
    }

    @Override
    public String getTranslationKey() {
        return super.getOrCreateTranslationKey();
    }
}