package net.penumbra.enderscape.registry;

import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.Enderscape;

public class EnderscapePacks {

    public static final Identifier IMPROVED_VISUALS = register("improved_visuals");

    public static final Identifier FIX_LEVITATION_ADVANCEMENT = register("fix_levitation_advancement");
    public static final Identifier FIX_VANILLA_RECIPES = register("fix_vanilla_recipes");
    public static final Identifier NEW_END_CITIES = register("new_end_cities");
    public static final Identifier NEW_STRONGHOLDS = register("new_strongholds");
    public static final Identifier NEW_TERRAIN = register("new_terrain");

    private static Identifier register(String name) {
        return Enderscape.id(name);
    }
}