package net.penumbra.enderscape.plugins.rrv.category;

import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewScreen;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import cc.cassian.rrv.common.recipe.rendering.AnimationTicker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.plugins.rrv.EnderscapeRRV;
import net.penumbra.enderscape.registry.item.EnderscapeItems;

import java.util.List;

public final class RustleClientRecipe implements ReliableClientRecipe {
    private final AnimationTicker animationTicker;
    private final Identifier id;
    private final SlotContent input;
    private final SlotContent output;
    private final SlotContent rustleInput;

    public RustleClientRecipe(Identifier id, SlotContent input, SlotContent output, int duration) {
        this.rustleInput = SlotContent.of(EnderscapeItems.RUSTLE_BUCKET);
        this.id = id;
        this.input = input;
        this.output = output;
        this.animationTicker = AnimationTicker.create(Identifier.withDefaultNamespace("rustling_tick"), duration);
    }

    @Override
    public void bindSlots(RecipeViewMenu.SlotFillContext slotFillContext) {
        slotFillContext.bindSlot(0, this.input);
        slotFillContext.bindSlot(1, this.output);
    }

    @Override
    public List<SlotContent> getIngredients() {
        return List.of(this.input, this.rustleInput);
    }

    @Override
    public List<SlotContent> getResults() {
        return List.of(this.output);
    }

    @Override
    public Identifier getId() {
        return this.id;
    }


    @Override
    public ReliableClientRecipeType getType() {
        return RustleClientRecipeType.INSTANCE;
    }

    @Override
    public List<AnimationTicker> getAnimationTickers() {
        return List.of(this.animationTicker);
    }

    @Override
    public void renderRecipe(RecipeViewScreen screen, RecipePosition recipePosition, GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        EnderscapeRRV.drawRecipeArrow(guiGraphics, this.animationTicker, 35, 8);
    }
}
