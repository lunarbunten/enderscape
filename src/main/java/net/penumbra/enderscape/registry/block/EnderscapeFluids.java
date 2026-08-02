package net.penumbra.enderscape.registry.block;

import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.penumbra.enderscape.block.fluid.VoidLachrymaFluid;
import net.penumbra.enderscape.references.EnderscapeFluidIds;

public class EnderscapeFluids {

    public static final FlowingFluid FLOWING_VOID_LACHRYMA = register(EnderscapeFluidIds.FLOWING_VOID_LACHRYMA, new VoidLachrymaFluid.Flowing());
    public static final FlowingFluid VOID_LACHRYMA = register(EnderscapeFluidIds.VOID_LACHRYMA, new VoidLachrymaFluid.Source());

    public static final CauldronInteraction.Dispatcher VOID_LACHRYMA_CAULDRON_INTERACTION = CauldronInteractions.newDispatcher("enderscape_void_lachryma");

    private static <T extends Fluid> T register(ResourceKey<Fluid> key, T fluid) {
        return Registry.register(BuiltInRegistries.FLUID, key, fluid);
    }
}