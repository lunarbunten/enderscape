package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnderscapePaintingVariants {

    public static final List<ResourceKey<PaintingVariant>> PAINTING_VARIANTS = new ArrayList<>();

    public static final ResourceKey<PaintingVariant> GRAPE_STATIC = register("grape_static");

    public static void bootstrap(BootstrapContext<PaintingVariant> context) {
        register(context, GRAPE_STATIC, 3, 3);
    }

    private static void register(BootstrapContext<PaintingVariant> context, ResourceKey<PaintingVariant> key, int width, int height) {
        context.register(key, new PaintingVariant(
                width,
                height,
                key.location(),
                Optional.of(Component.translatable(key.location().toLanguageKey("painting", "title")).withStyle(ChatFormatting.YELLOW)),
                Optional.of(Component.translatable(key.location().toLanguageKey("painting", "author")).withStyle(ChatFormatting.GRAY))
        ));
    }

    private static ResourceKey<PaintingVariant> register(String name) {
        ResourceKey<PaintingVariant> key = ResourceKey.create(Registries.PAINTING_VARIANT, Enderscape.id(name));
        PAINTING_VARIANTS.add(key);
        return key;
    }
}