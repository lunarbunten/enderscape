package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.client.entity.EntityRenderData;
import net.bunten.enderscape.client.entity.drifter.DrifterModel;
import net.bunten.enderscape.client.entity.drifter.DrifterRenderer;
import net.bunten.enderscape.client.entity.driftlet.DriftletModel;
import net.bunten.enderscape.client.entity.driftlet.DriftletRenderer;
import net.bunten.enderscape.client.entity.rubblemite.RubblemiteModel;
import net.bunten.enderscape.client.entity.rubblemite.RubblemiteRenderer;
import net.bunten.enderscape.client.entity.rustle.RustleModel;
import net.bunten.enderscape.client.entity.rustle.RustleRenderer;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.bunten.enderscape.entity.drifter.Driftlet;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.bunten.enderscape.entity.rustle.Rustle;
import net.bunten.enderscape.registry.EnderscapeEntities;

public class EnderscapeEntityRenderData {

    public static final EntityRenderData<Drifter> DRIFTER = EntityRenderData.create(EnderscapeEntities.DRIFTER, DrifterRenderer::new, DrifterModel::createLayer);
    public static final EntityRenderData<Driftlet> DRIFTLET = EntityRenderData.create(EnderscapeEntities.DRIFTLET, DriftletRenderer::new, DriftletModel::createLayer);
    public static final EntityRenderData<Rubblemite> RUBBLEMITE = EntityRenderData.create(EnderscapeEntities.RUBBLEMITE, RubblemiteRenderer::new, RubblemiteModel::createLayer);
    public static final EntityRenderData<Rustle> RUSTLE = EntityRenderData.create(EnderscapeEntities.RUSTLE, RustleRenderer::new, RustleModel::createLayer);

}