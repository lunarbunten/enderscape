package net.bunten.enderscape.items;

import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class DriftJellyBottleItem extends Item {
    public DriftJellyBottleItem(Settings settings) {
        super(settings.food(EnderscapeItems.DRIFT_JELLY_COMPONENT));
    }
    
    @Override
    public SoundEvent getDrinkSound() {
        return EnderscapeSounds.ITEM_DRIFT_JELLY_BOTTLE_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return EnderscapeSounds.ITEM_DRIFT_JELLY_BOTTLE_DRINK;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        playFinishSound(user);

        if (user instanceof ServerPlayerEntity mob) {
            Criteria.CONSUME_ITEM.trigger(mob, stack);
            mob.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (user instanceof PlayerEntity mob && !((PlayerEntity) user).getAbilities().creativeMode) {
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!mob.getInventory().insertStack(bottle)) {
                    mob.dropItem(bottle, false);
                }
            }

            return stack;
        }
    }

    protected void playFinishSound(LivingEntity user) {
        user.playSound(EnderscapeSounds.ITEM_DRIFT_JELLY_BOTTLE_FINISH, 1, 1);
    }
}