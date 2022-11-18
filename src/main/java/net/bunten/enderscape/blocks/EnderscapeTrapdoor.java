package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.HasRenderType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.TrapDoorBlock;

public class EnderscapeTrapdoor extends TrapDoorBlock implements HasRenderType {
    public EnderscapeTrapdoor(Properties settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}