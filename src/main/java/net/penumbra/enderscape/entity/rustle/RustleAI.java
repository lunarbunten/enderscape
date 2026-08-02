package net.penumbra.enderscape.entity.rustle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.entity.ai.EnderscapeActivity;
import net.penumbra.enderscape.entity.ai.EnderscapeSensors;
import net.penumbra.enderscape.entity.ai.behavior.CalmDownFromAttacker;
import net.penumbra.enderscape.entity.ai.behavior.ConditionalLookAtTargetSink;
import net.penumbra.enderscape.entity.ai.behavior.rustle.RustleGoToSleep;
import net.penumbra.enderscape.entity.ai.behavior.rustle.RustleItemConversion;
import net.penumbra.enderscape.entity.ai.behavior.rustle.RustleRegrowHairNaturally;
import net.penumbra.enderscape.manager.RustleItemConversionManager;
import net.penumbra.enderscape.registry.entity.EnderscapeEntities;
import net.penumbra.enderscape.registry.tag.EnderscapeItemTags;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.IS_PANICKING;
import static net.penumbra.enderscape.entity.ai.EnderscapeMemory.*;

@SuppressWarnings("deprecation")
public class RustleAI {

    public static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            AVOID_TARGET,
            BREED_TARGET,
            CANT_REACH_WALK_TARGET_SINCE,
            HURT_BY_ENTITY,
            IS_PANICKING,
            IS_TEMPTED,
            LOOK_TARGET,
            NEAREST_ATTACKABLE,
            NEAREST_LIVING_ENTITIES,
            NEAREST_VISIBLE_ADULT,
            NEAREST_VISIBLE_LIVING_ENTITIES,
            NEAREST_VISIBLE_PLAYER,
            PATH,
            RUSTLE_SLEEPING_ON_COOLDOWN,
            RUSTLE_HAIR_REGROWTH_COOLDOWN,
            RUSTLE_SLEEPING_SPOT,
            RUSTLE_SLEEP_TICKS,
            RUSTLE_IS_CONVERTING,
            TEMPTATION_COOLDOWN_TICKS,
            TEMPTING_PLAYER,
            WALK_TARGET
    );

    public static final ImmutableList<SensorType<? extends Sensor<? super Rustle>>> SENSOR_TYPES = ImmutableList.of(
            EnderscapeSensors.HURT_BY,
            EnderscapeSensors.IS_IN_WATER,
            EnderscapeSensors.NEAREST_ADULT,
            EnderscapeSensors.NEAREST_LIVING_ENTITIES,
            EnderscapeSensors.NEAREST_PLAYERS,
            EnderscapeSensors.NEAREST_ITEMS,
            EnderscapeSensors.RUSTLE_NEAREST_SLEEPING_SPOT,
            EnderscapeSensors.RUSTLE_TEMPTATIONS
    );

    public static final IntProvider HAIR_REGROWTH_COOLDOWN_RANGE_IN_MINUTES = UniformInt.of(4, 8);
    public static final IntProvider SLEEPING_TIME_RANGE_IN_MINUTES = ConstantInt.of(1);

    public static final BiPredicate<ServerLevel, BlockPos> HAS_STURDY_SURFACE = (level, pos) -> level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);

    public static List<ActivityData<Rustle>> getActivities(final Rustle body) {
        return List.of(
                initCoreActivity(),
                initItemConversionActivity(),
                initIdleActivity(),
                initRestActivity()
        );
    }

    public static void updateActivity(Rustle mob) {
        mob.getBrain().setActiveActivityToFirstValid(ImmutableList.of(EnderscapeActivity.REST, EnderscapeActivity.ITEM_CONVERSION, EnderscapeActivity.IDLE));
    }

    private static ActivityData<Rustle> initCoreActivity() {
        return ActivityData.create(EnderscapeActivity.CORE, 0, ImmutableList.of(
                new CalmDownFromAttacker(16),

                new CountDownCooldownTicks(RUSTLE_HAIR_REGROWTH_COOLDOWN),
                new CountDownCooldownTicks(RUSTLE_SLEEP_TICKS),
                new CountDownCooldownTicks(TEMPTATION_COOLDOWN_TICKS),

                new RustleRegrowHairNaturally(),

                new ConditionalLookAtTargetSink<>(Rustle::isSleeping, 45, 90),
                new MoveToTargetSink(),
                new Swim<>(0.8f),

                SetWalkTargetAwayFrom.entity(HURT_BY_ENTITY, 1.25F, 12, true)
            )
        );
    }

    private static ActivityData<Rustle> initItemConversionActivity() {
        return ActivityData.create(
                EnderscapeActivity.ITEM_CONVERSION,
                0,
                ImmutableList.of(new RustleItemConversion()),
                ImmutableSet.of(
                        Pair.of(RUSTLE_IS_CONVERTING, MemoryStatus.VALUE_PRESENT),
                        Pair.of(RUSTLE_SLEEP_TICKS, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    private static ActivityData<Rustle> initIdleActivity() {
        return ActivityData.create(
                EnderscapeActivity.IDLE,
                ImmutableList.of(
                        Pair.of(1, new AnimalMakeLove(EnderscapeEntities.RUSTLE, 1.25F, 1)),
                        Pair.of(2, new FollowTemptation(mob -> 1.25F)),
                        Pair.of(3, BabyFollowAdult.create(UniformInt.of(1, 4), 1.25F)),
                        Pair.of(4, SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 1.5F, UniformInt.of(30, 60))),
                        Pair.of(8, new RunOne<>(
                                ImmutableMap.of(WALK_TARGET, MemoryStatus.VALUE_ABSENT, HURT_BY_ENTITY, MemoryStatus.VALUE_ABSENT),
                                ImmutableList.of(
                                        Pair.of(RandomStroll.stroll(1), 5),
                                        Pair.of(new DoNothing(30, 60), 5),
                                        Pair.of(new RustleGoToSleep(1.0F), 1)
                                ))
                        )
                ),
                ImmutableSet.of(
                        Pair.of(RUSTLE_IS_CONVERTING, MemoryStatus.VALUE_ABSENT),
                        Pair.of(RUSTLE_SLEEP_TICKS, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    private static ActivityData<Rustle> initRestActivity() {
        return ActivityData.create(
                EnderscapeActivity.REST,
                ImmutableList.of(),
                Set.of(
                        Pair.of(RUSTLE_SLEEPING_ON_COOLDOWN, MemoryStatus.VALUE_ABSENT),
                        Pair.of(IS_PANICKING, MemoryStatus.VALUE_ABSENT),
                        Pair.of(RUSTLE_IS_CONVERTING, MemoryStatus.VALUE_ABSENT),
                        Pair.of(RUSTLE_SLEEP_TICKS, MemoryStatus.VALUE_PRESENT),
                        Pair.of(WALK_TARGET, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    public static Predicate<ItemStack> getTemptations() {
        return stack -> stack.is(EnderscapeItemTags.RUSTLE_FOOD) || RustleItemConversionManager.getRecipeFor(stack).isPresent();
    }

    public static void refreshNaturalHairGrowthCooldown(Rustle mob) {
        mob.getBrain().setMemory(RUSTLE_HAIR_REGROWTH_COOLDOWN, HAIR_REGROWTH_COOLDOWN_RANGE_IN_MINUTES.sample(mob.getRandom()) * 20 * 60);
    }
}