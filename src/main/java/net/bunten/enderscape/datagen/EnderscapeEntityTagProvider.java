package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeEntities.*;
import static net.bunten.enderscape.registry.tag.EnderscapeEntityTags.*;
import static net.minecraft.world.entity.EntityType.*;

public class EnderscapeEntityTagProvider extends FabricTagProvider<EntityType<?>> {

    public EnderscapeEntityTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.ENTITY_TYPE, future);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(AFFECTED_BY_MAGNIA).add(IRON_GOLEM, MINECART);
        getOrCreateTagBuilder(BLACKLISTED_FROM_MIRROR_IN_DISPENSER_TELEPORTATION).add(ELDER_GUARDIAN, ENDER_DRAGON, GHAST, RAVAGER, WARDEN, WITHER);
        getOrCreateTagBuilder(CREATES_VOID_PARTICLES_UPON_DEATH).add(ENDERMAN, ENDERMITE, RUBBLEMITE);
        getOrCreateTagBuilder(DRIFTERS).add(DRIFTER, DRIFTLET);
        getOrCreateTagBuilder(DRIFTERS_INTIMIDATED_BY).add(RUBBLEMITE, SLIME);
        getOrCreateTagBuilder(EXEMPT_FROM_MAGNIA_ATTRACTOR_ABUSE_COST).add(EXPERIENCE_ORB);
        getOrCreateTagBuilder(PULLED_BY_MAGNIA_ATTRACTOR).add(ITEM, EXPERIENCE_ORB);
        getOrCreateTagBuilder(RUBBLEMITE_HOSTILE_TOWARDS).add(IRON_GOLEM);

        getOrCreateTagBuilder(EntityTypeTags.ARTHROPOD).add(RUBBLEMITE);
        getOrCreateTagBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE).addTag(DRIFTERS);

        getOrCreateTagBuilder(externalKey("supplementaries", "ash_blacklist")).add(ENDERMAN, ENDERMITE, ENDER_DRAGON, SHULKER, RUBBLEMITE).forceAddTag(DRIFTERS);
    }

    private TagKey<EntityType<?>> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
