package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.client.entity.drifter.DrifterRenderer;
import net.bunten.enderscape.client.entity.rubblemite.RubblemiteRenderer;
import net.bunten.enderscape.client.entity.rustle.RustleRenderer;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class EnderscapeEntityRenderers {

    static {
        EntityRenderers.register(EnderscapeEntities.RUBBLEMITE, RubblemiteRenderer::new);
        EntityRenderers.register(EnderscapeEntities.RUSTLE, RustleRenderer::new);
        EntityRenderers.register(EnderscapeEntities.DRIFTER, DrifterRenderer::new);
    }
}