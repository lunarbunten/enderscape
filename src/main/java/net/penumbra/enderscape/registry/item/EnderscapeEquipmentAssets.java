package net.penumbra.enderscape.registry.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.penumbra.enderscape.Enderscape;

import static net.minecraft.world.item.equipment.EquipmentAssets.ROOT_ID;

public class EnderscapeEquipmentAssets {
    public static final ResourceKey<EquipmentAsset> SHADOLINE = ResourceKey.create(ROOT_ID, Enderscape.id("shadoline"));
    public static final ResourceKey<EquipmentAsset> DRIFT_LEGGINGS = ResourceKey.create(ROOT_ID, Enderscape.id("drift_leggings"));
    public static final ResourceKey<EquipmentAsset> SHULKER_SHELL = ResourceKey.create(ROOT_ID, Enderscape.id("shulker_shell"));
}
