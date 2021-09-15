package net.enderscape.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;


/**
 * Contains all the default food components used in Enderscape food items.
 */
public class EndFoods {

    public static final FoodComponent FLANGER_BERRY = new FoodComponent.Builder().hunger(3).saturationModifier(0.8F).build();
    public static final FoodComponent DRIFT_JELLY = new FoodComponent.Builder().hunger(6).saturationModifier(0.2F).statusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100), 1).build();
    public static final FoodComponent MURUSHROOMS = new FoodComponent.Builder().alwaysEdible().hunger(4).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 1), 1).build();

}