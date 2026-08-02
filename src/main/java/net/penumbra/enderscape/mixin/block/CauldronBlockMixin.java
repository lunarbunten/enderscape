package net.penumbra.enderscape.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCauldronBlock.class)
public abstract class CauldronBlockMixin extends Block {

    public CauldronBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    public void Enderscape$useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> info) {
        if (stack.is(EnderscapeItems.VOID_LACHRYMA_BUCKET)) {
            if (!level.isClientSide()) {
                Item itemUsed = stack.getItem();

                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
                player.awardStat(Stats.FILL_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(itemUsed));

                level.setBlockAndUpdate(pos, EnderscapeBlocks.VOID_LACHRYMA_CAULDRON.defaultBlockState());
                level.playSound(null, pos, EnderscapeItemSounds.VOID_LACHRYMA_BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }

            info.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}