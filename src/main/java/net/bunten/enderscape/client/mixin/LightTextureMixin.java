package net.bunten.enderscape.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.ARGB;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(LightTexture.class)
public class LightTextureMixin {

    @Shadow @Mutable @Final
    private static final Vector3f END_FLASH_SKY_LIGHT_COLOR = ARGB.vector3fFromRGB24(0x3B1159);
}