package net.bunten.enderscape.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;

public interface EndTrialSpawnable {

    EntityDataAccessor<Boolean> Enderscape$spawnedFromEndTrialSpawner();

    static boolean is(Entity entity) {
        return entity instanceof EndTrialSpawnable;
    }

    default void defineEndTrialSpawnableData(SynchedEntityData.Builder builder) {
        builder.define(Enderscape$spawnedFromEndTrialSpawner(), false);
    }

    static boolean spawnedFromEndTrialSpawner(Entity entity) {
        if (entity instanceof EndTrialSpawnable user) {
            return entity.getEntityData().get(user.Enderscape$spawnedFromEndTrialSpawner());
        }
        return false;
    }

    static void setSpawnedFromEndTrialSpawner(Entity entity, boolean value) {
        if (is(entity)) {
            EndTrialSpawnable user = (EndTrialSpawnable) entity;
            entity.getEntityData().set(user.Enderscape$spawnedFromEndTrialSpawner(), value);
        }
    }
}