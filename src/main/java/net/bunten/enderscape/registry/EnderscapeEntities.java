package net.bunten.enderscape.registry;

import org.betterx.bclib.api.v2.spawning.SpawnRuleBuilder;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.bunten.enderscape.entity.driftlet.Driftlet;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.Heightmap.Types;

public class EnderscapeEntities {

    public static final TagKey<EntityType<?>> DRIFTERS = register("drifters");

    public static final EntityType<Drifter> DRIFTER = register("drifter", Drifter::new, MobCategory.CREATURE, 1.9F, 2.9F);
    public static final EntityType<Driftlet> DRIFTLET = register("driftlet", Driftlet::new, MobCategory.CREATURE, 1, 1.2F);
    public static final EntityType<Rubblemite> RUBBLEMITE = register("rubblemite", Rubblemite::new, MobCategory.MONSTER, 0.6F, 0.4F);

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> entity) {
        return Registry.register(Registry.ENTITY_TYPE, Enderscape.id(name), entity);
    }

    private static <T extends Mob> EntityType<T> register(String name, EntityFactory<T> entity, MobCategory group, float width, float height) {
        return register(name, begin(name, entity, group, width, height).build());
    }

    private static <T extends Entity> FabricEntityTypeBuilder<T> begin(String name, EntityFactory<T> entity, MobCategory group, float width, float height) {
        return FabricEntityTypeBuilder.<T>create(group, entity).dimensions(EntityDimensions.fixed(width, height));
    }

    private static TagKey<EntityType<?>> register(String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, Enderscape.id(name));
    }

    static {
        SpawnRuleBuilder.start(DRIFTER).maxNearby(6, 32).buildOnGround(Types.MOTION_BLOCKING_NO_LEAVES);
        SpawnRuleBuilder.start(DRIFTLET).maxNearby(6, 32).buildOnGround(Types.MOTION_BLOCKING_NO_LEAVES);
        SpawnRuleBuilder.start(RUBBLEMITE).notPeaceful().hostile(1).maxNearby(6, 32).onlyOnValidBlocks().buildOnGround(Types.MOTION_BLOCKING_NO_LEAVES);

        FabricDefaultAttributeRegistry.register(DRIFTER, Drifter.createAttributes());
        FabricDefaultAttributeRegistry.register(DRIFTLET, Driftlet.createAttributes());
        FabricDefaultAttributeRegistry.register(RUBBLEMITE, Rubblemite.createAttributes());
    }
}