package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.minecraft.world.gen.feature.FeatureConfig;

public class MurushroomFeatureConfig implements FeatureConfig {
    public static final Codec<MurushroomFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (Codec.intRange(1, 64).fieldOf("horizontalRange")).forGetter(config -> config.horizontalRange),
            (Codec.intRange(1, 64).fieldOf("verticalRange")).forGetter(config -> config.verticalRange),
            (Codec.intRange(1, MurushroomsBlock.MAX_AGE).fieldOf("age")).forGetter(config -> config.age),
            (Codec.intRange(1, 512).fieldOf("tries")).forGetter(config -> config.tries))
            .apply(instance, MurushroomFeatureConfig::new));

    public final int horizontalRange;
    public final int verticalRange;
    public final int age;
    public final int tries;

    public MurushroomFeatureConfig(int horizontalRange, int verticalRange, int age, int tries) {
        this.horizontalRange = horizontalRange;
        this.verticalRange = verticalRange;
        this.age = age;
        this.tries = tries;
    }
}