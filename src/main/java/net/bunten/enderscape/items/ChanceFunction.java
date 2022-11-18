package net.bunten.enderscape.items;

import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.bunten.enderscape.registry.EnderscapeModifications;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class ChanceFunction extends LootItemConditionalFunction {
    final NumberProvider chance;

    ChanceFunction(LootItemCondition[] conditions, NumberProvider chance) {
        super(conditions);
        this.chance = chance;
    }

    @Override
    public LootItemFunctionType getType() {
        return EnderscapeModifications.CHANCE;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return chance.getReferencedContextParams();
    }

    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        if (context.getRandom().nextFloat() <= chance.getFloat(context)) {
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static LootItemConditionalFunction.Builder<?> setChance(NumberProvider numberProvider) {
        return ChanceFunction.simpleBuilder(conditions -> new ChanceFunction((LootItemCondition[])conditions, numberProvider));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<ChanceFunction> {
        
        @Override
        public void serialize(JsonObject json, ChanceFunction function, JsonSerializationContext context) {
            super.serialize(json, function, context);
            json.add("chance", context.serialize(function.chance));
        }

        @Override
        public ChanceFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new ChanceFunction(conditions, GsonHelper.getAsObject(json, "chance", context, NumberProvider.class));
        }
    }
}