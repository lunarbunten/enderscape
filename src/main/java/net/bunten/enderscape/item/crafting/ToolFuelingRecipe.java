package net.bunten.enderscape.item.crafting;

import net.bunten.enderscape.item.FueledTool;
import net.bunten.enderscape.registry.EnderscapeRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ToolFuelingRecipe extends CustomRecipe {
    public ToolFuelingRecipe(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput input, Level level) {
        int toolIndex = -1;
        int fuel = 0;

        // Finds fueled tool

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (FueledTool.is(stack)) {
                toolIndex = i;
                break;
            }
        }

        if (toolIndex < 0) return false;

        // Looks for and adds up total acceptable fuel for the tool

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty() || i == toolIndex) continue;
            if (isAcceptableFuel(input, stack, toolIndex)) {
                fuel++;
            } else {
                return false;
            }
        }

        if (fuel == 0) {
            return false;
        } else {
            ItemStack tool = input.getItem(toolIndex);

            int currentFuel = FueledTool.currentFuel(tool);
            int maxFuel = FueledTool.maxFuel(tool);

            return currentFuel < maxFuel && fuel <= maxFuel - currentFuel;
        }
    }

    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        int toolIndex = -1;
        int fuel = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (FueledTool.is(stack)) {
                toolIndex = i;
                break;
            }
        }

        if (toolIndex < 0) return ItemStack.EMPTY;

        // Looks for and adds up total acceptable fuel for the tool

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty() || i == toolIndex) continue;
            if (isAcceptableFuel(input, stack, toolIndex)) {
                fuel++;
            }
        }

        if (fuel == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack tool = input.getItem(toolIndex).copy();

            int added = Math.min(fuel, FueledTool.maxFuel(tool) - FueledTool.currentFuel(tool));
            FueledTool.setFuel(tool, FueledTool.currentFuel(tool) + added);

            return tool;
        }
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= 2;
    }

    private static boolean isAcceptableFuel(CraftingInput input, ItemStack stack, int toolIndex) {
        return stack.is(FueledTool.fuels(input.getItem(toolIndex)));
    }

    @Override
    public RecipeSerializer<ToolFuelingRecipe> getSerializer() {
        return EnderscapeRecipeSerializers.TOOL_FUELING.get();
    }
}