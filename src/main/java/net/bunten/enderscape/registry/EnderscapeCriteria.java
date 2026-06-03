package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.criteria.*;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class EnderscapeCriteria {

    public static final BounceOnDrifterCriterion BOUNCE_ON_DRIFTER = register("bounce_on_drifter", new BounceOnDrifterCriterion());
    public static final DashJumpCriterion DASH_JUMP = register("dash_jump", new DashJumpCriterion());
    public static final HearMagniaRadioSongCriterion HEAR_MAGNIA_RADIO_SONG = register("hear_magnia_radio_song", new HearMagniaRadioSongCriterion());
    public static final LodestoneTeleportationCriterion LODESTONE_TELEPORTATION = register("lodestone_teleportation", new LodestoneTeleportationCriterion());
    public static final PullEntityCriterion PULL_ENTITY = register("pull_entity", new PullEntityCriterion());
    public static final StunAttackCriterion STUN_ATTACK = register("stun_attack", new StunAttackCriterion());

    private static <T extends CriterionTrigger<?>> T register(String name, T criterion) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Enderscape.id(name), criterion);
    }
}