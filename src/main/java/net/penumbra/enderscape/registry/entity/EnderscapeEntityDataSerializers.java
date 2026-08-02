package net.penumbra.enderscape.registry.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.entity.rubblemite.Rubblemite;
import net.penumbra.enderscape.entity.rubblemite.RubblemiteVariant;
import net.penumbra.enderscape.entity.rustle.RustleConversionPhase;
import net.penumbra.enderscape.item.crafting.RustleRecipe;

import java.util.Optional;

public class EnderscapeEntityDataSerializers {

    public static final EntityDataSerializer<Rubblemite.State> RUBBLEMITE_STATE = EntityDataSerializer.forValueType(Rubblemite.State.STREAM_CODEC);
    public static final EntityDataSerializer<Holder<RubblemiteVariant>> RUBBLEMITE_VARIANT = EntityDataSerializer.forValueType(RubblemiteVariant.STREAM_CODEC);
    public static final EntityDataSerializer<RustleConversionPhase> RUSTLE_CONVERSION_PHASE = EntityDataSerializer.forValueType(RustleConversionPhase.STREAM_CODEC);
    public static final EntityDataSerializer<Optional<RustleRecipe>> OPTIONAL_RUSTLE_RECIPE = EntityDataSerializer.forValueType(RustleRecipe.STREAM_CODEC.apply(ByteBufCodecs::optional));

    static {
        FabricEntityDataRegistry.register(Enderscape.id("rubblemite_state"), RUBBLEMITE_STATE);
        FabricEntityDataRegistry.register(Enderscape.id("rubblemite_variant"), RUBBLEMITE_VARIANT);
        FabricEntityDataRegistry.register(Enderscape.id("rustle_conversion_phase"), RUSTLE_CONVERSION_PHASE);
        FabricEntityDataRegistry.register(Enderscape.id("optional_rustle_recipe"), OPTIONAL_RUSTLE_RECIPE);
    }
}