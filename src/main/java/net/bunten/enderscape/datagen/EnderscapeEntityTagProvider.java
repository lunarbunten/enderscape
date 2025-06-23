package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;

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
        valueLookupBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE).forceAddTag(DRIFTERS);
        valueLookupBuilder(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(RUBBLEMITE);
    }
}
