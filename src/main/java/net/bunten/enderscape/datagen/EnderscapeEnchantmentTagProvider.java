package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.EnderscapeEnchantments.*;
import static net.minecraft.tags.EnchantmentTags.*;

public class EnderscapeEnchantmentTagProvider extends EnchantmentTagsProvider {

    public EnderscapeEnchantmentTagProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), Enderscape.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(TOOLTIP_ORDER).add(
                TRANSDIMENSIONAL,
                RESONANCE
        );
        tag(IN_ENCHANTING_TABLE).add(RESONANCE);
        tag(TREASURE).add(REBOUND, TRANSDIMENSIONAL);
    }
}