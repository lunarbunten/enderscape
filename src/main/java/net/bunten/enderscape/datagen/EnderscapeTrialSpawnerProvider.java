package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeTrialSpawnerConfigs.TRIAL_SPAWNER_CONFIGS;

public class EnderscapeTrialSpawnerProvider extends FabricDynamicRegistryProvider {

    public EnderscapeTrialSpawnerProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        TRIAL_SPAWNER_CONFIGS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<TrialSpawnerConfig> key) {
        entries.add(key, provider.lookupOrThrow(Registries.TRIAL_SPAWNER_CONFIG).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Trial Spawner Configs";
    }
}