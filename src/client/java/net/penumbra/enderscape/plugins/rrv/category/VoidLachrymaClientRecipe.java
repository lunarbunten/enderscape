package net.penumbra.enderscape.plugins.rrv.category;

import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewScreen;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import cc.cassian.rrv.common.recipe.rendering.AnimationTicker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.plugins.rrv.EnderscapeRRV;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;

import java.util.List;

public final class VoidLachrymaClientRecipe implements ReliableClientRecipe {
    private final AnimationTicker animationTicker;
    private final Identifier id;
    private final SlotContent input;
    private final SlotContent output;
    private final float chance;
    private final SlotContent fluidInput;

    public VoidLachrymaClientRecipe(Identifier id, SlotContent input, SlotContent output, float chance) {
        this.id = id;
        this.fluidInput = SlotContent.of(EnderscapeBlocks.VOID_LACHRYMA);
        this.input = input;
        this.output = output;
        this.animationTicker = AnimationTicker.create(Identifier.withDefaultNamespace("voiding_tick"), (int) (20 / chance));
        this.chance = chance;
    }


    @Override
    public void bindSlots(RecipeViewMenu.SlotFillContext slotFillContext) {
        slotFillContext.bindSlot(0, this.input);
        slotFillContext.bindSlot(1, this.output);
    }

    @Override
    public List<SlotContent> getIngredients() {
        return List.of(this.input, this.fluidInput);
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
        return VoidLachrymaRecipeRecipeType.INSTANCE;
    }

    @Override
    public List<AnimationTicker> getAnimationTickers() {
        return List.of(this.animationTicker);
    }

    private MutableComponent averageChanceText(float chance) {
        return Component.translatable("jei.enderscape.category.void_lachryma.average_chance", String.format("%.2f", chance));
    }

    @Override
    public void renderRecipe(RecipeViewScreen screen, RecipePosition recipePosition, GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        EnderscapeRRV.drawRecipeArrow(guiGraphics, this.animationTicker, 35, 30);
        int y = 4;
        for (var line : screen.getFont().split(averageChanceText(this.chance), VoidLachrymaRecipeRecipeType.WINDOW_WIDTH - 4)) {
            guiGraphics.text(screen.getFont(), line, VoidLachrymaRecipeRecipeType.WINDOW_WIDTH / 2 - screen.getFont().width(line) / 2, y, 0xFF808080, false);
            y += 9;
        }
    }
}
