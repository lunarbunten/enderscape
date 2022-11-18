package net.bunten.enderscape.world;

import net.bunten.enderscape.world.surface.CelestialSurfaceRule;
import net.bunten.enderscape.world.surface.CorruptSurfaceRule;
import net.bunten.enderscape.world.surface.DefaultEndSurfaceRule;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;

public class EnderscapeRuleSources {
    
    public static final RuleSource DEFAULT_END_SURFACE = new DefaultEndSurfaceRule().surface().build();
    public static final RuleSource CELESTIAL_SURFACE = new CelestialSurfaceRule().surface().build();
    public static final RuleSource CORRUPT_SURFACE = new CorruptSurfaceRule().surface().build();

}