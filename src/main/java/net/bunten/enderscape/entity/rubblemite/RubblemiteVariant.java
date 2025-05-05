package net.bunten.enderscape.entity.rubblemite;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.tag.EnderscapeBiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public enum RubblemiteVariant implements StringRepresentable {
    END_STONE(0, "end_stone", Blocks.END_STONE, 8, EnderscapeBiomeTags.DOES_NOT_SPAWN_END_STONE_RUBBLEMITES),
    MIRESTONE(1, "mirestone", EnderscapeBlocks.MIRESTONE, 4, EnderscapeBiomeTags.DOES_NOT_SPAWN_MIRESTONE_RUBBLEMITES),
    VERADITE(2, "veradite", EnderscapeBlocks.VERADITE, 1, EnderscapeBiomeTags.DOES_NOT_SPAWN_VERADITE_RUBBLEMITES),
    KURODITE(3, "kurodite", EnderscapeBlocks.KURODITE, 1, EnderscapeBiomeTags.DOES_NOT_SPAWN_KURODITE_RUBBLEMITES);

    public static final EntityDataAccessor<Integer> DATA = SynchedEntityData.defineId(Rubblemite.class, EntityDataSerializers.INT);
    public static final String KEY = "RubblemiteVariant";

    public static final Codec<RubblemiteVariant> CODEC = StringRepresentable.fromEnum(RubblemiteVariant::values);
    public static final RubblemiteVariant[] BY_ID = Arrays.stream(RubblemiteVariant.values()).sorted(Comparator.comparingInt(RubblemiteVariant::getId)).toArray(RubblemiteVariant[]::new);

    private final int id;
    private final String name;
    private final ItemLike dropItem;
    private final int weight;
    private final TagKey<Biome> skippedBiomes;

    RubblemiteVariant(int id, String name, ItemLike dropItem, int weight, TagKey<Biome> skippedBiomes) {
        this.id = id;
        this.name = name;
        this.dropItem = dropItem;
        this.weight = weight;
        this.skippedBiomes = skippedBiomes;
    }

    public int getId() {
        return id;
    }

    public static RubblemiteVariant byId(int id) {
        return id >= 0 && id < BY_ID.length ? BY_ID[id] : END_STONE;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getSerializedName() {
        return getName();
    }

    public ItemLike getDropItem() {
        return dropItem;
    }

    public int getWeight() {
        return weight;
    }

    public TagKey<Biome> getSkippedBiomes() {
        return skippedBiomes;
    }

    public static RubblemiteVariant get(Rubblemite mob) {
        return RubblemiteVariant.BY_ID[mob.getEntityData().get(DATA)];
    }

    public static void set(Rubblemite mob, RubblemiteVariant variant) {
        mob.getEntityData().set(DATA, variant.getId());
    }

    public static RubblemiteVariant pickForSpawning(RandomSource random, Holder<Biome> biome) {
        int totalWeight = Arrays.stream(RubblemiteVariant.values()).filter(variant -> !biome.is(variant.getSkippedBiomes())).mapToInt(RubblemiteVariant::getWeight).sum();
        int roll = random.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (RubblemiteVariant variant : RubblemiteVariant.values()) {
            if (biome.is(variant.getSkippedBiomes())) continue;
            cumulativeWeight += variant.getWeight();
            if (roll < cumulativeWeight) return variant;
        }

        throw new IllegalStateException("No variant could be selected");
    }
}