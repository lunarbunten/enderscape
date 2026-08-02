package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.block.fluid.VoidLachrymaFluid;
import net.penumbra.enderscape.registry.block.EnderscapeFluids;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;

public class VoidLachrymaCauldron extends AbstractCauldronBlock {
    public static final MapCodec<VoidLachrymaCauldron> CODEC = simpleCodec(VoidLachrymaCauldron::new);
    private static final VoxelShape SHAPE_INSIDE = Block.column(12.0, 4.0, 15.0);
    private static final VoxelShape FILLED_SHAPE = Shapes.or(AbstractCauldronBlock.SHAPE, SHAPE_INSIDE);

    @Override
    public MapCodec<VoidLachrymaCauldron> codec() {
        return CODEC;
    }

    public VoidLachrymaCauldron(final BlockBehaviour.Properties properties) {
        super(properties, EnderscapeFluids.VOID_LACHRYMA_CAULDRON_INTERACTION);
    }

    @Override
    protected InteractionResult useItemOn(final ItemStack stack, final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult result) {
        if (stack.is(Items.BUCKET)) {
            if (!level.isClientSide()) {
                Item itemUsed = stack.getItem();

                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, EnderscapeItems.VOID_LACHRYMA_BUCKET.getDefaultInstance()));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(itemUsed));

                level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                level.playSound(null, pos, EnderscapeItemSounds.VOID_LACHRYMA_BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, result);
        }
    }

    @Override
    protected double getContentHeight(final BlockState state) {
        return 0.9375;
    }

    @Override
    public boolean isFull(final BlockState state) {
        return true;
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(final BlockState state, final BlockGetter level, final BlockPos pos, final Entity entity) {
        return FILLED_SHAPE;
    }

    @Override
    protected void entityInside(final BlockState state, final Level level, final BlockPos pos, final Entity entity, final InsideBlockEffectApplier applier, final boolean isPrecise) {
        VoidLachrymaFluid.applyEntityInsideEffects(entity, applier);
    }

    @Override
    protected int getAnalogOutputSignal(final BlockState state, final Level level, final BlockPos pos, final Direction direction) {
        return 3;
    }
}