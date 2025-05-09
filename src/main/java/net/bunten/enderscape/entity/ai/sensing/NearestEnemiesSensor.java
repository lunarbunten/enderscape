package net.bunten.enderscape.entity.ai.sensing;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import org.spongepowered.include.com.google.common.collect.Lists;

import com.google.common.collect.ImmutableSet;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;

public class NearestEnemiesSensor extends Sensor<LivingEntity> {

    private final TagKey<EntityType<?>> enemies;

    public NearestEnemiesSensor(TagKey<EntityType<?>> enemies) {
        this.enemies = enemies;
    }

    protected boolean isEnemy(LivingEntity mob, LivingEntity next) {
        return next.getType().is(enemies) && EntitySelector.NO_SPECTATORS.test(next) && mob.closerThan(next, 16);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(EnderscapeMemory.NEAREST_VISIBLE_LIVING_ENTITIES, EnderscapeMemory.NEAREST_ENEMIES, EnderscapeMemory.NEAREST_VISIBLE_ENEMY);
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity mob) {
        Brain<?> brain = mob.getBrain();
        
        Optional<LivingEntity> nearest = Optional.empty();
        Optional<LivingEntity> nearestAttackable = Optional.empty();
        ArrayList<LivingEntity> enemies = Lists.newArrayList();

        NearestVisibleLivingEntities visible = brain.getMemory(EnderscapeMemory.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        
        for (LivingEntity next : visible.findAll((next) -> isEnemy(mob, next))) {
            if (nearest.isEmpty()) nearest = Optional.of(next);
            if (isEntityAttackable(mob, next) && nearestAttackable.isEmpty()) nearestAttackable = Optional.of(next);
            enemies.add(next);
        }

        brain.setMemory(EnderscapeMemory.NEAREST_VISIBLE_ENEMY, nearest);
        brain.setMemory(EnderscapeMemory.NEAREST_VISIBLE_ATTACKABLE_ENEMY, nearestAttackable);
        brain.setMemory(EnderscapeMemory.NEAREST_ENEMIES, enemies);
    }
}