package net.penumbra.enderscape.plugins.rrv;


import cc.cassian.rrv.api.ReliableRecipeViewerClientPlugin;
import cc.cassian.rrv.api.recipe.ItemView;
import cc.cassian.rrv.client.recipe.ClientRecipeManager;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import cc.cassian.rrv.common.recipe.rendering.AnimationTicker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.item.component.RubbleShieldVariant;
import net.penumbra.enderscape.item.crafting.RustleRecipe;
import net.penumbra.enderscape.item.crafting.VoidLachrymaRecipe;
import net.penumbra.enderscape.plugins.rrv.category.RustleClientRecipe;
import net.penumbra.enderscape.plugins.rrv.category.VoidLachrymaClientRecipe;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnderscapeRRV implements ReliableRecipeViewerClientPlugin {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Identifier RECIPE_ARROW_FILLED = Enderscape.id("rrv/recipe_arrow_filled");

    @Override
    public void onIntegrationInitialize() {
        ItemView.addClientRecipeProvider(recipeList -> { // provides a list of `ReliableClientRecipe`s to add to
            ClientRecipeManager.INSTANCE.getRecipesForType(RustleRecipe.TYPE).forEach(holder -> { // provides a list of `RecipeHolder<UpgradingRecipe>`s for you to convert
                recipeList.add(new RustleClientRecipe(holder.id().identifier(), SlotContent.of(holder.value().input()), SlotContent.of(holder.value().result()), holder.value().effects().swellDuration()));
            });
            ClientRecipeManager.INSTANCE.getRecipesForType(VoidLachrymaRecipe.TYPE).forEach(holder -> { // provides a list of `RecipeHolder<UpgradingRecipe>`s for you to convert
                recipeList.add(new VoidLachrymaClientRecipe(holder.id().identifier(), SlotContent.of(holder.value().input()), SlotContent.of(holder.value().result()), holder.value().averageChancePerSecond()));
            });
        });

        ItemView.addClientReloadCallback(() -> {
            for (var variant : RubbleShieldVariant.VARIANTS) {
                var rubbleShield = EnderscapeItems.RUBBLE_SHIELD.getDefaultInstance();
                rubbleShield.set(EnderscapeDataComponents.RUBBLE_SHIELD_VARIANT, variant);
                ItemView.addStackSensitive(rubbleShield);
            }
        });
    }

    public static void drawRecipeArrow(GuiGraphicsExtractor guiGraphics, AnimationTicker ticker, int x, int y) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, RECIPE_ARROW_FILLED, 22, 16, 0, 0, x, y, (int) (ticker.getProgress() * 22), 16);
    }
}