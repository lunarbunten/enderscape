package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.sound.ElytraSoundInstance;
import net.bunten.enderscape.client.sound.ShulkerBulletSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl  {

    @Shadow
    private ClientLevel level;

    protected ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundEntityEventPacket;getEntity(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;"), cancellable = true)
    public void Enderscape$handleEntityEvent(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        Entity entity = packet.getEntity(this.level);
        if (entity != null) {
            if (packet.getEventId() == -68 && EnderscapeConfig.getInstance().elytraAddGlidingSound) {
                minecraft.getSoundManager().play(new ElytraSoundInstance((Player) entity));
                ci.cancel();
            }
        }
    }

    @Inject(method = "postAddEntitySoundInstance", at = @At(value = "HEAD"))
    public void Enderscape$handleEntityEvent(Entity entity, CallbackInfo ci) {
        if (entity instanceof ShulkerBullet bullet && EnderscapeConfig.getInstance().shulkerBulletLoopSound) minecraft.getSoundManager().queueTickingSound(new ShulkerBulletSoundInstance(bullet));
    }
}