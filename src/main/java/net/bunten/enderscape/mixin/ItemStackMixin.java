package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract int getDamageValue();

    @Shadow public abstract int getMaxDamage();

    @Shadow public abstract Item getItem();

    @Inject(method = "applyDamage", at = @At("TAIL"))
    public void Enderscape$animateTick(int i, @Nullable ServerPlayer player, Consumer<Item> consumer, CallbackInfo info) {
        if (getDamageValue() == getMaxDamage() - 1 && getItem() == Items.ELYTRA) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), EnderscapeItemSounds.ELYTRA_BREAK, player.getSoundSource(), 1.0F, 1.0F);
        }
    }
}