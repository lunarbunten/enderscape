package net.bunten.enderscape.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.block.MagniaSproutBlock;
import net.bunten.enderscape.block.MagniaSproutBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class MagniaSproutRenderer implements BlockEntityRenderer<MagniaSproutBlockEntity> {

    public MagniaSproutRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(MagniaSproutBlockEntity entity, float f, PoseStack pose, MultiBufferSource source, int i, int j, Vec3 cameraPos) {
        if (EnderscapeConfig.getInstance().debugMagniaSproutHitboxes) {
            BlockState state = entity.getBlockState();
            if (state.getBlock() instanceof MagniaSproutBlock sprout && sprout.getPolarity(state).isPresent()) {
                Vec3 color = Vec3.fromRGB24(sprout.getPolarity(state).get().getRangeHitboxColor());
                ShapeRenderer.renderLineBox(pose, source.getBuffer(RenderType.lines()), MagniaSproutBlockEntity.getRange(entity.getLevel(), state, entity.getBlockPos()).move(entity.getBlockPos().multiply(-1)), (float) color.x, (float) color.y, (float) color.z, state.getValue(MagniaSproutBlock.POWERED) ? 0.85F : 0.05F);
            }
        }
    }
}