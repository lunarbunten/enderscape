package net.penumbra.enderscape.item.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.item.component.FueledTool;
import net.penumbra.enderscape.registry.item.EnderscapeRecipeSerializers;

public class ToolFuelingRecipe extends CustomRecipe {
    public static final ToolFuelingRecipe INSTANCE = new ToolFuelingRecipe();
    public static final MapCodec<ToolFuelingRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, ToolFuelingRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

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

    public ItemStack assemble(CraftingInput input) {
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

    private static boolean isAcceptableFuel(CraftingInput input, ItemStack stack, int toolIndex) {
        return stack.is(FueledTool.fuels(input.getItem(toolIndex)));
    }

    @Override
    public RecipeSerializer<ToolFuelingRecipe> getSerializer() {
        return EnderscapeRecipeSerializers.TOOL_FUELING;
    }
}