package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.EnderscapeEntities.DRIFTER;
import static net.bunten.enderscape.registry.EnderscapeEntities.RUBBLEMITE;
import static net.bunten.enderscape.registry.tag.EnderscapeEntityTags.*;
import static net.minecraft.world.entity.EntityType.*;

public class EnderscapeEntityTagProvider extends EntityTypeTagsProvider {

    public EnderscapeEntityTagProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), Enderscape.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(AFFECTED_BY_MAGNIA).add(IRON_GOLEM, MINECART);
        tag(BLACKLISTED_FROM_MIRROR_IN_DISPENSER_TELEPORTATION).add(ELDER_GUARDIAN, ENDER_DRAGON, GHAST, RAVAGER, WARDEN, WITHER);
        tag(CREATES_VOID_PARTICLES_UPON_DEATH).add(ENDERMAN, ENDERMITE, RUBBLEMITE.get());
        tag(DRIFTERS_INTIMIDATED_BY).add(RUBBLEMITE.get(), SLIME);
        tag(EXEMPT_FROM_MAGNIA_ATTRACTOR_ABUSE_COST).add(EXPERIENCE_ORB);
        tag(PULLED_BY_MAGNIA_ATTRACTOR).add(ITEM, EXPERIENCE_ORB);
        tag(RUBBLEMITE_HOSTILE_TOWARDS).add(IRON_GOLEM);

        tag(EntityTypeTags.ARTHROPOD).add(RUBBLEMITE.get());
        tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(DRIFTER.get());

        tag(externalKey("supplementaries", "ash_blacklist")).add(ENDERMAN, ENDERMITE, ENDER_DRAGON, SHULKER, RUBBLEMITE.get(), DRIFTER.get());
    }

    private TagKey<EntityType<?>> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
