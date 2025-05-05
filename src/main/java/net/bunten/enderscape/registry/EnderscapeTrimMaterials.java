package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnderscapeTrimMaterials {

    public static final List<ResourceKey<TrimMaterial>> TRIM_MATERIALS = new ArrayList<>();

    public static final ResourceKey<TrimMaterial> NEBULITE = register("nebulite");
    public static final ResourceKey<TrimMaterial> SHADOLINE = register("shadoline");

    public static MaterialAssetGroup create(String string) {
        return new MaterialAssetGroup(new MaterialAssetGroup.AssetInfo(string), Map.of());
    }

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, NEBULITE, Style.EMPTY.withColor(0xFF66FF));
        register(context, SHADOLINE, Style.EMPTY.withColor(0x315B4D));
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, Style style) {
        Component component = Component.translatable(Util.makeDescriptionId("trim_material", key.location())).withStyle(style);
        context.register(key, new TrimMaterial(MaterialAssetGroup.create(key.location().getPath()), component));
    }

    private static ResourceKey<TrimMaterial> register(String name) {
        ResourceKey<TrimMaterial> key = ResourceKey.create(Registries.TRIM_MATERIAL, Enderscape.id(name));
        TRIM_MATERIALS.add(key);
        return key;
    }
}