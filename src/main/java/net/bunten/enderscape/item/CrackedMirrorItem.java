package net.bunten.enderscape.item;

import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class CrackedMirrorItem extends Item {
    public CrackedMirrorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        player.playSound(EnderscapeItemSounds.CRACKED_MIRROR_TRY_TELEPORT, 1, 1);
        player.displayClientMessage(Component.translatable("item.enderscape.cracked_mirror.message.try_use").withStyle(ChatFormatting.RED), true);
        player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        player.getCooldowns().addCooldown(this, 20);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);

        if (state.is(Blocks.LODESTONE)) {
            Player player = context.getPlayer();
            player.playSound(EnderscapeItemSounds.CRACKED_MIRROR_TRY_LINK, 1, 1);
            player.displayClientMessage(Component.translatable("item.enderscape.cracked_mirror.message.try_link").withStyle(ChatFormatting.RED), true);
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }
}
