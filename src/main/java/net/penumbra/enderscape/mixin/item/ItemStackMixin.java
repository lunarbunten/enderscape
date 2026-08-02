package net.penumbra.enderscape.mixin.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Unique
    private final ItemStack stack = (ItemStack) (Object) this;

    @Inject(method = "applyDamage", at = @At("TAIL"))
    public void Enderscape$animateTick(int i, @Nullable ServerPlayer player, Consumer<Item> consumer, CallbackInfo info) {
        if (stack.getDamageValue() == stack.getMaxDamage() - 1 && stack.getItem() == Items.ELYTRA) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), EnderscapeItemSounds.ELYTRA_BREAK, player.getSoundSource(), 4.0F, 1.0F);
        }
    }
}