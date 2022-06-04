package net.bunten.enderscape.world.biomes;

import net.bunten.enderscape.Enderscape;
import net.minecraft.util.Identifier;
import ru.bclib.world.biomes.BCLBiome;

public interface EnderscapeBiome {

    public String getName();

    public BCLBiome getBCLBiome();

    default Identifier getIdentifier() {
        return Enderscape.id(getName());
    }
}