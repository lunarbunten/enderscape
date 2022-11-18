package net.bunten.enderscape.criteria;

import net.bunten.enderscape.criteria.MirrorTeleportCriterion.TeleportType;
import net.minecraft.advancements.CriteriaTriggers;

public class EnderscapeCriteria {

    public static final HighsAndLowsCriterion HIGHS_AND_LOWS = CriteriaTriggers.register(new HighsAndLowsCriterion("highs_and_lows"));
    public static final LiquifyNebuliteCriterion LIQUIFY_NEBULITE = CriteriaTriggers.register(new LiquifyNebuliteCriterion());
    public static final MirrorTeleportCriterion MIRROR_TELEPORT_ANY = CriteriaTriggers.register(new MirrorTeleportCriterion(TeleportType.ANY));
    public static final MirrorTeleportCriterion MIRROR_TELEPORT_DIFFERENT = CriteriaTriggers.register(new MirrorTeleportCriterion(TeleportType.DIFFERENT));
    public static final MirrorTeleportCriterion MIRROR_TELEPORT_SAME = CriteriaTriggers.register(new MirrorTeleportCriterion(TeleportType.SAME));
 
}