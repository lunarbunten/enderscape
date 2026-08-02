package net.penumbra.enderscape.registry.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.penumbra.enderscape.Enderscape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnderscapeTrimMaterials {

    public static final List<ResourceKey<TrimMaterial>> TRIM_MATERIALS = new ArrayList<>();

    public static final ResourceKey<TrimMaterial> NEBULITE = register("nebulite");
    public static final ResourceKey<TrimMaterial> SHADOLINE = register("shadoline");

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, NEBULITE, Style.EMPTY.withColor(0xFF66FF), MaterialAssetGroup.create("nebulite"));
        register(context, SHADOLINE, Style.EMPTY.withColor(0x315B4D), MaterialAssetGroup.create("shadoline", Map.of(EnderscapeEquipmentAssets.SHADOLINE, "shadoline_lighter")));
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, Style style, MaterialAssetGroup group) {
        Component component = Component.translatable(Util.makeDescriptionId("trim_material", key.identifier())).withStyle(style);
        context.register(key, new TrimMaterial(group, component));
    }

    private static ResourceKey<TrimMaterial> register(String name) {
        ResourceKey<TrimMaterial> key = ResourceKey.create(Registries.TRIM_MATERIAL, Enderscape.id(name));
        TRIM_MATERIALS.add(key);
        return key;
    }
}