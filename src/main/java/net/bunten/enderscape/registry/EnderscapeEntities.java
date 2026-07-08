package net.bunten.enderscape.registry;

import com.google.common.reflect.Reflection;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.ai.EnderscapeSensors;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.bunten.enderscape.entity.rustle.Rustle;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EnderscapeEntities {

    public static final DeferredHolder<EntityType<?>, EntityType<Drifter>> DRIFTER = register("drifter", () -> EntityType.Builder.of(Drifter::new, MobCategory.CREATURE)
            .sized(1.9F, 2.9F)
            .eyeHeight(1.08F)
            .passengerAttachments(3.0F)
            .ridingOffset(0.1F)
            .clientTrackingRange(8)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Rubblemite>> RUBBLEMITE = register("rubblemite", () -> EntityType.Builder.of(Rubblemite::new, MobCategory.MONSTER)
            .sized(0.55F, 0.4F)
            .eyeHeight(0.13F)
            .passengerAttachments(0.3F)
            .ridingOffset(0.1F)
            .clientTrackingRange(8)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Rustle>> RUSTLE = register("rustle", () -> EntityType.Builder.of(Rustle::new, MobCategory.CREATURE)
            .sized(0.6F, 0.5F)
            .eyeHeight(0.13F)
            .passengerAttachments(0.3F)
            .ridingOffset(0.1F)
            .clientTrackingRange(8)
    );

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
        return register(ResourceKey.create(Registries.ENTITY_TYPE, Enderscape.id(name)), builder);
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(ResourceKey<EntityType<?>> resourceKey, Supplier<EntityType.Builder<T>> builder) {
        return RegistryHelper.registerForHolder(BuiltInRegistries.ENTITY_TYPE, resourceKey, () -> builder.get().build(resourceKey.location().toString()));
    }

    @SubscribeEvent
    public static void entityAttributeCreationListener(EntityAttributeCreationEvent event) {
        event.put(DRIFTER.get(), Drifter.createAttributes().build());
        event.put(RUBBLEMITE.get(), Rubblemite.createAttributes().build());
        event.put(RUSTLE.get(), Rustle.createAttributes().build());
    }

    @SubscribeEvent
    public static void spawnPlacementsListener(RegisterSpawnPlacementsEvent event) {
        event.register(DRIFTER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Drifter::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(RUBBLEMITE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Rubblemite::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(RUSTLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Rustle::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    static {
        Reflection.initialize(
                EnderscapeMemory.class,
                EnderscapeSensors.class
        );
    }
}