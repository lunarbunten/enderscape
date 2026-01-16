package net.bunten.enderscape.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.bunten.enderscape.Enderscape;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.ArmorType;
import net.ramixin.mixson.inline.Mixson;

import java.util.Arrays;
import java.util.List;

public final class EnderscapeAssetModifications  {

    private static final List<Identifier> ARMOR_MATERIALS = List.of(
            Identifier.withDefaultNamespace("leather"),
            Identifier.withDefaultNamespace("chainmail"),
            Identifier.withDefaultNamespace("iron"),
            Identifier.withDefaultNamespace("golden"),
            Identifier.withDefaultNamespace("diamond"),
            Identifier.withDefaultNamespace("netherite"),
            Identifier.withDefaultNamespace("copper"),
            Enderscape.id("shadoline")
    );

    static {
        Arrays.stream(ArmorType.values()).filter(type -> type != ArmorType.BODY).forEach(type -> ARMOR_MATERIALS.forEach(material -> registerItemModelModification(type.getName(), material)));

        registerItemModelModification("helmet", Identifier.withDefaultNamespace("turtle"));
        registerItemModelModification("leggings", Enderscape.id("drift"));

        registerTrimMaterialsToAtlas("armor_trims");
        registerTrimMaterialsToAtlas("items");

        registerTrimPatternTextures();
    }

    private static void registerItemModelModification(String armorPieceType, Identifier armorMaterial) {
        Mixson.registerEvent(
                1,
                Identifier.fromNamespaceAndPath(armorMaterial.getNamespace(), "items/" + armorMaterial.getPath() + "_" + armorPieceType).toString(),
                Enderscape.id("add_trims_to_" + armorMaterial.getPath() + "_" + armorPieceType).toString(),
                (context) -> {
                    JsonObject root = context.getFile().getAsJsonObject();
                    if (root == null || !root.has("model")) return;

                    JsonObject model = root.getAsJsonObject("model");
                    if (model == null) return;

                    if (!model.has("type") || !model.get("type").getAsString().equals("minecraft:select")) return;

                    JsonArray cases = model.getAsJsonArray("cases");
                    if (cases == null || cases.isEmpty()) return;

                    EnderscapeTrimMaterials.TRIM_MATERIALS.forEach(material -> {
                        JsonObject newCase = new JsonObject();
                        newCase.addProperty("when", material.identifier().toString());

                        JsonObject caseModel = new JsonObject();
                        caseModel.addProperty("type", "minecraft:model");
                        caseModel.addProperty("model", Enderscape.id("item/" + armorMaterial.getPath() + "_" + armorPieceType + "_" + material.identifier().getPath() + "_trim").toString());

                        newCase.add("model", caseModel);
                        cases.add(newCase);
                    });
                }
        );
    }

    private static void registerTrimPatternTextures() {
        Mixson.registerEvent(
                1,
                Identifier.withDefaultNamespace("atlases/armor_trims").toString(),
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
                                String name = pattern.identifier().getPath();
                                textures.add(Enderscape.id("trims/entity/humanoid/" + name).toString());
                                textures.add(Enderscape.id("trims/entity/humanoid_leggings/" + name).toString());
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
                Identifier.withDefaultNamespace("atlases/" + atlasName).toString(),
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

                            EnderscapeTrimMaterials.TRIM_MATERIALS.forEach(trimMaterial -> {
                                String trimName = trimMaterial.identifier().getPath();
                                permutations.addProperty(trimName, Enderscape.id("trims/color_palettes/" + trimName).toString());
                            });

                            break;
                        }
                    }
                }
        );
    }
}