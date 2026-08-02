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

import static net.minecraft.world.entity.EntityType.*;
import static net.penumbra.enderscape.registry.entity.EnderscapeEntities.*;
import static net.penumbra.enderscape.registry.tag.EnderscapeEntityTags.*;

public class EnderscapeEntityTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {

    public EnderscapeEntityTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        valueLookupBuilder(AFFECTED_BY_MAGNIA).add(IRON_GOLEM, MINECART);
        valueLookupBuilder(AMBIENT_VOIDING_IMMUNE).add(SHULKER);
        valueLookupBuilder(BLACKLISTED_FROM_LODESTONE_TELEPORTATION).add(ELDER_GUARDIAN, ENDER_DRAGON, GHAST, RAVAGER, WARDEN, WITHER);
        valueLookupBuilder(CREATES_VOID_PARTICLES_UPON_DEATH).addTag(VOID);
        valueLookupBuilder(DRIFTERS_INTIMIDATED_BY).add(SLIME);
        valueLookupBuilder(EXEMPT_FROM_MAGNIA_ATTRACTOR_ABUSE_COST).add(EXPERIENCE_ORB);
        valueLookupBuilder(HEALED_BY_VOID_CORRUPTION).addTag(VOID);
        valueLookupBuilder(HEALED_BY_VOID_PURIFICATION).add(DRIFTER);
        valueLookupBuilder(HURT_BY_VOID_PURIFICATION).addTag(VOID);
        valueLookupBuilder(IGNORES_BACKSTAB_DAMAGE).add(ARMOR_STAND, SHULKER);
        valueLookupBuilder(PULLED_BY_MAGNIA_ATTRACTOR).add(ITEM, EXPERIENCE_ORB);
        valueLookupBuilder(RUBBLEMITE_ALWAYS_HOSTILES).add(IRON_GOLEM);
        valueLookupBuilder(VOID).add(ENDERMAN, ENDERMITE, ENDER_DRAGON, RUBBLEMITE);
        valueLookupBuilder(VOID_IMMUNE).addTag(VOID).add(RUSTLE);
        valueLookupBuilder(VOID_LACHRYMA_WALKABLE_MOBS).addTag(VOID);
        valueLookupBuilder(VOID_SHALE_WALKABLE_MOBS).forceAddTag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS);

        valueLookupBuilder(EntityTypeTags.ARTHROPOD).add(RUBBLEMITE);
        valueLookupBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(DRIFTER);
        valueLookupBuilder(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(RUBBLEMITE, RUSTLE);

        valueLookupBuilder(externalKey("supplementaries", "ash_blacklist")).addTag(VOID).add(SHULKER, DRIFTER);
    }

    private TagKey<EntityType<?>> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(namespace, path));
    }
}
