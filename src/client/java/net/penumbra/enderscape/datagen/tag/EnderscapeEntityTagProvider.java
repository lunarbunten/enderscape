package net.penumbra.enderscape.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.entity.EntityTypeIds.*;
import static net.penumbra.enderscape.references.EnderscapeEntityIds.*;
import static net.penumbra.enderscape.registry.tag.EnderscapeEntityTags.*;

public class EnderscapeEntityTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {

    public EnderscapeEntityTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(AFFECTED_BY_MAGNIA).add(IRON_GOLEM, MINECART);
        tag(AMBIENT_VOIDING_IMMUNE).add(SHULKER);
        tag(BLACKLISTED_FROM_LODESTONE_TELEPORTATION).add(ELDER_GUARDIAN, ENDER_DRAGON, GHAST, RAVAGER, WARDEN, WITHER);
        tag(CREATES_VOID_PARTICLES_UPON_DEATH).addTag(VOID);
        tag(DRIFTERS_INTIMIDATED_BY).add(SLIME);
        tag(EXEMPT_FROM_MAGNIA_ATTRACTOR_ABUSE_COST).add(EXPERIENCE_ORB);
        tag(HEALED_BY_VOID_CORRUPTION).addTag(VOID);
        tag(HEALED_BY_VOID_PURIFICATION).add(DRIFTER);
        tag(HURT_BY_VOID_PURIFICATION).addTag(VOID);
        tag(IGNORES_BACKSTAB_DAMAGE).add(ARMOR_STAND, SHULKER);
        tag(PULLED_BY_MAGNIA_ATTRACTOR).add(ITEM, EXPERIENCE_ORB);
        tag(RUBBLEMITE_ALWAYS_HOSTILES).add(IRON_GOLEM);
        tag(VOID).add(ENDERMAN, ENDERMITE, ENDER_DRAGON, RUBBLEMITE);
        tag(VOID_IMMUNE).addTag(VOID).add(RUSTLE);
        tag(VOID_LACHRYMA_WALKABLE_MOBS).addTag(VOID);
        tag(VOID_SHALE_WALKABLE_MOBS).forceAddTag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS);

        tag(EntityTypeTags.ARTHROPOD).add(RUBBLEMITE);
        tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(DRIFTER);
        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(RUBBLEMITE, RUSTLE);

        tag(externalKey("supplementaries", "ash_blacklist")).addTag(VOID).add(SHULKER, DRIFTER);
    }

    private TagKey<EntityType<?>> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(namespace, path));
    }
}
