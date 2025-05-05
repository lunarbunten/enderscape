package net.bunten.enderscape.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;

public interface DashJumpUser {

    EntityDataAccessor<Boolean> Enderscape$dashed();
    EntityDataAccessor<Integer> Enderscape$dashTicks();

    static boolean is(Entity entity) {
        return entity instanceof DashJumpUser;
    }

    default void defineDashJumpData(SynchedEntityData.Builder builder) {
        builder.define(Enderscape$dashed(), false);
        builder.define(Enderscape$dashTicks(), 0);
    }

    static boolean dashed(Entity entity) {
        if (entity instanceof DashJumpUser user) {
            return entity.getEntityData().get(user.Enderscape$dashed());
        }
        return false;
    }

    static void setDashed(Entity entity, boolean value) {
        if (is(entity)) {
            DashJumpUser user = (DashJumpUser) entity;
            entity.getEntityData().set(user.Enderscape$dashed(), value);
            if (!value) setDashTicks(entity, 0);
        }
    }

    static int dashTicks(Entity entity) {
        if (entity instanceof DashJumpUser user) {
            return entity.getEntityData().get(user.Enderscape$dashTicks());
        }
        return 0;
    }

    static void setDashTicks(Entity entity, int value) {
        if (is(entity)) {
            DashJumpUser user = (DashJumpUser) entity;
            entity.getEntityData().set(user.Enderscape$dashTicks(), value);
        }
    }
}