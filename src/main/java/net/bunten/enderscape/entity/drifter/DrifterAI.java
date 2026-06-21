package net.bunten.enderscape.entity.drifter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.ai.EnderscapeSensors;
import net.bunten.enderscape.entity.ai.behavior.CalmDownFromAttacker;
import net.bunten.enderscape.entity.ai.behavior.CalmDownFromIntimidator;
import net.bunten.enderscape.entity.ai.behavior.DrifterRefreshHomePosition;
import net.bunten.enderscape.entity.ai.behavior.DrifterStartOrStopLeakingJelly;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class DrifterAI {

    public static final ImmutableList<MemoryModuleType<? extends Object>> MEMORY_TYPES = ImmutableList.of(
            EnderscapeMemory.AVOID_TARGET,
            EnderscapeMemory.BREED_TARGET,
            EnderscapeMemory.CANT_REACH_WALK_TARGET_SINCE,
            EnderscapeMemory.DRIFTER_FIND_HOME_COOLDOWN,
            EnderscapeMemory.DRIFTER_JELLY_CHANGE_COOLDOWN,
            EnderscapeMemory.HURT_BY_ENTITY,
            EnderscapeMemory.IS_PANICKING,
            EnderscapeMemory.IS_TEMPTED,
            EnderscapeMemory.LOOK_TARGET,
            EnderscapeMemory.NEAREST_ATTACKABLE,
            EnderscapeMemory.NEAREST_INTIMIDATOR,
            EnderscapeMemory.NEAREST_LIVING_ENTITIES,
            EnderscapeMemory.NEAREST_VISIBLE_ADULT,
            EnderscapeMemory.NEAREST_VISIBLE_LIVING_ENTITIES,
            EnderscapeMemory.NEAREST_VISIBLE_PLAYER,
            EnderscapeMemory.PATH,
            EnderscapeMemory.TEMPTATION_COOLDOWN_TICKS,
            EnderscapeMemory.TEMPTING_PLAYER,
            EnderscapeMemory.WALK_TARGET
    );

    public static final ImmutableList<SensorType<? extends Sensor<? super Drifter>>> SENSOR_TYPES = ImmutableList.of(
            EnderscapeSensors.DRIFTER_TEMPTATIONS,
            EnderscapeSensors.HURT_BY,
            EnderscapeSensors.IS_IN_WATER,
            EnderscapeSensors.NEAREST_ADULT,
            EnderscapeSensors.NEAREST_INTIMIDATOR,
            EnderscapeSensors.NEAREST_LIVING_ENTITIES,
            EnderscapeSensors.NEAREST_PLAYERS
    );

    public static final int HOME_RADIUS = 64;

    private static final Brain.Provider<Drifter> BRAIN_PROVIDER = Brain.provider(MEMORY_TYPES, SENSOR_TYPES, entity -> createActivityData());

    public static Brain<?> makeBrain(Drifter drifter, Brain.Packed packed) {
        var brain = BRAIN_PROVIDER.makeBrain(drifter, packed);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();

        return brain;
    }

    public static ImmutableList<ActivityData<Drifter>> createActivityData() {
        return ImmutableList.of(
                ActivityData.create(Activity.CORE, 0, ImmutableList.of(
                        new CalmDownFromAttacker(24),
                        new CalmDownFromIntimidator(24),

                        new CountDownCooldownTicks(EnderscapeMemory.DRIFTER_FIND_HOME_COOLDOWN),
                        new CountDownCooldownTicks(EnderscapeMemory.DRIFTER_JELLY_CHANGE_COOLDOWN),
                        new CountDownCooldownTicks(EnderscapeMemory.TEMPTATION_COOLDOWN_TICKS),

                        new DrifterStartOrStopLeakingJelly(),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new DrifterRefreshHomePosition(),
                        new Swim<>(0.8f),

                        SetWalkTargetAwayFrom.entity(EnderscapeMemory.HURT_BY_ENTITY, 2, 12, true),
                        SetWalkTargetAwayFrom.entity(EnderscapeMemory.NEAREST_INTIMIDATOR, 2, 8, true)
                )),
                ActivityData.create(Activity.IDLE, ImmutableList.of(
                        Pair.of(0, new AnimalMakeLove(EnderscapeEntities.DRIFTER)),
                        Pair.of(1, new FollowTemptation(mob -> 1.25F)),
                        Pair.of(2, BabyFollowAdult.create(UniformInt.of(4, 16), 2)),
                        Pair.of(3, SetWalkTargetAwayFrom.entity(EnderscapeMemory.NEAREST_INTIMIDATOR, 1, 12, true)),
                        Pair.of(4, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0f, UniformInt.of(30, 60))),
                        Pair.of(8, new RunOne<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableList.of(
                                                Pair.of(RandomStroll.fly(1.0F), 3),
                                                Pair.of(new DoNothing(30, 60), 3)
                                        )
                                )
                        )
                ))
        );
    }

    public static void updateActivity(Drifter entity) {
        entity.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }

    public static Predicate<ItemStack> getTemptations() {
        return stack -> stack.is(EnderscapeItemTags.DRIFTER_FOOD);
    }
}