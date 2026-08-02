package net.penumbra.enderscape.datagen.model;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

public class EnderscapeTextureMapping {

    public static TextureMapping blinklamp(Block block, String suffix) {
        return new TextureMapping().put(TextureSlot.ALL, getBlockTexture(block, suffix));
    }

    public static TextureMapping plant(Block block, String suffix) {
        return new TextureMapping().put(TextureSlot.PLANT, getBlockTexture(block, suffix));
    }

    public static TextureMapping crossSuffixed(Block block, String suffix) {
        return new TextureMapping().put(TextureSlot.CROSS, getBlockTexture(block, suffix));
    }

    public static TextureMapping magniaRadio(final Block block, String suffix) {
        return new TextureMapping()
                .put(TextureSlot.ALL, getBlockTexture(block, suffix))
                .put(EnderscapeTextureSlots.ALL_EMISSIVE, getBlockTexture(block, suffix + "_emissive"));
    }

    public static TextureMapping emissiveCross(Block block, String suffix) {
        return new TextureMapping()
                .put(TextureSlot.CROSS, getBlockTexture(block, suffix))
                .put(TextureSlot.CROSS_EMISSIVE, getBlockTexture(block, suffix + "_emissive"));
    }

    public static TextureMapping overgrowth(final Block block, final Block baseStone) {
        return new TextureMapping()
                .put(TextureSlot.TOP, getBlockTexture(block, "_top"))
                .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
                .put(TextureSlot.BOTTOM, getBlockTexture(baseStone));
    }

    public static TextureMapping voidShale(final Block block, String suffix) {
        return new TextureMapping()
                .put(TextureSlot.SIDE, getBlockTexture(block, "_side" + suffix))
                .put(TextureSlot.END, getBlockTexture(block, "_end" + suffix))
                .put(EnderscapeTextureSlots.SIDE_EMISSIVE, getBlockTexture(block, "_side" + suffix + "_emissive"))
                .put(EnderscapeTextureSlots.END_EMISSIVE, getBlockTexture(block, "_end" + suffix + "_emissive"));
    }

    public static TextureMapping endHavenCore(final Block block, String suffix) {
        return new TextureMapping()
                .put(TextureSlot.SIDE, getBlockTexture(block, suffix))
                .put(EnderscapeTextureSlots.SIDE_EMISSIVE, getBlockTexture(block, suffix + "_emissive"))
                .put(TextureSlot.END, getBlockTexture(block, "_end"))
                .put(TextureSlot.PARTICLE, getBlockTexture(block, "_inactive"));
    }

    public static TextureMapping nebuliteOre(final Block parent, final Block block) {
        return new TextureMapping()
                .put(TextureSlot.ALL, getBlockTexture(block))
                .put(EnderscapeTextureSlots.ALL_EMISSIVE, getBlockTexture(parent, "_emissive"));
    }

    public static TextureMapping emissiveAllTextured(final Block block) {
        return new TextureMapping()
                .put(TextureSlot.ALL, getBlockTexture(block))
                .put(EnderscapeTextureSlots.ALL_EMISSIVE, getBlockTexture(block, "_emissive"));
    }

    public static Material getBlockTexture(final Block block) {
        return getBlockTexture(block, "");
    }

    private static @NonNull Material getBlockTexture(Block block, final String suffix) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(block);
        return new Material(id.withPath(path -> "block/" + path + suffix));
    }
}
