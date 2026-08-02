package net.penumbra.enderscape.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.effect.MobEffects.ABSORPTION;
import static net.penumbra.enderscape.registry.tag.EnderscapeMobEffectTags.UNSUPPORTED_WITH_VOIDED_HEALTH;

public class EnderscapeMobEffectTagProvider extends FabricTagsProvider<MobEffect> {

    public EnderscapeMobEffectTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.MOB_EFFECT, future);
    }

    protected TagAppender<ResourceKey<MobEffect>, MobEffect> tag(TagKey<MobEffect> key) {
        return TagAppender.forBuilder(getOrCreateRawBuilder(key));
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(UNSUPPORTED_WITH_VOIDED_HEALTH).add(keyFor(ABSORPTION));
    }

    private static ResourceKey<MobEffect> keyFor(Holder<MobEffect> effect) {
        return effect.unwrapKey().get();
    }
}