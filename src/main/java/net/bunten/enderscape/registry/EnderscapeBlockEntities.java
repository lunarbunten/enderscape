package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.block.MagniaSproutBlockEntity;
import net.bunten.enderscape.block.MagniaRadioBlockEntity;
import net.bunten.enderscape.block.VoidCampfireBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class EnderscapeBlockEntities {
    public static final Supplier<BlockEntityType<MagniaSproutBlockEntity>> MAGNIA_SPROUT = register("magnia_sprout", () -> BlockEntityType.Builder.of(MagniaSproutBlockEntity::new, EnderscapeBlocks.ALLURING_MAGNIA_SPROUT.get(), EnderscapeBlocks.REPULSIVE_MAGNIA_SPROUT.get()).build(null));
    public static final Supplier<BlockEntityType<MagniaRadioBlockEntity>> MAGNIA_RADIO = register("magnia_radio", () -> BlockEntityType.Builder.of(MagniaRadioBlockEntity::new,  EnderscapeBlocks.MAGNIA_RADIO.get()).build(null));
    public static final Supplier<BlockEntityType<VoidCampfireBlockEntity>> VOID_CAMPFIRE = register("void_campfire", () -> BlockEntityType.Builder.of(VoidCampfireBlockEntity::new,  EnderscapeBlocks.VOID_CAMPFIRE.get()).build(null));

    public static <T extends BlockEntity, B extends BlockEntityType<T>> Supplier<B> register(String name, Supplier<B> type) {
        Util.fetchChoiceType(References.BLOCK_ENTITY, name);
        return RegistryHelper.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Enderscape.id(name), type);
    }
}