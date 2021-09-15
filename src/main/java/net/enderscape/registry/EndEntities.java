package net.enderscape.registry;

import net.enderscape.Enderscape;
import net.enderscape.entity.drifter.DrifterEntity;
import net.enderscape.entity.driftlet.DriftletEntity;
import net.enderscape.entity.motu.MotuEntity;
import net.enderscape.entity.rubblemite.RubblemiteEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.registry.Registry;

public class EndEntities {

        public static final EntityType<DrifterEntity> DRIFTER = register("drifter",
        FabricEntityTypeBuilder.Mob.create(SpawnGroup.CREATURE, DrifterEntity::new).dimensions(EntityDimensions.fixed(1.9F, 2.9F)).build());
    
        public static final EntityType<DriftletEntity> DRIFTLET = register("driftlet",
        FabricEntityTypeBuilder.Mob.create(SpawnGroup.CREATURE, DriftletEntity::new).dimensions(EntityDimensions.fixed(1, 1.2F)).build());
    
        public static final EntityType<MotuEntity> MOTU = register("motu",
        FabricEntityTypeBuilder.Mob.create(SpawnGroup.MONSTER, MotuEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.4F)).build());
    
        public static final EntityType<RubblemiteEntity> RUBBLEMITE = register("rubblemite",
        FabricEntityTypeBuilder.Mob.create(SpawnGroup.MONSTER, RubblemiteEntity::new)
        .dimensions(EntityDimensions.fixed(0.6F, 0.4F)).build());

    private static <A extends Entity, T extends EntityType<A>> EntityType<A> register(String name, T entity) {
        return Registry.register(Registry.ENTITY_TYPE, Enderscape.id(name), entity);
    }

    public static void init() {
        FabricDefaultAttributeRegistry.register(DRIFTER,
        DrifterEntity.createMobAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 16)
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10));

        FabricDefaultAttributeRegistry.register(DRIFTLET,
        DrifterEntity.createMobAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 8)
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2));

        FabricDefaultAttributeRegistry.register(MOTU,
        MotuEntity.createMobAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.16)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20)
        .add(EntityAttributes.GENERIC_ARMOR, 40));

        FabricDefaultAttributeRegistry.register(RUBBLEMITE,
        RubblemiteEntity.createMobAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 6)
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
        .add(EntityAttributes.GENERIC_ARMOR, 20));
    }
}