package net.penumbra.enderscape.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @WrapWithCondition(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
    private boolean Enderscape$cancelHotbarScroll(Inventory instance, int slot) {
        return !EnderscapeMobEffects.isStunned(Minecraft.getInstance().player);
    }
}