package net.bunten.enderscape.datagen;

import net.bunten.enderscape.registry.EnderscapeNoiseParameters;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.concurrent.CompletableFuture;

public class EnderscapeNoiseProvider extends FabricDynamicRegistryProvider {

    public EnderscapeNoiseProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        EnderscapeNoiseParameters.NOISE_PARAMETERS.forEach(noiseParametersResourceKey -> add(provider, entries, noiseParametersResourceKey));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<NormalNoise.NoiseParameters> key) {
        entries.add(key, provider.lookupOrThrow(Registries.NOISE).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Noise";
    }
}
