package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.ids.EnderscapeEntityIds.*;
import static net.bunten.enderscape.registry.tag.EnderscapeEntityTags.*;
import static net.minecraft.world.entity.EntityTypeIds.*;

public class EnderscapeEntityTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {

    public EnderscapeEntityTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(AFFECTED_BY_MAGNIA).add(IRON_GOLEM, MINECART);
        tag(BLACKLISTED_FROM_MIRROR_IN_DISPENSER_TELEPORTATION).add(ELDER_GUARDIAN, ENDER_DRAGON, GHAST, RAVAGER, WARDEN, WITHER);
        tag(CREATES_VOID_PARTICLES_UPON_DEATH).add(ENDERMAN, ENDERMITE, RUBBLEMITE);
        tag(DRIFTERS_INTIMIDATED_BY).add(RUBBLEMITE, SLIME);
        tag(EXEMPT_FROM_MAGNIA_ATTRACTOR_ABUSE_COST).add(EXPERIENCE_ORB);
        tag(PULLED_BY_MAGNIA_ATTRACTOR).add(ITEM, EXPERIENCE_ORB);
        tag(RUBBLEMITE_HOSTILE_TOWARDS).add(IRON_GOLEM);

        tag(EntityTypeTags.ARTHROPOD).add(RUBBLEMITE);
        tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(DRIFTER);
        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(RUBBLEMITE);

        tag(externalKey("supplementaries", "ash_blacklist")).add(ENDERMAN, ENDERMITE, ENDER_DRAGON, SHULKER, RUBBLEMITE, DRIFTER);
    }

    private TagKey<EntityType<?>> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(namespace, path));
    }
}
