package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnderscapeTrimMaterials {

    public static final List<ResourceKey<TrimMaterial>> TRIM_MATERIALS = new ArrayList<>();

    public static final ResourceKey<TrimMaterial> NEBULITE = register("nebulite");
    public static final ResourceKey<TrimMaterial> SHADOLINE = register("shadoline");

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, NEBULITE, EnderscapeItems.NEBULITE, Style.EMPTY.withColor(0xFF66FF));
        register(context, SHADOLINE, EnderscapeItems.SHADOLINE_INGOT, Style.EMPTY.withColor(0x315B4D));
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, Item item, Style style) {
        ResourceLocation location = key.location();
        TrimMaterial material = TrimMaterial.create(location.getPath(), item, Component.translatable(Util.makeDescriptionId("trim_material", location)).withStyle(style), Map.of());
        context.register(key, material);
    }

    private static ResourceKey<TrimMaterial> register(String name) {
        ResourceKey<TrimMaterial> key = ResourceKey.create(Registries.TRIM_MATERIAL, Enderscape.id(name));
        TRIM_MATERIALS.add(key);
        return key;
    }
}