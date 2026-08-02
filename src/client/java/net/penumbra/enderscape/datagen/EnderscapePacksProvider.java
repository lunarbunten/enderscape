package net.penumbra.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.DetectedVersion;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.ResourceFilterSection;
import net.minecraft.util.IdentifierPattern;
import net.penumbra.enderscape.registry.EnderscapePacks;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class EnderscapePacksProvider {
    static void initialize(FabricDataGenerator generator) {
        createPack(generator, PackType.CLIENT_RESOURCES, EnderscapePacks.IMPROVED_VISUALS);

        createPack(generator, PackType.SERVER_DATA, EnderscapePacks.FIX_LEVITATION_ADVANCEMENT);
        createPack(generator, EnderscapePacks.FIX_VANILLA_RECIPES, fixVanillaRecipesMetadata());

        createPack(generator, PackType.SERVER_DATA, EnderscapePacks.NEW_END_CITIES);
        createPack(generator, PackType.SERVER_DATA, EnderscapePacks.NEW_STRONGHOLDS);
        createPack(generator, PackType.SERVER_DATA, EnderscapePacks.NEW_TERRAIN);
    }

    private static FabricDataGenerator.Pack createPack(FabricDataGenerator generator, PackType type, Identifier identifier) {
        FabricDataGenerator.Pack pack = generator.createBuiltinResourcePack(identifier);
        pack.addProvider(createPackMetadata(type, identifier));
        return pack;
    }

    private static FabricDataGenerator.Pack createPack(FabricDataGenerator generator, Identifier identifier, FabricDataGenerator.Pack.Factory<PackMetadataGenerator> packMetadata) {
        FabricDataGenerator.Pack pack = generator.createBuiltinResourcePack(identifier);
        pack.addProvider(packMetadata);
        return pack;
    }

    private static FabricDataGenerator.Pack.Factory<PackMetadataGenerator> createPackMetadata(PackType type, Identifier identifier) {
        return output -> {
            PackMetadataGenerator metadata = new PackMetadataGenerator(output);

            metadata.add(PackMetadataSection.forPackType(type), new PackMetadataSection(
                    Component.translatable(String.format("pack.%s.%s.description", identifier.getNamespace(), identifier.getPath())),
                    DetectedVersion.BUILT_IN.packVersion(type).minorRange()
            ));

            return metadata;
        };
    }

    private static FabricDataGenerator.Pack.Factory<PackMetadataGenerator> fixVanillaRecipesMetadata() {
        return output -> {
            PackMetadataGenerator metadata = new PackMetadataGenerator(output);

            Identifier identifier = EnderscapePacks.FIX_VANILLA_RECIPES;
            PackType type = PackType.SERVER_DATA;

            metadata.add(PackMetadataSection.forPackType(type), new PackMetadataSection(
                    Component.translatable(String.format("pack.%s.%s.description", identifier.getNamespace(), identifier.getPath())),
                    DetectedVersion.BUILT_IN.packVersion(type).minorRange()
            ));

            metadata.add(ResourceFilterSection.TYPE, new ResourceFilterSection(List.of(
                    new IdentifierPattern(Optional.of(Pattern.compile("minecraft")), Optional.of(Pattern.compile("recipe/end_stone_bricks\\.json")))
            )));

            return metadata;
        };
    }
}
