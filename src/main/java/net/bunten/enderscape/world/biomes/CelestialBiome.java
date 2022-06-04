package net.bunten.enderscape.world.biomes;

import net.bunten.enderscape.Enderscape;

public interface CelestialBiome {

    default int getFogColor() {
        return Enderscape.hasBetterEnd() ? 0xCC59B5 : 0x20192D;
    }
 
    default float getFogDensity() {
        return Enderscape.hasBetterEnd() ? 1.75F : 1;
    }
}