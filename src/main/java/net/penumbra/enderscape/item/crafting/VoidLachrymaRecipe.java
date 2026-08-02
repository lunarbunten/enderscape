package net.penumbra.enderscape.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.item.EnderscapeRecipeSerializers;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;

public class VoidLachrymaRecipe extends SingleItemRecipe {

    public static final Holder.Reference<SoundEvent> DEFAULT_SOUND = EnderscapeEntitySounds.ITEM_VOID_LACHRYMA_CORRUPTION;
    public static final SimpleParticleType DEFAULT_PARTICLE = EnderscapeParticles.VOID_POOF;
    public static final float DEFAULT_CONVERSION_CHANCE = 1.0F;
    public static final float DEFAULT_MINIMUM_VOIDED_PERCENTAGE = 1.0F;

    public static final MapCodec<VoidLachrymaRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Recipe.CommonInfo.MAP_CODEC.forGetter(VoidLachrymaRecipe::commonInfo),
                    Ingredient.CODEC.fieldOf("input").forGetter(VoidLachrymaRecipe::input),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(VoidLachrymaRecipe::result),
                    SoundEvent.CODEC.optionalFieldOf("sound", DEFAULT_SOUND).forGetter(VoidLachrymaRecipe::sound),
                    ParticleTypes.CODEC.optionalFieldOf("particle", DEFAULT_PARTICLE).forGetter(VoidLachrymaRecipe::particle),
                    ExtraCodecs.floatRange(0.0F, 1.0F).optionalFieldOf("average_chance_per_second", DEFAULT_CONVERSION_CHANCE).forGetter(VoidLachrymaRecipe::averageChancePerSecond),
                    ExtraCodecs.floatRange(0.0F, 1.0F).optionalFieldOf("minimum_voided_percentage", DEFAULT_MINIMUM_VOIDED_PERCENTAGE).forGetter(VoidLachrymaRecipe::minimumVoidedPercentage)
            ).apply(instance, VoidLachrymaRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, VoidLachrymaRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            VoidLachrymaRecipe::commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC,
            VoidLachrymaRecipe::input,
            ItemStackTemplate.STREAM_CODEC,
            VoidLachrymaRecipe::result,
            SoundEvent.STREAM_CODEC,
            VoidLachrymaRecipe::sound,
            ParticleTypes.STREAM_CODEC,
            VoidLachrymaRecipe::particle,
            ByteBufCodecs.FLOAT,
            VoidLachrymaRecipe::averageChancePerSecond,
            ByteBufCodecs.FLOAT,
            VoidLachrymaRecipe::minimumVoidedPercentage,
            VoidLachrymaRecipe::new
    );

    public static final RecipeBookCategory CATEGORY = Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, Enderscape.id("void_lachryma"), new RecipeBookCategory());
    public static final RecipeType<VoidLachrymaRecipe> TYPE = Registry.register(BuiltInRegistries.RECIPE_TYPE, Enderscape.id("void_lachryma"), new RecipeType<VoidLachrymaRecipe>() {

        public String toString() {
            return "enderscape_void_lachryma";
        }
    });

    private final Holder<SoundEvent> sound;
    private final ParticleOptions particle;
    private final float averageChancePerSecond;
    private final float minimumVoidedPercentage;

    public VoidLachrymaRecipe(
            CommonInfo commonInfo,
            Ingredient input,
            ItemStackTemplate result,
            Holder<SoundEvent> sound,
            ParticleOptions particle,
            float averageChancePerSecond,
            float minimumVoidedPercentage
    ) {
        super(commonInfo, input, result);
        this.sound = sound;
        this.particle = particle;
        this.averageChancePerSecond = averageChancePerSecond;
        this.minimumVoidedPercentage = minimumVoidedPercentage;
    }

    @Override
    public ItemStackTemplate result() {
        return super.result();
    }

    public CommonInfo commonInfo() {
        return commonInfo;
    }

    public Holder<SoundEvent> sound() {
        return sound;
    }

    public ParticleOptions particle() {
        return particle;
    }

    public float averageChancePerSecond() {
        return averageChancePerSecond;
    }

    public float minimumVoidedPercentage() {
        return minimumVoidedPercentage;
    }

    public float averageChance() {
        return averageChancePerSecond() / 20.0F;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return EnderscapeRecipeSerializers.VOID_LACHRYMA;
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return TYPE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return CATEGORY;
    }

    public static class Builder {
        private final CommonInfo commonInfo;
        private final Ingredient input;
        private final ItemStackTemplate result;

        private Holder<SoundEvent> sound = DEFAULT_SOUND;
        private ParticleOptions particle = DEFAULT_PARTICLE;
        private float conversionChance = DEFAULT_CONVERSION_CHANCE;
        private float minimumVoidedPercentage = DEFAULT_MINIMUM_VOIDED_PERCENTAGE;

        private Builder(CommonInfo commonInfo, Ingredient inputs, ItemStackTemplate result) {
            this.commonInfo = commonInfo;
            this.input = inputs;
            this.result = result;
        }

        public static Builder create(CommonInfo commonInfo, Ingredient inputs, ItemStackTemplate result) {
            return new Builder(commonInfo, inputs, result);
        }

        public Builder sound(Holder<SoundEvent> sound) {
            this.sound = sound;
            return this;
        }

        public Builder particle(ParticleOptions particle) {
            this.particle = particle;
            return this;
        }

        public Builder conversionChance(float conversionChance) {
            this.conversionChance = conversionChance;
            return this;
        }

        public Builder minimumVoidedPercentage(float minimumVoidedPercentage) {
            this.minimumVoidedPercentage = minimumVoidedPercentage;
            return this;
        }

        public VoidLachrymaRecipe build() {
            return new VoidLachrymaRecipe(
                    commonInfo,
                    input,
                    result,
                    sound,
                    particle,
                    conversionChance,
                    minimumVoidedPercentage
            );
        }
    }
}
