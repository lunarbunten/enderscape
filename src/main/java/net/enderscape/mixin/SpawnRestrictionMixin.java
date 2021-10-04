package net.enderscape.mixin;

import net.enderscape.entity.drifter.DrifterEntity;
import net.enderscape.entity.driftlet.DriftletEntity;
import net.enderscape.registry.EndEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpawnRestriction.class)
public abstract class SpawnRestrictionMixin {

    static {
        register(EndEntities.DRIFTER, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DrifterEntity::canSpawn);
        register(EndEntities.DRIFTLET, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DriftletEntity::canSpawn);
        register(EndEntities.RUBBLEMITE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
    }

    @Shadow
    private static <T extends MobEntity> void register(EntityType<T> a, SpawnRestriction.Location b, Heightmap.Type c, SpawnRestriction.SpawnPredicate<T> d) {
    }
}