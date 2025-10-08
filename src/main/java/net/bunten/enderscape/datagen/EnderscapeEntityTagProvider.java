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

public class EnderscapeEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {

    public EnderscapeEntityTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        valueLookupBuilder(AFFECTED_BY_MAGNIA).add(IRON_GOLEM, MINECART);
        valueLookupBuilder(BLACKLISTED_FROM_MIRROR_IN_DISPENSER_TELEPORTATION).add(ELDER_GUARDIAN, ENDER_DRAGON, GHAST, RAVAGER, WARDEN, WITHER);
        valueLookupBuilder(CREATES_VOID_PARTICLES_UPON_DEATH).add(ENDERMAN, ENDERMITE, RUBBLEMITE);
        valueLookupBuilder(DRIFTERS).add(DRIFTER, DRIFTLET);
        valueLookupBuilder(DRIFTERS_INTIMIDATED_BY).add(RUBBLEMITE, SLIME);
        valueLookupBuilder(RUBBLEMITE_HOSTILE_TOWARDS).add(IRON_GOLEM);

        valueLookupBuilder(EntityTypeTags.ARTHROPOD).add(RUBBLEMITE);
        valueLookupBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE).addTag(DRIFTERS);
        valueLookupBuilder(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(RUBBLEMITE);

        valueLookupBuilder(externalKey("supplementaries", "ash_blacklist")).add(ENDERMAN, ENDERMITE, ENDER_DRAGON, SHULKER, RUBBLEMITE).addTag(DRIFTERS);
    }

    private TagKey<EntityType<?>> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
