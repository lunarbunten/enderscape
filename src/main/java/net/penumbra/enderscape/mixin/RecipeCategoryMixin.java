package net.penumbra.enderscape.mixin;

import net.minecraft.data.recipes.RecipeCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RecipeCategory.class)
public enum RecipeCategoryMixin {
    ENDERSCAPE_RUSTLE("rustle"),
    ENDERSCAPE_VOID_LACHRYMA("void_lachryma");

    @Shadow
    RecipeCategoryMixin(final String recipeFolderName) {
    }
}