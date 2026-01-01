package net.bunten.enderscape.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.bunten.enderscape.Enderscape;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.ramixin.mixson.inline.Mixson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class EnderscapeAssetModifications  {

    private static final List<ResourceLocation> ARMOR_MATERIALS = List.of(
            ResourceLocation.withDefaultNamespace("leather"),
            ResourceLocation.withDefaultNamespace("chainmail"),
            ResourceLocation.withDefaultNamespace("iron"),
            ResourceLocation.withDefaultNamespace("golden"),
            ResourceLocation.withDefaultNamespace("diamond"),
            ResourceLocation.withDefaultNamespace("netherite"),
            Enderscape.id("shadoline")
    );

    static {
        Arrays.stream(ArmorItem.Type.values()).filter(type -> type != ArmorItem.Type.BODY).forEach(type -> ARMOR_MATERIALS.forEach(material -> registerItemModelModification(type.getName(), material)));

        registerItemModelModification("helmet", ResourceLocation.withDefaultNamespace("turtle"));
        registerItemModelModification("leggings", Enderscape.id("drift"));

        registerTrimMaterialsToAtlas("armor_trims");
        registerTrimMaterialsToAtlas("blocks");

        registerTrimPatternTextures();
    }

    private static void registerItemModelModification(String armorPieceType, ResourceLocation armorMaterial) {
        Mixson.registerEvent(
                1,
                ResourceLocation.fromNamespaceAndPath(armorMaterial.getNamespace(), "models/item/" + armorMaterial.getPath() + "_" + armorPieceType).toString(),
                Enderscape.id("add_trims_to_" + armorMaterial.getPath() + "_" + armorPieceType).toString(),
                context -> {
                    JsonObject root = context.getFile().getAsJsonObject();
                    if (root == null || !root.has("overrides")) return;

                    JsonArray overrides = root.getAsJsonArray("overrides");
                    if (overrides == null || overrides.isEmpty()) return;

                    EnderscapeTrimMaterials.TRIM_MATERIALS.forEach((material, value) -> {
                        JsonObject newCase = new JsonObject();
                        JsonObject predicate = new JsonObject();

                        predicate.addProperty("trim_type", value);
                        newCase.add("predicate", predicate);
                        newCase.addProperty("model", Enderscape.id("item/" + armorMaterial.getPath() + "_" + armorPieceType + "_" + material.location().getPath() + "_trim").toString());
                        overrides.add(newCase);
                    });

                    List<JsonElement> list = new ArrayList<>();
                    for (JsonElement override : overrides) list.add(override);

                    list.sort((a, b) -> {
                        JsonObject objectA = a.getAsJsonObject();
                        JsonObject objectB = b.getAsJsonObject();

                        if (objectA == null || objectB == null) return 0;

                        JsonObject predicateA = objectA.getAsJsonObject("predicate");
                        JsonObject predicateB = objectB.getAsJsonObject("predicate");

                        if (predicateA == null || predicateB == null) return 0;

                        JsonElement trimA = predicateA.get("trim_type");
                        JsonElement trimB = predicateB.get("trim_type");

                        if (trimA == null || trimB == null) return 0;

                        return Float.compare(trimA.getAsFloat(), trimB.getAsFloat());
                    });

                    JsonArray sorted = new JsonArray();
                    list.forEach(sorted::add);

                    root.add("overrides", sorted);
                }
        );
    }

    private static void registerTrimPatternTextures() {
        Mixson.registerEvent(
                1,
                ResourceLocation.withDefaultNamespace("atlases/armor_trims").toString(),
                Enderscape.id("add_trim_patterns_to_armor_trims_atlas").toString(),
                (context) -> {
                    JsonObject root = context.getFile().getAsJsonObject();
                    if (root == null || !root.has("sources")) return;

                    JsonArray sources = root.getAsJsonArray("sources");
                    if (sources == null || sources.isEmpty()) return;

                    for (JsonElement element : sources) {
                        if (!element.isJsonObject()) continue;

                        JsonObject object = element.getAsJsonObject();
                        if (object == null || !object.has("type")) continue;

                        String type = object.get("type").getAsString();

                        if ("paletted_permutations".equals(type) || "minecraft:paletted_permutations".equals(type)) {
                            JsonArray textures = object.getAsJsonArray("textures");
                            if (textures == null || textures.isEmpty()) return;

                            EnderscapeTrimPatterns.TRIM_PATTERNS.forEach(pattern -> {
                                String name = pattern.location().getPath();
                                textures.add(Enderscape.id("trims/models/armor/" + name).toString());
                                textures.add(Enderscape.id("trims/models/armor/" + name + "_leggings").toString());
                            });

                            break;
                        }
                    }
                }
        );
    }

    private static void registerTrimMaterialsToAtlas(String atlasName) {
        Mixson.registerEvent(
                1,
                ResourceLocation.withDefaultNamespace("atlases/" + atlasName).toString(),
                Enderscape.id("add_trim_materials_to_" + atlasName + "_atlas").toString(),
                (context) -> {
                    JsonObject root = context.getFile().getAsJsonObject();
                    if (root == null || !root.has("sources")) return;

                    JsonArray sources = root.getAsJsonArray("sources");
                    if (sources == null || sources.isEmpty()) return;

                    for (JsonElement element : sources) {
                        if (!element.isJsonObject()) continue;

                        JsonObject object = element.getAsJsonObject();
                        if (object == null || !object.has("type")) return;

                        String type = object.get("type").getAsString();

                        if ("paletted_permutations".equals(type) || "minecraft:paletted_permutations".equals(type)) {
                            JsonObject permutations = object.getAsJsonObject("permutations");
                            if (permutations == null || permutations.isEmpty()) return;

                            EnderscapeTrimMaterials.TRIM_MATERIALS.forEach((material, value) -> {
                                String trimName = material.location().getPath();
                                permutations.addProperty(trimName, Enderscape.id("trims/color_palettes/" + trimName).toString());
                            });

                            break;
                        }
                    }
                }
        );
    }
}