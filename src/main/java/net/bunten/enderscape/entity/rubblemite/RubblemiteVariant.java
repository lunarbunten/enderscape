package net.bunten.enderscape.entity.rubblemite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Optional;

public record RubblemiteVariant(Optional<ResourceKey<LootTable>> extraDropItems, RubblemiteVariant.AssetInfo assetInfo, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {

    public static final Codec<RubblemiteVariant> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            LootTable.KEY_CODEC.optionalFieldOf("extra_drop_items").forGetter(RubblemiteVariant::extraDropItems),
                            RubblemiteVariant.AssetInfo.CODEC.fieldOf("assets").forGetter(RubblemiteVariant::assetInfo),
                            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(RubblemiteVariant::spawnConditions)
                    ).apply(instance, RubblemiteVariant::new)
    );

    public static final Codec<RubblemiteVariant> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(RubblemiteVariant.AssetInfo.CODEC.fieldOf("assets").forGetter(RubblemiteVariant::assetInfo)).apply(instance, RubblemiteVariant::new)
    );

    public static final Codec<Holder<RubblemiteVariant>> CODEC = RegistryFixedCodec.create(EnderscapeRegistries.RUBBLEMITE_VARIANT);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<RubblemiteVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(EnderscapeRegistries.RUBBLEMITE_VARIANT);

    private RubblemiteVariant(RubblemiteVariant.AssetInfo assetInfo) {
        this(Optional.empty(), assetInfo, SpawnPrioritySelectors.EMPTY);
    }

    public static Optional<? extends Holder<RubblemiteVariant>> selectVariantToSpawn(RandomSource random, RegistryAccess access, SpawnContext context) {
        return PriorityProvider.pick(access.lookupOrThrow(EnderscapeRegistries.RUBBLEMITE_VARIANT).listElements(), Holder::value, random, context);
    }

    @Override
    public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
        return spawnConditions.selectors();
    }

    public record AssetInfo(ClientAsset asset) {
        public static final Codec<RubblemiteVariant.AssetInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(ClientAsset.CODEC.fieldOf("asset").forGetter(RubblemiteVariant.AssetInfo::asset)).apply(instance, RubblemiteVariant.AssetInfo::new));
    }
}