package net.bunten.enderscape.items;

import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class DriftJellyBottleItem extends Item {

    private static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder().nutrition(6).saturationMod(0.2F).effect(new MobEffectInstance(MobEffects.LEVITATION, 100), 1).build();

    public DriftJellyBottleItem(Properties settings) {
        super(settings.food(FOOD_PROPERTIES));
    }
    
    @Override
    public SoundEvent getDrinkingSound() {
        return EnderscapeSounds.DRIFT_JELLY_BOTTLE_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return EnderscapeSounds.DRIFT_JELLY_BOTTLE_DRINK;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(world, user, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        super.finishUsingItem(stack, world, user);
        playFinishSound(user);

        if (user instanceof ServerPlayer player) {
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (user instanceof Player player && !player.getAbilities().instabuild) {
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.getInventory().add(bottle)) {
                    player.drop(bottle, false);
                }
                player.getCooldowns().addCooldown(this, 68);
            }

            return stack;
        }
    }

    protected void playFinishSound(LivingEntity user) {
        user.playSound(EnderscapeSounds.DRIFT_JELLY_BOTTLE_FINISH, 1, 1);
    }
}