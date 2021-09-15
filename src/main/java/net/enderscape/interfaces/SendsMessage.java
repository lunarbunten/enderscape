package net.enderscape.interfaces;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

public interface SendsMessage {

    /**
     * Checked to see when an item should display it's message.
     *
     * @param stack  The item stack itself.
     * @param player The player holding the item.
     */
    boolean canDisplay(ItemStack stack, PlayerEntity player);

    /**
     * The message the item should display above the crosshair.
     *
     * @param stack  The item stack itself.
     * @param player The player holding the item.
     */
    @Nullable TranslatableText getMessage(ItemStack stack, PlayerEntity player);

    /**
     * The color of the text being displayed.
     *
     * @param stack  The item stack itself.
     * @param player The player holding the item.
     */
    int getColor(ItemStack stack, PlayerEntity player);
}