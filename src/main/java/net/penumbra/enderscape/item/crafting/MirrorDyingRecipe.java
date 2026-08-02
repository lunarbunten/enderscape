package net.penumbra.enderscape.item.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.registry.item.EnderscapeRecipeSerializers;

import static net.minecraft.core.component.DataComponents.DYE;
import static net.penumbra.enderscape.registry.component.EnderscapeDataComponents.DYE_COLOR;
import static net.penumbra.enderscape.registry.item.EnderscapeItems.MIRROR;

public class MirrorDyingRecipe extends CustomRecipe {
    public static final MirrorDyingRecipe INSTANCE = new MirrorDyingRecipe();
    public static final MapCodec<MirrorDyingRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, MirrorDyingRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public boolean matches(CraftingInput input, Level level) {
        int mirrorIndex;
        int dyeIndex;

        if ((mirrorIndex = getMirror(input)) < 0) {
            return false;
        }

        if ((dyeIndex = getDye(input, mirrorIndex)) < 0) {
            return false;
        }

        return true;
    }

    public ItemStack assemble(CraftingInput input) {
        int mirrorIndex;
        int dyeIndex;

        if ((mirrorIndex = getMirror(input)) < 0) {
            return ItemStack.EMPTY;
        }

        if ((dyeIndex = getDye(input, mirrorIndex)) < 0) {
            return ItemStack.EMPTY;
        }

        ItemStack mirror = input.getItem(mirrorIndex).copy();

        mirror.set(DYE_COLOR, input.getItem(dyeIndex).get(DYE));

        return mirror;
    }

    private static int getMirror(CraftingInput input) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(MIRROR)) {
                return i;
            }
        }
        return -1;
    }

    private static int getDye(CraftingInput input, int mirrorIndex) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty() || i == mirrorIndex) continue;
            if (canUseAsDye(stack, input.getItem(mirrorIndex))) {
                return i;
            }
        }

        return -1;
    }

    private static boolean canUseAsDye(ItemStack stack, ItemStack tool) {
        if (stack.is(ItemTags.DYES) && stack.has(DYE)) {
            return !tool.has(DYE_COLOR) || !stack.get(DYE).equals(tool.get(DYE_COLOR));
        } else {
            return false;
        }
    }

    @Override
    public RecipeSerializer<MirrorDyingRecipe> getSerializer() {
        return EnderscapeRecipeSerializers.MIRROR_DYING;
    }
}