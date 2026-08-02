package net.penumbra.enderscape.plugins.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.item.crafting.RustleRecipe;
import net.penumbra.enderscape.item.crafting.VoidLachrymaRecipe;
import net.penumbra.enderscape.plugins.jei.category.RustleRecipeCategory;
import net.penumbra.enderscape.plugins.jei.category.VoidLachrymaRecipeCategory;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class EnderscapeJEI implements IModPlugin {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final IRecipeType<RecipeHolder<RustleRecipe>> RUSTLE = IRecipeType.create(RustleRecipe.TYPE);
    public static final IRecipeType<RecipeHolder<VoidLachrymaRecipe>> VOID_LACHRYMA = IRecipeType.create(VoidLachrymaRecipe.TYPE);

    @Override
    public Identifier getPluginUid() {
        return Enderscape.id("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        LOGGER.info("Registering categories...");

        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new RustleRecipeCategory(guiHelper));
        registration.addRecipeCategories(new VoidLachrymaRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        LOGGER.info("Registering recipes...");

        registration.addRecipes(RUSTLE, allOf(RustleRecipe.TYPE));
        registration.addRecipes(VOID_LACHRYMA, allOf(VoidLachrymaRecipe.TYPE));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        LOGGER.info("Registering recipe catalysts...");

        registration.addCraftingStation(RUSTLE, EnderscapeItems.RUSTLE_BUCKET);
        registration.addCraftingStation(VOID_LACHRYMA, EnderscapeItems.VOID_LACHRYMA_BUCKET);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        LOGGER.info("Registering item subtypes...");

        registration.registerFromDataComponentTypes(EnderscapeItems.RUBBLE_SHIELD, EnderscapeDataComponents.RUBBLE_SHIELD_VARIANT);
        registration.registerFromDataComponentTypes(EnderscapeItems.MIRROR, EnderscapeDataComponents.DYE_COLOR);
    }

    private <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> allOf(RecipeType<T> type) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            throw new NullPointerException("Level is null trying to access recipes for recipe type " + type);
        }
        return new ArrayList<>(level.recipeAccess().getSynchronizedRecipes().getAllOfType(type));
    }
}