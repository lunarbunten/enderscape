package net.bunten.enderscape.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.function.Supplier;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.world.item.Items.CHORUS_FRUIT;

public class EnderscapeDataMapProvider extends DataMapProvider {
    public EnderscapeDataMapProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider());
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var compost = builder(NeoForgeDataMaps.COMPOSTABLES);
        registerCompostableItem(compost, 0.1F, VEILED_LEAF_PILE);
        registerCompostableItem(compost, 0.3F, WISP_SPROUTS);
        registerCompostableItem(compost, 0.3F, CELESTIAL_CAP);
        registerCompostableItem(compost, 0.3F, CELESTIAL_GROWTH);
        registerCompostableItem(compost, 0.3F, () -> CHORUS_FRUIT);
        registerCompostableItem(compost, 0.3F, CORRUPT_GROWTH);
        registerCompostableItem(compost, 0.3F, MURUBLIGHT_CAP);
        registerCompostableItem(compost, 0.3F, VEILED_LEAVES);
        registerCompostableItem(compost, 0.5F, BLINKLIGHT);
        registerCompostableItem(compost, 0.5F, CHORUS_SPROUTS);
        registerCompostableItem(compost, 0.5F, DRY_END_GROWTH);
        registerCompostableItem(compost, 0.5F, WISP_GROWTH);
        registerCompostableItem(compost, 0.5F, FLANGER_BERRY);
        registerCompostableItem(compost, 0.5F, MURUBLIGHT_BRACKET);
        registerCompostableItem(compost, 0.5F, VEILED_SAPLING);
        registerCompostableItem(compost, 0.65F, BULB_FLOWER);
        registerCompostableItem(compost, 0.65F, CELESTIAL_CHANTERELLE);
        registerCompostableItem(compost, 0.65F, MURUBLIGHT_CHANTERELLE);
        registerCompostableItem(compost, 0.65F, RIPE_FLANGER_BERRY_BLOCK);
        registerCompostableItem(compost, 0.65F, WISP_FLOWER);

        var fuel = builder(NeoForgeDataMaps.FURNACE_FUELS);
        fuel.add(BuiltInRegistries.ITEM.getKey(VOID_SHALE.get().asItem()), new FurnaceFuel(200), false);
    }

    private void registerCompostableItem(Builder<Compostable, Item> builder, float v, Supplier<? extends ItemLike> wispFlower) {
        var item = wispFlower.get().asItem();
        var key = BuiltInRegistries.ITEM.getKey(item);
        builder.add(key, new Compostable(v, false), false);
    }
}
