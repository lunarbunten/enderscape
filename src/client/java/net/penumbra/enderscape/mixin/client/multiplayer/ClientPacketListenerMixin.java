package net.penumbra.enderscape.mixin.client.multiplayer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.EnderscapeClient;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import net.penumbra.enderscape.sound.ElytraSoundInstance;
import net.penumbra.enderscape.sound.ShulkerBulletSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl  {

    @Shadow
    private ClientLevel level;

    @Unique
    private boolean Enderscape$entityWasVoided = false;

    protected ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundEntityEventPacket;getEntity(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;"), cancellable = true)
    public void Enderscape$handleEntityEvent(ClientboundEntityEventPacket packet, CallbackInfo info) {
        Entity entity = packet.getEntity(level);

        if (entity != null) {
            if (entity instanceof LivingEntity mob) Enderscape$entityWasVoided = VoidManager.hasVoidedHealth(mob);

            if (packet.getEventId() == -68 && entity instanceof Player player && EnderscapeConfig.getInstance().elytraAddGlidingSound) {
                minecraft.getSoundManager().play(new ElytraSoundInstance(player));
                info.cancel();
            }
        }
    }

    @WrapOperation(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;displayItemActivation(Lnet/minecraft/world/item/ItemStack;)V"))
    public void Enderscape$changeTotemActivation(GameRenderer renderer, ItemStack stack, Operation<Void> original) {
        EnderscapeClient.clientsideVariables().displayVoidedTotemEffect = Enderscape$entityWasVoided;
        original.call(renderer, stack);
    }

    @ModifyArg(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"), index = 3)
    public SoundEvent Enderscape$changeTotemSoundWhenVoided(SoundEvent sound) {
        return Enderscape$entityWasVoided ? EnderscapeItemSounds.TOTEM_OF_UNDYING_VOIDED : sound;
    }

    @Inject(method = "postAddEntitySoundInstance", at = @At(value = "HEAD"))
    public void Enderscape$handleEntityEvent(Entity entity, CallbackInfo ci) {
        if (entity instanceof ShulkerBullet bullet && EnderscapeConfig.getInstance().shulkerBulletLoopSound) minecraft.getSoundManager().queueTickingSound(new ShulkerBulletSoundInstance(bullet));
    }
}