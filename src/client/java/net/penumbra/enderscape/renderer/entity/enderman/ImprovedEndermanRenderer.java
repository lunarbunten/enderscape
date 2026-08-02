package net.penumbra.enderscape.renderer.entity.enderman;

import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BlockDecorationLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.registry.renderer.EnderscapeModelLayers;
import net.penumbra.enderscape.renderer.entity.LightDependantEnderEyesLayer;
import net.penumbra.enderscape.renderer.entity.StoresSkylightFactor;
import org.jspecify.annotations.NonNull;

public class ImprovedEndermanRenderer extends MobRenderer<EnderMan, EndermanRenderState, ImprovedEndermanModel<EndermanRenderState>> {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private static final Identifier ENDERMAN_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderman/enderman.png");
    private final BlockModelResolver blockModelResolver;

    public ImprovedEndermanRenderer(final EntityRendererProvider.Context context) {
        super(context, new ImprovedEndermanModel<>(context.bakeLayer(EnderscapeModelLayers.ENDERMAN)), 0.5F);
        blockModelResolver = context.getBlockModelResolver();

        addLayer(getEyesLayer());
        addLayer(new BlockDecorationLayer<>(this, state -> state.carriedBlock, model::applyCarriedBlockTransform));
    }

    private @NonNull RenderLayer<EndermanRenderState, ImprovedEndermanModel<EndermanRenderState>> getEyesLayer() {
        if (EnderscapeConfig.getInstance().endermanLightSensitiveEyes) {
            return LightDependantEnderEyesLayer.enderman(this);
        } else {
            return new ImprovedEndermanEyesLayer(this);
        }
    }

    @Override
    public Identifier getTextureLocation(final EndermanRenderState state) {
        return ENDERMAN_LOCATION;
    }

    @Override
    public EndermanRenderState createRenderState() {
        return new EndermanRenderState();
    }

    @Override
    public void extractRenderState(final EnderMan entity, final EndermanRenderState state, final float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, itemModelResolver);
        state.isCreepy = entity.isCreepy();
        BlockState carriedBlock = entity.getCarriedBlock();

        if (carriedBlock != null) {
            blockModelResolver.update(state.carriedBlock, carriedBlock, BLOCK_DISPLAY_CONTEXT);
        } else {
            state.carriedBlock.clear();
        }

        ImprovedEndermanRenderState.extract(entity, state);
        StoresSkylightFactor.extract(entity, state);
    }
}
