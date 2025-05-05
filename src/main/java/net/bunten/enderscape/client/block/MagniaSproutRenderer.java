package net.bunten.enderscape.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.block.MagniaSproutBlock;
import net.bunten.enderscape.block.MagniaSproutBlockEntity;
import net.bunten.enderscape.block.properties.MagniaType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class MagniaSproutRenderer implements BlockEntityRenderer<MagniaSproutBlockEntity> {

    public MagniaSproutRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(MagniaSproutBlockEntity entity, float f, PoseStack pose, MultiBufferSource source, int i, int j, Vec3 cameraPos) {
        if (EnderscapeConfig.getInstance().debugMagniaSproutHitboxes) {
            if (entity.getBlockState().getBlock() instanceof MagniaSproutBlock sprout) {
                Vec3 color = sprout.magniaType.equals(MagniaType.ALLURING) ? Vec3.fromRGB24(0x8CC9FF) : Vec3.fromRGB24(0xFF9E9B);
                ShapeRenderer.renderLineBox(pose, source.getBuffer(RenderType.lines()), MagniaSproutBlockEntity.getRange(entity.getLevel(), entity.getBlockState(), entity.getBlockPos()).move(entity.getBlockPos().multiply(-1)), (float) color.x, (float) color.y, (float) color.z, entity.getBlockState().getValue(MagniaSproutBlock.POWERED) ? 0.85F : 0.05F);
            }
        }
    }
}