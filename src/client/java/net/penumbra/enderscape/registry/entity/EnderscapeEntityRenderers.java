package net.penumbra.enderscape.registry.entity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.renderer.entity.drifter.DrifterRenderer;
import net.penumbra.enderscape.renderer.entity.enderman.ImprovedEndermanRenderer;
import net.penumbra.enderscape.renderer.entity.rubblemite.RubblemiteRenderer;
import net.penumbra.enderscape.renderer.entity.rustle.RustleRenderer;

public class EnderscapeEntityRenderers {

    static {
        EntityRenderers.register(EnderscapeEntities.DRIFTER, DrifterRenderer::new);
        EntityRenderers.register(EnderscapeEntities.RUBBLEMITE, RubblemiteRenderer::new);
        EntityRenderers.register(EnderscapeEntities.RUSTLE, RustleRenderer::new);

        if (EnderscapeConfig.getInstance().endermanUpdateRenderer) {
            EntityRenderers.register(EntityTypes.ENDERMAN, ImprovedEndermanRenderer::new);
        }
    }
}