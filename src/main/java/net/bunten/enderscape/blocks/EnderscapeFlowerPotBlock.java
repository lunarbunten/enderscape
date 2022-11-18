package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.HasRenderType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;

public class EnderscapeFlowerPotBlock extends FlowerPotBlock implements HasRenderType {
    public EnderscapeFlowerPotBlock(Block content, Properties settings) {
        super(content, settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}