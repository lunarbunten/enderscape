package net.bunten.enderscape.mixin;

import net.bunten.enderscape.network.ClientboundRubbleShieldCooldownSoundPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ServerItemCooldowns;
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
    private void Enderscape$sendCooldownPacket(ResourceLocation location, CallbackInfo ci) {
        if (location.getPath().contains("rubble_shield")) {
            ServerPlayNetworking.send(player, new ClientboundRubbleShieldCooldownSoundPayload());
        }
    }
}