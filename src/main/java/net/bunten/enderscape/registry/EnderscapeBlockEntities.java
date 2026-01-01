package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.block.MagniaRadioBlockEntity;
import net.bunten.enderscape.block.MagniaSproutBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EnderscapeBlockEntities {
    public static final BlockEntityType<MagniaSproutBlockEntity> MAGNIA_SPROUT = register("magnia_sprout", FabricBlockEntityTypeBuilder.create(MagniaSproutBlockEntity::new, EnderscapeBlocks.ALLURING_MAGNIA_SPROUT, EnderscapeBlocks.REPULSIVE_MAGNIA_SPROUT).build());
    public static final BlockEntityType<MagniaRadioBlockEntity> MAGNIA_RADIO = register("magnia_radio", FabricBlockEntityTypeBuilder.create(MagniaRadioBlockEntity::new, EnderscapeBlocks.MAGNIA_RADIO).build());

    public static <T extends BlockEntity, B extends BlockEntityType<T>> B register(String name, B type) {
        Util.fetchChoiceType(References.BLOCK_ENTITY, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Enderscape.id(name), type);
    }
}