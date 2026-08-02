package net.penumbra.enderscape.gui.value;

import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.Enderscape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeartOverlay {

    public static final HeartOverlay VOIDED = new HeartOverlay("voided");
    public static final HeartOverlay VOID_PURIFICATION = new HeartOverlay("void_purification");
    public static final HeartOverlay VOID_PURIFICATION_OUTLINE = new HeartOverlay("void_purification_outline");
    public static final HeartOverlay OUTER_VOID_WARNING = new HeartOverlay("outer_void_warning");

    private final Map<HeartOverlayInfo, Identifier> spriteCache = new HashMap<>();
    private final Identifier folder;

    public HeartOverlay(String name) {
        this.folder = Enderscape.id("hud/heart/" + name + "/");
        cacheSprites();
    }

    public Identifier getSprite(boolean half, boolean hardcore, boolean reversed) {
        return spriteCache.get(new HeartOverlayInfo(half, hardcore, reversed));
    }

    private void cacheSprites() {
        for (boolean half : List.of(true, false)) {
            for (boolean hardcore : List.of(true, false)) {
                for (boolean reversed : List.of(true, false)) {
                    HeartOverlayInfo info = new HeartOverlayInfo(half, hardcore, reversed);
                    spriteCache.put(info, buildSprite(info));
                }
            }
        }
    }

    private Identifier buildSprite(HeartOverlayInfo info) {
        Identifier value = folder;

        if (info.hardcore()) value = value.withSuffix("hardcore/");

        String suffix = info.half() ? (info.reversed() ? "half_reversed" : "half") : "full";

        return value.withSuffix(suffix);
    }

    private record HeartOverlayInfo(
            boolean half,
            boolean hardcore,
            boolean reversed
    ) { }
}