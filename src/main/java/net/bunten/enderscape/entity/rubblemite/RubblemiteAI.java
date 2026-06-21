package net.bunten.enderscape.entity.rubblemite;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.bunten.enderscape.entity.ai.EnderscapeAI;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.ai.EnderscapeSensors;
import net.bunten.enderscape.entity.ai.behavior.*;
import net.bunten.enderscape.entity.drifter.Drifter;
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

import java.util.Optional;

@SuppressWarnings("deprecation")
public class RubblemiteAI {

    public static final ImmutableList<MemoryModuleType<? extends Object>> MEMORY_TYPES = ImmutableList.of(
            EnderscapeMemory.ANGRY_AT,
            EnderscapeMemory.ATTACK_COOLING_DOWN,
            EnderscapeMemory.ATTACK_TARGET,
            EnderscapeMemory.AVOID_TARGET,
            EnderscapeMemory.CANT_REACH_WALK_TARGET_SINCE,
            EnderscapeMemory.HURT_BY_ENTITY,
            EnderscapeMemory.LOOK_TARGET,
            EnderscapeMemory.NEAREST_ATTACKABLE,
            EnderscapeMemory.NEAREST_ENEMIES,
            EnderscapeMemory.NEAREST_LIVING_ENTITIES,
            EnderscapeMemory.NEAREST_VISIBLE_ATTACKABLE_ENEMY,
            EnderscapeMemory.NEAREST_VISIBLE_ENEMY,
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
            EnderscapeSensors.NEAREST_ITEMS,
            EnderscapeSensors.NEAREST_LIVING_ENTITIES,
            EnderscapeSensors.NEAREST_PLAYERS,
            EnderscapeSensors.RUBBLEMITE_NEAREST_ENEMIES
    );

    private static final Brain.Provider<Rubblemite> BRAIN_PROVIDER = Brain.provider(MEMORY_TYPES, SENSOR_TYPES, entity -> createActivityData());

    public static ImmutableList<ActivityData<Rubblemite>> createActivityData() {
        return ImmutableList.of(
                ActivityData.create(Activity.CORE, 0, ImmutableList.of(
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH_TIME),
                        new ConditionalLookAtTargetSink<>(rubblemite -> rubblemite.isDashing() || rubblemite.isInsideShell(), 45, 90),
                        new RubblemiteShellCooldown(),
                        new RubblemiteManageState(),
                        StopBeingAngryIfTargetDead.create()
                )),
                ActivityData.create(Activity.IDLE, ImmutableList.of(
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
                )),
                ActivityData.create(Activity.FIGHT, 10, ImmutableList.of(
                        new RubblemiteDashAfterPreparing(),
                        new RubblemitePrepareDashDuringCombat(),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1),
                        BehaviorBuilder.triggerIf(mob -> mob.getState() == Rubblemite.State.IDLING, MeleeAttack.create(15)),
                        StopAttackingIfTargetInvalid.create()
                ), EnderscapeMemory.ATTACK_TARGET)
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

    public static Brain<?> makeBrain(Rubblemite rubblemite, Brain.Packed packed) {
        var brain = BRAIN_PROVIDER.makeBrain(rubblemite, packed);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();

        return brain;
    }

    public static void updateActivity(Rubblemite mob) {
        mob.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }
}