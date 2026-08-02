package net.penumbra.enderscape.datagen.model;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.penumbra.enderscape.Enderscape;

import java.util.Optional;

import static net.minecraft.client.data.models.model.TextureSlot.*;
import static net.penumbra.enderscape.datagen.model.EnderscapeTextureSlots.*;

public class EnderscapeModelTemplates {

    public static final ModelTemplate TEMPLATE_BRACKET_1 = create("template_bracket_1", ALL);
    public static final ModelTemplate TEMPLATE_BRACKET_2 = create("template_bracket_2", ALL);
    public static final ModelTemplate TEMPLATE_BRACKET_3 = create("template_bracket_3", ALL);
    public static final ModelTemplate TEMPLATE_BRACKET_4 = create("template_bracket_4", ALL);
    public static final ModelTemplate TEMPLATE_EMISSIVE_GROWTH = create("template_emissive_growth", CROSS, CROSS_EMISSIVE);
    public static final ModelTemplate TEMPLATE_END_HAVEN_CORE = create("template_end_haven_core", TextureSlot.END, TextureSlot.SIDE, SIDE_EMISSIVE);

    public static final ModelTemplate TEMPLATE_MAGNIA_RADIO = create("template_magnia_radio", ALL, ALL_EMISSIVE);
    public static final ModelTemplate TEMPLATE_MAGNIA_SPROUT_POWERED = create("template_magnia_sprout_powered", CROSS, CROSS_EMISSIVE);
    public static final ModelTemplate TEMPLATE_NEBULITE_ORE = create("template_nebulite_ore", ALL, ALL_EMISSIVE);
    public static final ModelTemplate TEMPLATE_EMISSIVE_OVERGROWN_BRICKS = create("template_emissive_overgrown_bricks", ALL, ALL_EMISSIVE);
    public static final ModelTemplate TEMPLATE_OVERGROWTH_1 = create("template_overgrowth_1", TOP, SIDE, BOTTOM);
    public static final ModelTemplate TEMPLATE_OVERGROWTH_2 = create("template_overgrowth_2", TOP, SIDE, BOTTOM);
    public static final ModelTemplate TEMPLATE_OVERGROWTH_3 = create("template_overgrowth_3", TOP, SIDE, BOTTOM);
    public static final ModelTemplate TEMPLATE_OVERGROWTH_4 = create("template_overgrowth_4", TOP, SIDE, BOTTOM);
    public static final ModelTemplate TEMPLATE_PATH_1 = create("template_path_1", TOP, SIDE, BOTTOM);
    public static final ModelTemplate TEMPLATE_PATH_2 = create("template_path_2", TOP, SIDE, BOTTOM);
    public static final ModelTemplate TEMPLATE_PATH_3 = create("template_path_3", TOP, SIDE, BOTTOM);
    public static final ModelTemplate TEMPLATE_PATH_4 = create("template_path_4", TOP, SIDE, BOTTOM);
    public static final ModelTemplate TEMPLATE_VEILED_VINES = create("template_veiled_vines", CROSS);
    public static final ModelTemplate TEMPLATE_VOID_SHALE = create("template_void_shale", END, SIDE, END_EMISSIVE, SIDE_EMISSIVE);

    public static final ModelTemplate TEMPLATE_MIRROR = createItem("template_mirror", TextureSlot.LAYER0);
    public static final ModelTemplate TEMPLATE_DYED_MIRROR = createItem("template_mirror", TextureSlot.LAYER0, TextureSlot.LAYER1);
    public static final ModelTemplate TEMPLATE_MAGNIA_ATTRACTOR = createItem("template_magnia_attractor", TextureSlot.LAYER0);
    public static final ModelTemplate TEMPLATE_RUBBLE_SHIELD = createItem("template_rubble_shield", TextureSlot.LAYER0);
    public static final ModelTemplate TEMPLATE_RUBBLE_SHIELD_USING = createItem("template_rubble_shield_using", TextureSlot.LAYER0);

    private static ModelTemplate create(final String id, final TextureSlot... slots) {
        return new ModelTemplate(Optional.of(Enderscape.id("block/" + id)), Optional.empty(), slots);
    }

    private static ModelTemplate createItem(final String id, final TextureSlot... slots) {
        return new ModelTemplate(Optional.of(Enderscape.id("item/" + id)), Optional.empty(), slots);
    }
}
