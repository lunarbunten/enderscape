package net.enderscape.registry;

import net.enderscape.criteria.LiquifyNebuliteCriterion;
import net.enderscape.criteria.MirrorTeleportCriterion;
import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;

public class EndCriteria {

    public static final LiquifyNebuliteCriterion LIQUIFY_NEBULITE = CriterionRegistry.register(new LiquifyNebuliteCriterion());
    public static final MirrorTeleportCriterion MIRROR_TELEPORT = CriterionRegistry.register(new MirrorTeleportCriterion());

    public static void init() {
    }
}