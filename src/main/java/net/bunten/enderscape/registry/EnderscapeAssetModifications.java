package net.bunten.enderscape.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.bunten.enderscape.Enderscape;
import net.minecraft.resources.ResourceLocation;
import net.ramixin.mixson.inline.Mixson;

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
                ResourceLocation.fromNamespaceAndPath(armorMaterial.getNamespace(), "items/" + armorMaterial.getPath() + "_" + armorPieceType).toString(),
                Enderscape.id("add_trims_to_" + armorMaterial.getPath() + "_" + armorPieceType).toString(),
                (context) -> {
                    JsonObject rootJson = context.getFile().getAsJsonObject();
                    JsonObject modelJson = rootJson.getAsJsonObject("model");
                    JsonArray casesArray = modelJson.getAsJsonArray("cases");
                    JsonObject baseCase = casesArray.get(0).getAsJsonObject();

                    EnderscapeTrimMaterials.TRIM_MATERIALS.forEach(trimMaterial -> {
                        JsonObject newCase = baseCase.deepCopy();

                        newCase.addProperty("when", trimMaterial.location().toString());
                        newCase.getAsJsonObject("model").addProperty("model", Enderscape.id("item/" + armorMaterial.getPath() + "_" + armorPieceType + "_" + trimMaterial.location().getPath() + "_trim").toString());

                        casesArray.add(newCase);
                    });
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
                                texturesArray.add(Enderscape.id("trims/entity/humanoid/" + name).toString());
                                texturesArray.add(Enderscape.id("trims/entity/humanoid_leggings/" + name).toString());
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

                            EnderscapeTrimMaterials.TRIM_MATERIALS.forEach(trimMaterial -> {
                                String trimName = trimMaterial.location().getPath();
                                permutationsJson.addProperty(trimName, Enderscape.id("trims/color_palettes/" + trimName).toString());
                            });

                            break;
                        }
                    }
                }
        );
    }
}