package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeEnchantments.*;
import static net.minecraft.tags.EnchantmentTags.*;

public class EnderscapeEnchantmentTagProvider extends FabricTagProvider<Enchantment> {

    public EnderscapeEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.ENCHANTMENT, future);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(TOOLTIP_ORDER).add(
                TRANSDIMENSIONAL,
                RESONANCE
        );
        getOrCreateTagBuilder(IN_ENCHANTING_TABLE).add(RESONANCE);
        getOrCreateTagBuilder(TREASURE).add(REBOUND, TRANSDIMENSIONAL);
    }
}