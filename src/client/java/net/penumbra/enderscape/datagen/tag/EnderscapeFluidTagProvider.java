package net.penumbra.enderscape.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.penumbra.enderscape.registry.block.EnderscapeFluids;

import java.util.concurrent.CompletableFuture;

import static net.penumbra.enderscape.registry.tag.EnderscapeFluidTags.VOID_LACHRYMA;

public class EnderscapeFluidTagProvider extends FabricTagsProvider.FluidTagsProvider {

    public EnderscapeFluidTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        valueLookupBuilder(VOID_LACHRYMA).add(EnderscapeFluids.VOID_LACHRYMA, EnderscapeFluids.FLOWING_VOID_LACHRYMA);
    }
}
