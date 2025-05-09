package net.bunten.enderscape.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.bunten.enderscape.Enderscape;
import net.minecraft.resources.ResourceLocation;
import net.ramixin.mixson.inline.Mixson;

import java.util.ArrayList;
import java.util.List;

public final class EnderscapeAssetModifications  {
    private static final List<String> ARMOR_PIECE_TYPES = List.of("helmet", "chestplate", "leggings", "boots");

    private static final List<ResourceLocation> ARMOR_MATERIALS = List.of(
            ResourceLocation.withDefaultNamespace("leather"),
            ResourceLocation.withDefaultNamespace("chainmail"),
            ResourceLocation.withDefaultNamespace("iron"),
            ResourceLocation.withDefaultNamespace("golden"),
            ResourceLocation.withDefaultNamespace("diamond"),
            ResourceLocation.withDefaultNamespace("netherite")
    );

    static {
        ARMOR_PIECE_TYPES.forEach(armorType -> ARMOR_MATERIALS.forEach(material -> registerItemModelModification(armorType, material)));

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
                    JsonObject rootJson = context.getFile().getAsJsonObject();
                    JsonArray overridesArray = rootJson.getAsJsonArray("overrides");

                    EnderscapeTrimMaterials.TRIM_MATERIALS.forEach((trimMaterial, value) -> {
                        JsonObject newCase = new JsonObject();
                        JsonObject predicate = new JsonObject();

                        predicate.addProperty("trim_type", value);
                        newCase.add("predicate", predicate);
                        newCase.addProperty("model", Enderscape.id("item/" + armorMaterial.getPath() + "_" + armorPieceType + "_" + trimMaterial.location().getPath() + "_trim").toString());
                        overridesArray.add(newCase);
                    });

                    List<JsonElement> overrideList = new ArrayList<>();
                    for (JsonElement override : overridesArray) overrideList.add(override);

                    overrideList.sort((a, b) -> Float.compare(a.getAsJsonObject().getAsJsonObject("predicate").get("trim_type").getAsFloat(), b.getAsJsonObject().getAsJsonObject("predicate").get("trim_type").getAsFloat()));

                    JsonArray sortedOverrides = new JsonArray();
                    overrideList.forEach(sortedOverrides::add);

                    rootJson.add("overrides", sortedOverrides);
                }
        );
    }


    private static void registerTrimPatternTextures() {
        Mixson.registerEvent(
                1,
                ResourceLocation.withDefaultNamespace("atlases/armor_trims").toString(),
                Enderscape.id("add_trim_patterns_to_armor_trims_atlas").toString(),
                (context) -> {
                    JsonObject rootJson = context.getFile().getAsJsonObject();
                    JsonArray sourcesArray = rootJson.getAsJsonArray("sources");

                    for (JsonElement sourceElement : sourcesArray) {
                        JsonObject sourceObject = sourceElement.getAsJsonObject();

                        String type = sourceObject.get("type").getAsString();
                        if ("paletted_permutations".equals(type) || "minecraft:paletted_permutations".equals(type)) {
                            JsonArray texturesArray = sourceObject.getAsJsonArray("textures");

                            EnderscapeTrimPatterns.TRIM_PATTERNS.forEach(pattern -> {
                                String name = pattern.location().getPath();
                                texturesArray.add(Enderscape.id("trims/models/armor/" + name).toString());
                                texturesArray.add(Enderscape.id("trims/models/armor/" + name + "_leggings").toString());
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
                    JsonObject rootJson = context.getFile().getAsJsonObject();
                    JsonArray sourcesArray = rootJson.getAsJsonArray("sources");

                    for (JsonElement sourceElement : sourcesArray) {
                        JsonObject sourceObject = sourceElement.getAsJsonObject();

                        String type = sourceObject.get("type").getAsString();
                        if ("paletted_permutations".equals(type) || "minecraft:paletted_permutations".equals(type)) {
                            JsonObject permutationsJson = sourceObject.getAsJsonObject("permutations");

                            EnderscapeTrimMaterials.TRIM_MATERIALS.forEach((material, value) -> {
                                String trimName = material.location().getPath();
                                permutationsJson.addProperty(trimName, Enderscape.id("trims/color_palettes/" + trimName).toString());
                            });

                            break;
                        }
                    }
                }
        );
    }
}