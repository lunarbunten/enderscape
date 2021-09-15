package net.enderscape.world.biomes;

/**
 * Template for creating a basic end islands biome.
 *
 * @see EnderscapeBiome
 */
public abstract class EnderscapeIslandsBiome extends EnderscapeBiome {
    private final EnderscapeBiome parent;

    public EnderscapeIslandsBiome(EnderscapeBiome parent, String name) {
        super(name, parent.getMixedNoisePoint());
        this.parent = parent;
    }

    /**
     * The parent biome from which this will share it's mixed noise point.
     */
    public final EnderscapeBiome getParent() {
        return parent;
    }
}