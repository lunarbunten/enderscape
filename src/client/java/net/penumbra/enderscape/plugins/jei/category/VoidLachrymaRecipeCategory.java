package net.penumbra.enderscape.plugins.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.penumbra.enderscape.item.crafting.VoidLachrymaRecipe;
import net.penumbra.enderscape.plugins.jei.EnderscapeJEI;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import org.jspecify.annotations.NonNull;

public class VoidLachrymaRecipeCategory extends AbstractRecipeCategory<RecipeHolder<VoidLachrymaRecipe>> {

    public static final int WINDOW_WIDTH = 100;
    public static final int WINDOW_HEIGHT = 54;

    public VoidLachrymaRecipeCategory(IGuiHelper guiHelper) {
        super(
                EnderscapeJEI.VOID_LACHRYMA,
                Component.translatable("jei.enderscape.category.void_lachryma"),
                guiHelper.createDrawableItemLike(EnderscapeItems.VOID_LACHRYMA_BUCKET),
                WINDOW_WIDTH, WINDOW_HEIGHT
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<VoidLachrymaRecipe> holder, @NonNull IFocusGroup focusGroup) {
        VoidLachrymaRecipe recipe = holder.value();

        builder.addInputSlot(11, 32).setStandardSlotBackground().add(recipe.input());
        builder.addOutputSlot(69, 32).setOutputSlotBackground().add(recipe.result().create());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<VoidLachrymaRecipe> holder, @NonNull IFocusGroup focusGroup) {
        float chance = holder.value().averageChancePerSecond();

        builder.addAnimatedRecipeArrow((int) (20.0 / chance)).setPosition(35, 32);
        builder.addText(averageChanceText(chance), getWidth() - 20, getHeight())
                .setPosition(10, 2)
                .setTextAlignment(HorizontalAlignment.CENTER)
                .setColor(0xFF808080);
    }

    private MutableComponent averageChanceText(float chance) {
        return Component.translatable("jei.enderscape.category.void_lachryma.average_chance", String.format("%.2f", chance));
    }
}