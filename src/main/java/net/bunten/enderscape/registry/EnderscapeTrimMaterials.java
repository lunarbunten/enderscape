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
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnderscapeTrimMaterials {

    public static final HashMap<ResourceKey<TrimMaterial>, Float> TRIM_MATERIALS = new LinkedHashMap<>();

    public static final float NEBULITE_VALUE = 0.632F;
    public static final float SHADOLINE_VALUE = 0.743F;

    public static final ResourceKey<TrimMaterial> NEBULITE = register("nebulite", NEBULITE_VALUE);
    public static final ResourceKey<TrimMaterial> SHADOLINE = register("shadoline", SHADOLINE_VALUE);

    public void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, NEBULITE, EnderscapeItems.NEBULITE.get(), Style.EMPTY.withColor(0xFF66FF), NEBULITE_VALUE);
        register(context, SHADOLINE, EnderscapeItems.SHADOLINE_INGOT.get(), Style.EMPTY.withColor(0x315B4D), SHADOLINE_VALUE);
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, Item item, Style style, float f) {
        ResourceLocation location = key.location();
        TrimMaterial material = TrimMaterial.create(location.getPath(), item, f, Component.translatable(Util.makeDescriptionId("trim_material", location)).withStyle(style), Map.of());
        context.register(key, material);
    }

    private static ResourceKey<TrimMaterial> register(String name, float value) {
        ResourceKey<TrimMaterial> key = ResourceKey.create(Registries.TRIM_MATERIAL, Enderscape.id(name));
        TRIM_MATERIALS.put(key, value);
        return key;
    }
}