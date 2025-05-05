package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.criteria.BounceOnDrifterCriterion;
import net.bunten.enderscape.criteria.MirrorTeleportCriterion;
import net.bunten.enderscape.criteria.PullEntityCriterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class EnderscapeCriteria {

    public static final BounceOnDrifterCriterion BOUNCE_ON_DRIFTER = register("bounce_on_drifter", new BounceOnDrifterCriterion());
    public static final MirrorTeleportCriterion MIRROR_TELEPORT = register("mirror_teleport", new MirrorTeleportCriterion());
    public static final PullEntityCriterion PULL_ENTITY = register("pull_entity", new PullEntityCriterion());

    private static <T extends CriterionTrigger<?>> T register(String name, T criterion) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Enderscape.id(name), criterion);
    }
}