package net.bunten.enderscape.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.block.MagniaSproutBlock;
import net.bunten.enderscape.block.MagniaSproutBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

import static net.bunten.enderscape.client.registry.EnderscapeDebugScreenEntries.MAGNIA_SPROUT_RANGE;

@Environment(EnvType.CLIENT)
public class MagniaSproutRenderer implements BlockEntityRenderer<MagniaSproutBlockEntity, MagniaSproutState> {

    public MagniaSproutRenderer(BlockEntityRendererProvider.Context context) {}

    private final Minecraft minecraft = Minecraft.getInstance();

    @Override
    public MagniaSproutState createRenderState() {
        return new MagniaSproutState();
    }

    @Override
    public void extractRenderState(MagniaSproutBlockEntity entity, MagniaSproutState sprout, float f, Vec3 vec3, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(entity, sprout, f, vec3, crumblingOverlay);

        BlockState state = entity.getBlockState();
        if (state.getBlock() instanceof MagniaSproutBlock block) {
            sprout.range = MagniaSproutBlockEntity.getRange(entity.getLevel(), state, entity.getBlockPos()).move(entity.getBlockPos().multiply(-1));

            block.getPolarity(state).ifPresent((polarity) -> {
                boolean powered = state.getValue(MagniaSproutBlock.POWERED);
                sprout.color = ARGB.color(powered ? 0.85F : 0.05F, polarity.getRangeHitboxColor());
            });
        }
    }

    @Override
    public void submit(MagniaSproutState state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        if (minecraft.debugEntries.isCurrentlyEnabled(MAGNIA_SPROUT_RANGE)) {
            if (state.range == null) return;

            collector.submitShapeOutline(pose, Shapes.create(state.range), RenderTypes.lines(), state.color, minecraft.getWindow().getAppropriateLineWidth(), false);
        }
    }
}