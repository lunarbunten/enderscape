package net.penumbra.enderscape.registry.entity;

import net.minecraft.core.ClientAsset;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.entity.rubblemite.RubblemiteVariant;
import net.penumbra.enderscape.registry.EnderscapeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.penumbra.enderscape.registry.entity.EnderscapeEntityLootTables.*;
import static net.penumbra.enderscape.registry.tag.EnderscapeBlockTags.*;

public class EnderscapeRubblemiteVariants {

    public static final List<ResourceKey<RubblemiteVariant>> RUBBLEMITE_VARIANTS = new ArrayList<>();

    public static final ResourceKey<RubblemiteVariant> END_STONE = register("end_stone");
    public static final ResourceKey<RubblemiteVariant> MIRESTONE = register("mirestone");
    public static final ResourceKey<RubblemiteVariant> VERADITE = register("veradite");
    public static final ResourceKey<RubblemiteVariant> KURODITE = register("kurodite");
    public static final ResourceKey<RubblemiteVariant> DEFAULT = END_STONE;

    private static final SpawnPrioritySelectors FALLBACK_SELECTOR = SpawnPrioritySelectors.fallback(0);

    public static void bootstrap(BootstrapContext<RubblemiteVariant> context) {
        register(context, END_STONE, RUBBLEMITE_END_STONE, FALLBACK_SELECTOR);
        register(context, MIRESTONE, RUBBLEMITE_MIRESTONE, RUBBLEMITE_MIRESTONE_VARIANTS_SPAWN_ON);
        register(context, VERADITE, RUBBLEMITE_VERADITE, RUBBLEMITE_VERADITE_VARIANTS_SPAWN_ON);
        register(context, KURODITE, RUBBLEMITE_KURODITE, RUBBLEMITE_KURODITE_VARIANTS_SPAWN_ON);
    }

    private static ResourceKey<RubblemiteVariant> register(String string) {
        ResourceKey<RubblemiteVariant> key = ResourceKey.create(EnderscapeRegistries.RUBBLEMITE_VARIANT, Enderscape.id(string));
        RUBBLEMITE_VARIANTS.add(key);
        return key;
    }

    private static void register(BootstrapContext<RubblemiteVariant> context, ResourceKey<RubblemiteVariant> key, ResourceKey<LootTable> table, TagKey<Block> blocks) {
        register(context, key, table, spawnsOnBlocks(blocks));
    }

    private static void register(BootstrapContext<RubblemiteVariant> context, ResourceKey<RubblemiteVariant> key, ResourceKey<LootTable> table, SpawnPrioritySelectors selectors) {
        context.register(key, new RubblemiteVariant(Optional.of(table), new RubblemiteVariant.AssetInfo(new ClientAsset.ResourceTexture(Enderscape.id("entity/rubblemite/rubblemite_" + key.identifier().getPath()))), selectors));
    }

    private static SpawnPrioritySelectors spawnsOnBlocks(TagKey<Block> blocks) {
        return SpawnPrioritySelectors.single(new EnderscapeSpawnConditionTypes.FloorCheck(blocks), 1);
    }
}
