package net.penumbra.enderscape.datagen.model;

import net.minecraft.client.data.models.model.TextureSlot;

public class EnderscapeTextureSlots {

    public static final TextureSlot ALL_EMISSIVE = create("all_emissive", TextureSlot.ALL);
    public static final TextureSlot END_EMISSIVE = create("end_emissive", TextureSlot.END);
    public static final TextureSlot SIDE_EMISSIVE = create("side_emissive", TextureSlot.SIDE);

    public static TextureSlot create(final String id, final TextureSlot parent) {
        return new TextureSlot(id, parent);
    }

    public static TextureSlot create(final String id) {
        return new TextureSlot(id, null);
    }
}