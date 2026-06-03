package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeEnchantments.*;
import static net.minecraft.tags.EnchantmentTags.*;

public class EnderscapeEnchantmentTagProvider extends FabricTagsProvider<Enchantment> {

    public EnderscapeEnchantmentTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.ENCHANTMENT, future);
    }

    protected TagAppender<Enchantment> tag(TagKey<Enchantment> key) {
        return TagAppender.forBuilder(getOrCreateRawBuilder(key));
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(TOOLTIP_ORDER).add(
                TRANSDIMENSIONAL,
                BUNDLING,
                RESONANCE
        );
        tag(IN_ENCHANTING_TABLE).add(BUNDLING, RESONANCE);
        tag(TREASURE).add(REBOUND, TRANSDIMENSIONAL);
    }
}