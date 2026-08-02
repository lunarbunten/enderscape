package net.penumbra.enderscape.mixin.item;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ServerItemCooldowns;
import net.penumbra.enderscape.network.ClientboundRubbleShieldCooldownSoundPayload;
import net.penumbra.enderscape.references.EnderscapeItemIds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerItemCooldowns.class)
public class ServerItemCooldownsMixin {

    @Shadow
    @Final
    private ServerPlayer player;

    @Inject(at = @At("TAIL"), method = "onCooldownEnded")
    private void Enderscape$sendCooldownPacket(Identifier location, CallbackInfo ci) {
        if (location.equals(EnderscapeItemIds.RUBBLE_SHIELD.identifier())) {
            ServerPlayNetworking.send(player, new ClientboundRubbleShieldCooldownSoundPayload());
        }
    }
}