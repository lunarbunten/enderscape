package net.enderscape.interfaces;

public interface LayerMapped {
    LayerType getLayerType();

    enum LayerType {
        CUTOUT, CUTOUT_MIPPED, TRANSLUCENT
    }
}