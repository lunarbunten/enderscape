package net.penumbra.enderscape.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;


/**
 * Backport from 26.2 as a way to simplify
 */
public record BlockItemId(ResourceKey<Block> block, ResourceKey<Item> item) {
   public static BlockItemId create(final Identifier blockId, final Identifier itemId) {
      return new BlockItemId(ResourceKey.create(Registries.BLOCK, blockId), ResourceKey.create(Registries.ITEM, itemId));
   }
}