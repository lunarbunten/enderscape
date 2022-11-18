package net.bunten.enderscape.items;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;

public class MurushroomsItem extends BlockItem {

    public static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder().alwaysEat().nutrition(4).saturationMod(0.3F).effect(new MobEffectInstance(MobEffects.POISON, 200, 1), 1).build();
    
    public MurushroomsItem(Properties settings) {
        super(EnderscapeBlocks.MURUSHROOMS, settings.food(FOOD_PROPERTIES));
    }
}