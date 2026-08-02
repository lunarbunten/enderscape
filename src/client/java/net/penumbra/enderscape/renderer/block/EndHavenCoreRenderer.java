package net.penumbra.enderscape.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.block.EndHavenCoreBlock;
import net.penumbra.enderscape.block.EndHavenCoreBlockEntity;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EndHavenCoreRenderer implements BlockEntityRenderer<EndHavenCoreBlockEntity, EndHavenCoreRenderState> {

    private static final List<Direction> ALL_FACES = List.of(Direction.values());

    private static final Vector3fc FROM = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3fc TO = new Vector3f(1.0F, 1.0F, 1.0F);

    private static final float CUBE_SCALE = 0.9F;

    private static final Map<Direction, List<Vector3fc>> FACES = Util.makeEnumMap(
            Direction.class,
            direction -> {
                FaceInfo faceInfo = FaceInfo.fromFacing(direction);
                return List.of(
                        faceInfo.getVertexInfo(0).select(FROM, TO),
                        faceInfo.getVertexInfo(1).select(FROM, TO),
                        faceInfo.getVertexInfo(2).select(FROM, TO),
                        faceInfo.getVertexInfo(3).select(FROM, TO)
                );
            }
    );

    public EndHavenCoreRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public EndHavenCoreRenderState createRenderState() {
        return new EndHavenCoreRenderState();
    }

    public void extractRenderState(EndHavenCoreBlockEntity entity, final EndHavenCoreRenderState state, final float partialTicks, final Vec3 camera, final ModelFeatureRenderer.CrumblingOverlay progress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, camera, progress);

        state.active = EndHavenCoreBlock.isActive(entity.getBlockState());
        state.facesToShow.clear();

        if (state.active) {
            for (Direction direction : Direction.values()) {
                if (entity.shouldRenderFace(direction)) {
                    state.facesToShow.add(direction);
                }
            }
        }
    }

    @Override
    public void submit(final EndHavenCoreRenderState state, final PoseStack pose, final SubmitNodeCollector collector, final CameraRenderState camera) {
        submitCube(state.facesToShow, RenderTypes.endGateway(), pose, collector);
    }

    protected static void submitCube(final Collection<Direction> faces, final RenderType type, final PoseStack poseStack, final SubmitNodeCollector collector) {
        if (!faces.isEmpty()) {
            float offset = (1.0F - CUBE_SCALE) / 2.0F;
            poseStack.translate(offset, offset, offset);
            poseStack.scale(CUBE_SCALE, CUBE_SCALE, CUBE_SCALE);

            collector.submitCustomGeometry(poseStack, type, (pose, buffer) -> {
                for (Direction direction : faces) {
                    for (Vector3fc faceVertex : FACES.get(direction)) {
                        buffer.addVertex(pose, faceVertex);
                    }
                }
            });
        }
    }

    public static void submitSpecial(final RenderType type, final PoseStack pose, final SubmitNodeCollector collector) {
        submitCube(ALL_FACES, type, pose, collector);
    }

    public static void getExtents(final Consumer<Vector3fc> output) {
        FACES.values().forEach(vertices -> vertices.forEach(output));
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}