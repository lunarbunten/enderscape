package net.bunten.enderscape.item;

import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class DriftJellyBottleItem extends Item {

    public DriftJellyBottleItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        super.finishUsingItem(stack, level, user);
        user.playSound(EnderscapeItemSounds.DRIFT_JELLY_BOTTLE_FINISH.value(), 1, 1);

        if (user instanceof ServerPlayer player) {
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (user instanceof Player player && !player.hasInfiniteMaterials()) {
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.getInventory().add(bottle)) {
                    player.drop(bottle, false);
                }
            }

            return stack;
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 40;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return EnderscapeItemSounds.DRIFT_JELLY_BOTTLE_DRINK.value();
    }

    @Override
    public SoundEvent getEatingSound() {
        return EnderscapeItemSounds.DRIFT_JELLY_BOTTLE_DRINK.value();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return ItemUtils.startUsingInstantly(level, player, interactionHand);
    }
}
