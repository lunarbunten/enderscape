package net.penumbra.enderscape.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.item.crafting.value.RustleRecipeEffects;
import net.penumbra.enderscape.registry.item.EnderscapeRecipeSerializers;

public class RustleRecipe extends SingleItemRecipe {

    public static final MapCodec<RustleRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    CommonInfo.MAP_CODEC.forGetter(RustleRecipe::commonInfo),
                    Ingredient.CODEC.fieldOf("input").forGetter(RustleRecipe::input),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(RustleRecipe::result),
                    RustleRecipeEffects.CODEC.fieldOf("effects").forGetter(RustleRecipe::effects),
                    Codec.FLOAT.fieldOf("experience").forGetter(RustleRecipe::experience)
            ).apply(instance, RustleRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RustleRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, RustleRecipe::commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC, RustleRecipe::input,
            ItemStackTemplate.STREAM_CODEC, RustleRecipe::result,
            RustleRecipeEffects.STREAM_CODEC, RustleRecipe::effects,
            ByteBufCodecs.FLOAT, RustleRecipe::experience,
            RustleRecipe::new
    );

    public static final RecipeBookCategory CATEGORY = Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, Enderscape.id("rustle"), new RecipeBookCategory());
    public static final RecipeType<RustleRecipe> TYPE = Registry.register(BuiltInRegistries.RECIPE_TYPE, Enderscape.id("rustle"), new RecipeType<RustleRecipe>() {

        public String toString() {
            return "enderscape_rustle";
        }
    });

    private final RustleRecipeEffects effects;
    private final float experience;

    public RustleRecipe(
            final CommonInfo commonInfo,
            final Ingredient input,
            final ItemStackTemplate result,
            final RustleRecipeEffects effects,
            final float experience
    ) {
        super(commonInfo, input, result);
        this.effects = effects;
        this.experience = experience;
    }

    public CommonInfo commonInfo() {
        return commonInfo;
    }

    public RustleRecipeEffects effects() {
        return effects;
    }

    public float experience() {
        return experience;
    }

    @Override
    public ItemStackTemplate result() {
        return super.result();
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return EnderscapeRecipeSerializers.RUSTLE;
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return TYPE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return CATEGORY;
    }
}
