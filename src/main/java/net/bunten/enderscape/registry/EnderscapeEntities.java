package net.bunten.enderscape.registry;

import org.betterx.bclib.api.v2.spawning.SpawnRuleBuilder;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.drifter.DrifterEntity;
import net.bunten.enderscape.entity.driftlet.DriftletEntity;
import net.bunten.enderscape.entity.rubblemite.RubblemiteEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.EntityFactory;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap.Type;

public class EnderscapeEntities {

    public static final EntityType<DrifterEntity> DRIFTER = register("drifter", DrifterEntity::new, SpawnGroup.CREATURE, 1.9F, 2.9F);
    public static final EntityType<DriftletEntity> DRIFTLET = register("driftlet", DriftletEntity::new, SpawnGroup.CREATURE, 1, 1.2F);
    public static final EntityType<RubblemiteEntity> RUBBLEMITE = register("rubblemite", RubblemiteEntity::new, SpawnGroup.MONSTER, 0.6F, 0.4F);

    public static final TagKey<EntityType<?>> DRIFTERS = register("drifters");

    private static <T extends MobEntity> EntityType<T> register(String name, EntityFactory<T> entity, SpawnGroup group, float width, float height) {
        EntityType<T> type = FabricEntityTypeBuilder.<T>create(group, entity).dimensions(EntityDimensions.fixed(width, height)).build();
        return Registry.register(Registry.ENTITY_TYPE, Enderscape.id(name), type);
    }

    private static TagKey<EntityType<?>> register(String name) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, Enderscape.id(name));
    }

    public static void init() {
        SpawnRuleBuilder.start(DRIFTER).maxNearby(6, 32).buildOnGround(Type.MOTION_BLOCKING_NO_LEAVES);
        SpawnRuleBuilder.start(DRIFTLET).maxNearby(6, 32).buildOnGround(Type.MOTION_BLOCKING_NO_LEAVES);
        SpawnRuleBuilder.start(RUBBLEMITE).notPeaceful().onlyOnValidBlocks().maxNearby(6, 32).buildOnGround(Type.MOTION_BLOCKING_NO_LEAVES);
        
        FabricDefaultAttributeRegistry.register(DRIFTER, DrifterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(DRIFTLET, DriftletEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(RUBBLEMITE, RubblemiteEntity.createAttributes());
    }
}