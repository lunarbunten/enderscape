package net.bunten.enderscape.entity.rustle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.ai.EnderscapeSensors;
import net.bunten.enderscape.entity.ai.behavior.*;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class RustleAI {

    public static final ImmutableList<MemoryModuleType<? extends Object>> MEMORY_TYPES = ImmutableList.of(
            EnderscapeMemory.AVOID_TARGET,
            EnderscapeMemory.BREED_TARGET,
            EnderscapeMemory.CANT_REACH_WALK_TARGET_SINCE,
            EnderscapeMemory.HURT_BY_ENTITY,
            EnderscapeMemory.IS_PANICKING,
            EnderscapeMemory.IS_TEMPTED,
            EnderscapeMemory.LOOK_TARGET,
            EnderscapeMemory.NEAREST_ATTACKABLE,
            EnderscapeMemory.NEAREST_LIVING_ENTITIES,
            EnderscapeMemory.NEAREST_VISIBLE_ADULT,
            EnderscapeMemory.NEAREST_VISIBLE_LIVING_ENTITIES,
            EnderscapeMemory.NEAREST_VISIBLE_PLAYER,
            EnderscapeMemory.PATH,
            EnderscapeMemory.RUSTLE_SLEEPING_ON_COOLDOWN,
            EnderscapeMemory.RUSTLE_FOOD,
            EnderscapeMemory.RUSTLE_HAIR_REGROWTH_COOLDOWN,
            EnderscapeMemory.RUSTLE_SLEEPING_SPOT,
            EnderscapeMemory.RUSTLE_SLEEP_TICKS,
            EnderscapeMemory.TEMPTATION_COOLDOWN_TICKS,
            EnderscapeMemory.TEMPTING_PLAYER,
            EnderscapeMemory.WALK_TARGET
    );

    public static final ImmutableList<ActivityData<Rustle>> ACTIVITY_DATA = ImmutableList.of(
            ActivityData.create(Activity.CORE, 0, ImmutableList.of(
                    new CalmDownFromAttacker(16),

                    new CountDownCooldownTicks(EnderscapeMemory.RUSTLE_HAIR_REGROWTH_COOLDOWN),
                    new CountDownCooldownTicks(EnderscapeMemory.RUSTLE_SLEEP_TICKS),
                    new CountDownCooldownTicks(EnderscapeMemory.TEMPTATION_COOLDOWN_TICKS),

                    new RustleRegrowHairNaturally(),

                    new ConditionalLookAtTargetSink<>(Rustle::isSleeping, 45, 90),
                    new MoveToTargetSink(),
                    new Swim<>(0.8f),

                    SetWalkTargetAwayFrom.entity(EnderscapeMemory.HURT_BY_ENTITY, 1.25F, 12, true)
            )),
            ActivityData.create(Activity.IDLE,
                    ImmutableList.of(
                            Pair.of(0, new AnimalMakeLove(EnderscapeEntities.RUSTLE, 1.25F, 1)),
                            Pair.of(1, new FollowTemptation(mob -> 1.25F)),
                            Pair.of(2, new RustleEatWhenSheared(1.25F)),
                            Pair.of(3, BabyFollowAdult.create(UniformInt.of(1, 4), 1.25F)),
                            Pair.of(4, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 1.5F, UniformInt.of(30, 60))),
                            Pair.of(8, new RunOne<>(
                                    ImmutableMap.of(EnderscapeMemory.WALK_TARGET, MemoryStatus.VALUE_ABSENT, EnderscapeMemory.HURT_BY_ENTITY, MemoryStatus.VALUE_ABSENT),
                                    ImmutableList.of(
                                            Pair.of(RandomStroll.stroll(1), 5),
                                            Pair.of(new DoNothing(30, 60), 5),
                                            Pair.of(new RustleGoToSleep(1.0F), 1)
                                    ))
                            )),
                    ImmutableSet.of(Pair.of(EnderscapeMemory.RUSTLE_SLEEP_TICKS, MemoryStatus.VALUE_ABSENT))
            ),
            ActivityData.create(Activity.REST, ImmutableList.of(
                            Pair.of(8, new DoNothing(30, 60))),
                    Set.of(
                            Pair.of(EnderscapeMemory.RUSTLE_SLEEPING_ON_COOLDOWN, MemoryStatus.VALUE_ABSENT),
                            Pair.of(EnderscapeMemory.IS_PANICKING, MemoryStatus.VALUE_ABSENT),
                            Pair.of(EnderscapeMemory.RUSTLE_SLEEP_TICKS, MemoryStatus.VALUE_PRESENT),
                            Pair.of(EnderscapeMemory.WALK_TARGET, MemoryStatus.VALUE_ABSENT)
                    )
            )
    );

    public static final ImmutableList<SensorType<? extends Sensor<? super Rustle>>> SENSOR_TYPES = ImmutableList.of(
            EnderscapeSensors.HURT_BY,
            EnderscapeSensors.IS_IN_WATER,
            EnderscapeSensors.NEAREST_ADULT,
            EnderscapeSensors.NEAREST_LIVING_ENTITIES,
            EnderscapeSensors.NEAREST_PLAYERS,
            EnderscapeSensors.RUSTLE_NEAREST_FOOD,
            EnderscapeSensors.RUSTLE_NEAREST_SLEEPING_SPOT,
            EnderscapeSensors.RUSTLE_TEMPTATIONS
    );


    private static final Brain.Provider<Rustle> BRAIN_PROVIDER = Brain.provider(MEMORY_TYPES, RustleAI.SENSOR_TYPES, entity -> RustleAI.ACTIVITY_DATA);

    public static final IntProvider HAIR_REGROWTH_COOLDOWN_RANGE_IN_MINUTES = UniformInt.of(4, 8);
    public static final IntProvider SLEEPING_TIME_RANGE_IN_MINUTES = ConstantInt.of(1);

    public static final BiPredicate<ServerLevel, BlockPos> HAS_STURDY_SURFACE = (level, pos) -> level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);

    public static Brain<?> makeBrain(Rustle rustle, Brain.Packed packed) {
        var brain = BRAIN_PROVIDER.makeBrain(rustle, packed);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();

        return brain;
    }

    public static void updateActivity(Rustle mob) {
        mob.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.REST, Activity.IDLE));
    }

    public static Predicate<ItemStack> getTemptations() {
        return stack -> stack.is(EnderscapeItemTags.RUSTLE_FOOD);
    }

    public static void refreshNaturalHairGrowthCooldown(Rustle mob) {
        mob.getBrain().setMemory(EnderscapeMemory.RUSTLE_HAIR_REGROWTH_COOLDOWN, HAIR_REGROWTH_COOLDOWN_RANGE_IN_MINUTES.sample(mob.getRandom()) * 20 * 60);
    }
}