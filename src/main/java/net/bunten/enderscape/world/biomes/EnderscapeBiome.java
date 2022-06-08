package net.bunten.enderscape.world.biomes;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;

import net.bunten.enderscape.Enderscape;
import net.minecraft.util.Identifier;

public interface EnderscapeBiome {

    public String getName();

    public BCLBiome getBCLBiome();

    default Identifier getIdentifier() {
        return Enderscape.id(getName());
    }
}