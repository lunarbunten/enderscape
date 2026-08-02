package net.penumbra.enderscape.entity.ai.sensing;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;

public class NearestAttackableSensor extends NearestVisibleLivingEntitySensor {

    private final TagKey<EntityType<?>> alwaysHostiles;

    public NearestAttackableSensor(TagKey<EntityType<?>> alwaysHostiles) {
        this.alwaysHostiles = alwaysHostiles;
    }

    @Override
    protected boolean isMatchingEntity(final ServerLevel level, final LivingEntity body, final LivingEntity mob) {
        return isClose(body, mob) && isHostileTarget(mob) && Sensor.isEntityAttackable(level, body, mob);
    }
    private boolean isHostileTarget(final LivingEntity mob) {
        return mob.is(alwaysHostiles);
    }

    private boolean isClose(final LivingEntity body, final LivingEntity mob) {
        return mob.distanceToSqr(body) <= 64.0;
    }

    @Override
    protected MemoryModuleType<LivingEntity> getMemoryToSet() {
        return MemoryModuleType.NEAREST_ATTACKABLE;
    }
}