package net.penumbra.enderscape.registry.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.item.crafting.MirrorDyingRecipe;
import net.penumbra.enderscape.item.crafting.RustleRecipe;
import net.penumbra.enderscape.item.crafting.ToolFuelingRecipe;
import net.penumbra.enderscape.item.crafting.VoidLachrymaRecipe;

public class EnderscapeRecipeSerializers {

    public static final RecipeSerializer<MirrorDyingRecipe> MIRROR_DYING = register("mirror_dying", MirrorDyingRecipe.MAP_CODEC, MirrorDyingRecipe.STREAM_CODEC);
    public static final RecipeSerializer<RustleRecipe> RUSTLE = register("rustle", RustleRecipe.MAP_CODEC, RustleRecipe.STREAM_CODEC);
    public static final RecipeSerializer<ToolFuelingRecipe> TOOL_FUELING = register("tool_fueling", ToolFuelingRecipe.MAP_CODEC, ToolFuelingRecipe.STREAM_CODEC);
    public static final RecipeSerializer<VoidLachrymaRecipe> VOID_LACHRYMA = register("void_lachryma", VoidLachrymaRecipe.MAP_CODEC, VoidLachrymaRecipe.STREAM_CODEC);

    private static <T extends Recipe<?>> RecipeSerializer<T> register(String name, MapCodec<T> mapCodec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        RecipeSerializer<T> serializer = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Enderscape.id(name), new RecipeSerializer<>(mapCodec, streamCodec));
        RecipeSynchronization.synchronizeRecipeSerializer(serializer);
        return serializer;
    }
}