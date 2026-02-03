package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.crafting.ToolFuelingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public class EnderscapeRecipeSerializers {

    public static final Supplier<RecipeSerializer<ToolFuelingRecipe>> TOOL_FUELING = register("tool_fueling", () -> new SimpleCraftingRecipeSerializer<>(ToolFuelingRecipe::new));

    private static <T extends Recipe<?>> Supplier<RecipeSerializer<T>> register(String name, Supplier<RecipeSerializer<T>> serializer) {
        return RegistryHelper.register(BuiltInRegistries.RECIPE_SERIALIZER, Enderscape.id(name), serializer);
    }
}