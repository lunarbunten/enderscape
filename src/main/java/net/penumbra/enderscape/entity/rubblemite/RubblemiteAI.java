package net.penumbra.enderscape.entity.rubblemite;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.penumbra.enderscape.entity.ai.EnderscapeAI;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.ai.EnderscapeSensors;
import net.penumbra.enderscape.entity.ai.behavior.ConditionalLookAtTargetSink;
import net.penumbra.enderscape.entity.ai.behavior.rubblemite.RubblemiteDashAfterPreparing;
import net.penumbra.enderscape.entity.ai.behavior.rubblemite.RubblemiteManageState;
import net.penumbra.enderscape.entity.ai.behavior.rubblemite.RubblemitePrepareDashDuringCombat;
import net.penumbra.enderscape.entity.ai.behavior.rubblemite.RubblemiteShellCooldown;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class RubblemiteAI {

    public static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            EnderscapeMemory.ANGRY_AT,
            EnderscapeMemory.ATTACK_COOLING_DOWN,
            EnderscapeMemory.ATTACK_TARGET,
            EnderscapeMemory.AVOID_TARGET,
            EnderscapeMemory.CANT_REACH_WALK_TARGET_SINCE,
            EnderscapeMemory.HURT_BY_ENTITY,
            EnderscapeMemory.LOOK_TARGET,
            EnderscapeMemory.NEAREST_ATTACKABLE,
            EnderscapeMemory.NEAREST_LIVING_ENTITIES,
            EnderscapeMemory.NEAREST_VISIBLE_LIVING_ENTITIES,
            EnderscapeMemory.NEAREST_VISIBLE_PLAYER,
            EnderscapeMemory.PATH,
            EnderscapeMemory.RUBBLEMITE_PREPARING_DASH,
            EnderscapeMemory.RUBBLEMITE_PREPARING_DASH_TIME,
            EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN,
            EnderscapeMemory.RUBBLEMITE_HIDING_DURATION,
            EnderscapeMemory.RUBBLEMITE_HIDING_ON_COOLDOWN,
            EnderscapeMemory.WALK_TARGET
    );

    public static final ImmutableList<SensorType<? extends Sensor<? super Rubblemite>>> SENSOR_TYPES = ImmutableList.of(
            EnderscapeSensors.HURT_BY,
            EnderscapeSensors.NEAREST_LIVING_ENTITIES,
            EnderscapeSensors.NEAREST_PLAYERS,
            EnderscapeSensors.RUBBLEMITE_NEAREST_ATTACKABLE
    );

    public static List<ActivityData<Rubblemite>> getActivities(final Rubblemite body) {
        return List.of(
                initCoreActivity(),
                initIdleActivity(),
                initFightActivity()
        );
    }

    public static void updateActivity(Rubblemite mob) {
        mob.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

    private static ActivityData<Rubblemite> initCoreActivity() {
        return ActivityData.create(Activity.CORE, 0, ImmutableList.of(
                new MoveToTargetSink(),
                new CountDownCooldownTicks(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH_TIME),
                new ConditionalLookAtTargetSink<>(rubblemite -> rubblemite.isDashing() || rubblemite.isInsideShell(), 45, 90),
                new RubblemiteShellCooldown(),
                new RubblemiteManageState(),
                StopBeingAngryIfTargetDead.create()
            )
        );
    }

    private static ActivityData<Rubblemite> initIdleActivity() {
        return ActivityData.create(Activity.IDLE, ImmutableList.of(
            Pair.of(1, StartAttacking.create(EnderscapeAI::getAttackTarget)),
            Pair.of(4, SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 6.0f, UniformInt.of(30, 60))),
            Pair.of(8, new RunOne<>(
                    ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                    ImmutableList.of(
                            Pair.of(RandomStroll.stroll(1), 3),
                            Pair.of(new DoNothing(30, 60), 3)
                        )
                    )
                )
            )
        );
    }

    private static ActivityData<Rubblemite> initFightActivity() {
        return ActivityData.create(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        new RubblemiteDashAfterPreparing(),
                        new RubblemitePrepareDashDuringCombat(),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1),
                        BehaviorBuilder.triggerIf(mob -> mob.getState() == Rubblemite.State.IDLING, MeleeAttack.create(15)),
                        StopAttackingIfTargetInvalid.create()
                ),
                EnderscapeMemory.ATTACK_TARGET
        );
    }

    public static boolean isShellCoolingDown(Rubblemite mob) {
        Brain<Rubblemite> brain = mob.getBrain();

        if (brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_HIDING_ON_COOLDOWN)) {
            Optional<Boolean> cooling = brain.getMemory(EnderscapeMemory.RUBBLEMITE_HIDING_ON_COOLDOWN);
            return cooling.orElse(false);
        }

        return false;
    }
}