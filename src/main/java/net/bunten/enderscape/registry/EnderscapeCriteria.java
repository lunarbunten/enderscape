package net.bunten.enderscape.registry;

import net.bunten.enderscape.criteria.LiquifyNebuliteCriterion;
import net.bunten.enderscape.criteria.MirrorTeleportCriterion;
import net.minecraft.advancement.criterion.Criteria;

public class EnderscapeCriteria {

    public static final LiquifyNebuliteCriterion LIQUIFY_NEBULITE = Criteria.register(new LiquifyNebuliteCriterion());
    public static final MirrorTeleportCriterion MIRROR_TELEPORT = Criteria.register(new MirrorTeleportCriterion());

    public static void init() {
    }
}