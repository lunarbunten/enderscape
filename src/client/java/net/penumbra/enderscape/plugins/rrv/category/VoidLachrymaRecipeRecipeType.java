package net.penumbra.enderscape.plugins.rrv.category;

import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import org.jspecify.annotations.Nullable;

public record VoidLachrymaRecipeRecipeType() implements ReliableClientRecipeType {
    public static final ReliableClientRecipeType INSTANCE = new VoidLachrymaRecipeRecipeType();

    public static final Identifier IDENTIFIER = Enderscape.id("void_lachryma");

    public static final int WINDOW_WIDTH = 100;
    public static final int WINDOW_HEIGHT = 54;
    public static final Identifier BACKGROUND = Enderscape.id("textures/gui/rrv/void_lachryma_background.png");

    @Override
    public Component getDisplayName() {
        return Component.translatable("jei.enderscape.category.void_lachryma");
    }

    @Override
    public int getDisplayWidth() {
        return WINDOW_WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return WINDOW_HEIGHT;
    }

    @Override
    public @Nullable Identifier getGuiTexture() {
        return BACKGROUND;
    }

    @Override
    public int getSlotCount() {
        return 2;
    }

    @Override
    public void placeSlots(RecipeViewMenu.SlotDefinition slotDefinition) {
        slotDefinition.addItemSlot(0, 11, 30);
        slotDefinition.addItemSlot(1, 69, 30);
    }

    @Override
    public Identifier getId() {
        return IDENTIFIER;
    }

    @Override
    public ItemStack getIcon() {
        return EnderscapeItems.VOID_LACHRYMA_BUCKET.getDefaultInstance();
    }
}