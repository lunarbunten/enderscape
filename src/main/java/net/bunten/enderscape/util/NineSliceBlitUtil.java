package net.bunten.enderscape.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class NineSliceBlitUtil {
    public final GuiGraphics guiGraphics;
    private final GuiSpriteManager sprites;
    private final float alpha;

    public NineSliceBlitUtil(GuiSpriteManager guiSpriteManager, GuiGraphics guiGraphics, float alpha) {
        this.sprites = guiSpriteManager;
        this.guiGraphics = guiGraphics;
        this.alpha = alpha;
    }

    public void blitSprite(ResourceLocation resourceLocation, int i, int j, int k, int l, int m) {
        TextureAtlasSprite textureAtlasSprite = this.sprites.getSprite(resourceLocation);
        GuiSpriteScaling guiSpriteScaling = this.sprites.getSpriteScaling(textureAtlasSprite);
        if (guiSpriteScaling instanceof GuiSpriteScaling.NineSlice nineSlice) {
            this.blitNineSlicedSprite(textureAtlasSprite, nineSlice, i, j, k, l, m);
        }
    }

    private void blitNineSlicedSprite(TextureAtlasSprite textureAtlasSprite, GuiSpriteScaling.NineSlice nineSlice, int i, int j, int k, int l, int m) {
        GuiSpriteScaling.NineSlice.Border border = nineSlice.border();
        int n = Math.min(border.left(), l / 2);
        int o = Math.min(border.right(), l / 2);
        int p = Math.min(border.top(), m / 2);
        int q = Math.min(border.bottom(), m / 2);
        if (l == nineSlice.width() && m == nineSlice.height()) {
            this.blitSprite(textureAtlasSprite, nineSlice.width(), nineSlice.height(), 0, 0, i, j, k, l, m);
        } else if (m == nineSlice.height()) {
            this.blitSprite(textureAtlasSprite, nineSlice.width(), nineSlice.height(), 0, 0, i, j, k, n, m);
            this.blitTiledSprite(
                    textureAtlasSprite, i + n, j, k, l - o - n, m, n, 0, nineSlice.width() - o - n, nineSlice.height(), nineSlice.width(), nineSlice.height()
            );
            this.blitSprite(textureAtlasSprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - o, 0, i + l - o, j, k, o, m);
        } else if (l == nineSlice.width()) {
            this.blitSprite(textureAtlasSprite, nineSlice.width(), nineSlice.height(), 0, 0, i, j, k, l, p);
            this.blitTiledSprite(
                    textureAtlasSprite, i, j + p, k, l, m - q - p, 0, p, nineSlice.width(), nineSlice.height() - q - p, nineSlice.width(), nineSlice.height()
            );
            this.blitSprite(textureAtlasSprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - q, i, j + m - q, k, l, q);
        } else {
            this.blitSprite(textureAtlasSprite, nineSlice.width(), nineSlice.height(), 0, 0, i, j, k, n, p);
            this.blitTiledSprite(textureAtlasSprite, i + n, j, k, l - o - n, p, n, 0, nineSlice.width() - o - n, p, nineSlice.width(), nineSlice.height());
            this.blitSprite(textureAtlasSprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - o, 0, i + l - o, j, k, o, p);
            this.blitSprite(textureAtlasSprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - q, i, j + m - q, k, n, q);
            this.blitTiledSprite(
                    textureAtlasSprite, i + n, j + m - q, k, l - o - n, q, n, nineSlice.height() - q, nineSlice.width() - o - n, q, nineSlice.width(), nineSlice.height()
            );
            this.blitSprite(textureAtlasSprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - o, nineSlice.height() - q, i + l - o, j + m - q, k, o, q);
            this.blitTiledSprite(textureAtlasSprite, i, j + p, k, n, m - q - p, 0, p, n, nineSlice.height() - q - p, nineSlice.width(), nineSlice.height());
            this.blitTiledSprite(
                    textureAtlasSprite,
                    i + n,
                    j + p,
                    k,
                    l - o - n,
                    m - q - p,
                    n,
                    p,
                    nineSlice.width() - o - n,
                    nineSlice.height() - q - p,
                    nineSlice.width(),
                    nineSlice.height()
            );
            this.blitTiledSprite(
                    textureAtlasSprite, i + l - o, j + p, k, n, m - q - p, nineSlice.width() - o, p, o, nineSlice.height() - q - p, nineSlice.width(), nineSlice.height()
            );
        }
    }

    private void blitTiledSprite(TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l, int m, int n, int o, int p, int q, int r, int s) {
        if (l > 0 && m > 0) {
            if (p > 0 && q > 0) {
                for (int t = 0; t < l; t += p) {
                    int u = Math.min(p, l - t);

                    for (int v = 0; v < m; v += q) {
                        int w = Math.min(q, m - v);
                        this.blitSprite(textureAtlasSprite, r, s, n, o, i + t, j + v, k, u, w);
                    }
                }
            } else {
                throw new IllegalArgumentException("Tiled sprite texture size must be positive, got " + p + "x" + q);
            }
        }
    }

    private void blitSprite(TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l, int m, int n, int o, int p, int q) {
        if (p != 0 && q != 0) {
            this.guiGraphics.innerBlit(
                    textureAtlasSprite.atlasLocation(),
                    m,
                    m + p,
                    n,
                    n + q,
                    o,
                    textureAtlasSprite.getU((float)k / i),
                    textureAtlasSprite.getU((float)(k + p) / i),
                    textureAtlasSprite.getV((float)l / j),
                    textureAtlasSprite.getV((float)(l + q) / j),
                    1.0F,
                    1.0F,
                    1.0F,
                    this.alpha
            );
        }
    }

}
