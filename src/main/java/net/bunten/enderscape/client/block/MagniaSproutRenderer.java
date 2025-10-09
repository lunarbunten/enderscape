package net.bunten.enderscape.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.block.MagniaSproutBlock;
import net.bunten.enderscape.block.MagniaSproutBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class MagniaSproutRenderer implements BlockEntityRenderer<MagniaSproutBlockEntity, MagniaSproutState> {

    public MagniaSproutRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public MagniaSproutState createRenderState() {
        return new MagniaSproutState();
    }

    @Override
    public void extractRenderState(MagniaSproutBlockEntity entity, MagniaSproutState state, float f, Vec3 vec3, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(entity, state, f, vec3, crumblingOverlay);

        BlockState blockState = entity.getBlockState();
        if (blockState.getBlock() instanceof MagniaSproutBlock sprout) {
            state.range = MagniaSproutBlockEntity.getRange(entity.getLevel(), blockState, entity.getBlockPos()).move(entity.getBlockPos().multiply(-1));
            state.color = Vec3.fromRGB24(sprout.getPolarity(blockState).get().getRangeHitboxColor());
            state.intensity = blockState.getValue(MagniaSproutBlock.POWERED) ? 0.85F : 0.05F;
        }
    }

    @Override
    public void submit(MagniaSproutState state, PoseStack pose, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (EnderscapeConfig.getInstance().debugMagniaSproutHitboxes) {
            if (state.range == null) return;
            submitNodeCollector.submitCustomGeometry(pose, RenderType.lines(), (pose1, vertexConsumer) -> {
                ShapeRenderer.renderLineBox(pose1, vertexConsumer, state.range, (float) state.color.x, (float) state.color.y, (float) state.color.z, state.intensity);
            });
        }
    }
}