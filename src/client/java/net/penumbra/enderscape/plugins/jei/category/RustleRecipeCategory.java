package net.penumbra.enderscape.plugins.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.penumbra.enderscape.item.crafting.RustleRecipe;
import net.penumbra.enderscape.plugins.jei.EnderscapeJEI;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import org.jspecify.annotations.NonNull;

public class RustleRecipeCategory extends AbstractRecipeCategory<RecipeHolder<RustleRecipe>> {

    public static final int WINDOW_WIDTH = 100;
    public static final int WINDOW_HEIGHT = 28;

    public RustleRecipeCategory(IGuiHelper guiHelper) {
        super(
                EnderscapeJEI.RUSTLE,
                Component.translatable("jei.enderscape.category.rustle"),
                guiHelper.createDrawableItemLike(EnderscapeItems.RUSTLE_BUCKET),
                WINDOW_WIDTH, WINDOW_HEIGHT
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RustleRecipe> holder, @NonNull IFocusGroup focusGroup) {
        RustleRecipe recipe = holder.value();

        builder.addInputSlot(11, 6).setStandardSlotBackground().add(recipe.input());
        builder.addOutputSlot(69, 6).setOutputSlotBackground().add(recipe.result().create());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<RustleRecipe> holder, @NonNull IFocusGroup focusGroup) {
        builder.addAnimatedRecipeArrow(holder.value().effects().swellDuration()).setPosition(35, 6);
    }
}