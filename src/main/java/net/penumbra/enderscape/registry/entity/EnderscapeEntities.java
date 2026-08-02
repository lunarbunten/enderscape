package net.penumbra.enderscape.registry.entity;

import com.google.common.reflect.Reflection;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.entity.ai.EnderscapeActivity;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.ai.EnderscapeSensors;
import net.penumbra.enderscape.entity.drifter.Drifter;
import net.penumbra.enderscape.entity.rubblemite.Rubblemite;
import net.penumbra.enderscape.entity.rustle.Rustle;

public class EnderscapeEntities {

    public static final EntityType<Drifter> DRIFTER = register("drifter", EntityType.Builder.of(Drifter::new, MobCategory.CREATURE)
            .sized(1.9F, 2.9F)
            .eyeHeight(1.08F)
            .passengerAttachments(3.0F)
            .ridingOffset(0.1F)
            .clientTrackingRange(8)
    );

    public static final EntityType<Rubblemite> RUBBLEMITE = register("rubblemite", EntityType.Builder.of(Rubblemite::new, MobCategory.MONSTER)
            .sized(0.55F, 0.375F)
            .eyeHeight(0.13F)
            .passengerAttachments(0.3F)
            .ridingOffset(0.1F)
            .clientTrackingRange(8)
            .notInPeaceful()
    );

    public static final EntityType<Rustle> RUSTLE = register("rustle", EntityType.Builder.of(Rustle::new, MobCategory.CREATURE)
            .sized(0.6F, 0.3125F)
            .eyeHeight(0.13F)
            .passengerAttachments(0.3F)
            .ridingOffset(0.1F)
            .clientTrackingRange(8)
    );

    static {
        Reflection.initialize(
                EnderscapeActivity.class,
                EnderscapeMemory.class,
                EnderscapeSensors.class
        );

        SpawnPlacements.register(DRIFTER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Drifter::canSpawn);
        SpawnPlacements.register(RUBBLEMITE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Rubblemite::canSpawn);
        SpawnPlacements.register(RUSTLE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Rustle::canSpawn);

        FabricDefaultAttributeRegistry.register(DRIFTER, Drifter.createAttributes());
        FabricDefaultAttributeRegistry.register(RUBBLEMITE, Rubblemite.createAttributes());
        FabricDefaultAttributeRegistry.register(RUSTLE, Rustle.createAttributes());
    }

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        return register(ResourceKey.create(Registries.ENTITY_TYPE, Enderscape.id(name)), builder);
    }

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> resourceKey, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, resourceKey, builder.build(resourceKey));
    }
}