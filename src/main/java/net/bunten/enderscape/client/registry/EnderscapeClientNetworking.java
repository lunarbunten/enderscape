package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.client.entity.EndermanStareSoundInstance;
import net.bunten.enderscape.client.entity.EndermanStaticSoundInstance;
import net.bunten.enderscape.network.*;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.bunten.enderscape.registry.EnderscapeMusic;
import net.bunten.enderscape.util.BlockUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

import static net.bunten.enderscape.client.EnderscapeClient.MAX_STARE_STICKS;
import static net.bunten.enderscape.client.EnderscapeClient.staticSoundInstance;

@Environment(EnvType.CLIENT)
public class EnderscapeClientNetworking {

    private static void receiveDashJumpPayload(ClientboundDashJumpPayload payload, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        LocalPlayer player = client.player;

        client.execute(() -> {
            if (player == null || !player.isAlive() || player.isSpectator()) return;

            Vec3 travel = new Vec3(player.xxa, player.yya, player.zza).normalize();

            float sinYRot = Mth.sin(player.getYRot() * (Mth.PI / 180));
            float cosYRot = Mth.cos(player.getYRot() * (Mth.PI / 180));
            float hozPower = player.isFallFlying() ? payload.horizontalPower() * payload.glideVelocityFactor() : payload.horizontalPower();
            float verPower = player.isFallFlying() ? payload.verticalPower() * payload.glideVelocityFactor() : payload.verticalPower();

            player.setDeltaMovement(new Vec3(travel.x * hozPower * cosYRot - travel.z * hozPower * sinYRot, verPower, travel.z * hozPower * cosYRot + travel.x * hozPower * sinYRot));
        });
    }

    private static void receiveDashJumpSoundPayload(ClientboundDashJumpSoundPayload payload, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        client.execute(() -> {
            Entity entity = client.level.getEntity(payload.entityId());

            if (entity != null && entity.isAlive() && !entity.isSpectator()) {
                SoundEvent soundEvent = client.level.registryAccess()
                        .lookupOrThrow(Registries.SOUND_EVENT)
                        .getOrThrow(ResourceKey.create(Registries.SOUND_EVENT, payload.soundEvent()))
                        .value();

                client.level.playLocalSound(entity, soundEvent, entity.getSoundSource(), 1.0F, 1.0F);
            }
        });
    }

    private static void receiveMirrorTeleportPayload(ClientboundMirrorTeleportInfoPayload payload, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        client.execute(() -> {
            EnderscapeClient.postMirrorUseTicks = 60;
        });
    }

    private static void receiveNebuliteOreSoundPayload(ClientboundNebuliteOreSoundPayload payload, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        client.execute(() -> {
            BlockPos nebulite = payload.globalPos().pos();
            ResourceKey<Level> dimension = payload.globalPos().dimension();

            ClientLevel level = client.level;
            Entity entity = client.cameraEntity;

            if (level != null && level.dimension() == dimension && entity instanceof LivingEntity mob) {
                SoundEvent sound;

                if (BlockUtil.isBlockObstructed(level, nebulite)) {
                    sound = EnderscapeBlockSounds.NEBULITE_ORE_IDLE_OBSTRUCTED;
                } else {
                    if (mob.blockPosition().closerThan(nebulite, 12)) {
                        sound = EnderscapeBlockSounds.NEBULITE_ORE_IDLE;
                    } else {
                        sound = EnderscapeBlockSounds.NEBULITE_ORE_IDLE_FAR;
                    }
                }

                float range = (Mth.clamp((float) (nebulite.getY() - mob.getY()), -8.0F, 0.0F) / 20) + (Mth.nextFloat(level.getRandom(), 0.9F, 1.1F));

                level.playLocalSound(nebulite.getX(), nebulite.getY(), nebulite.getZ(), sound, SoundSource.BLOCKS, range, range, false);
            }
        });
    }

    private static void receiveRubbleShieldCooldownSoundPayload(ClientboundRubbleShieldCooldownSoundPayload payload, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        client.execute(() -> {
            if (client.player != null) client.getSoundManager().play(SimpleSoundInstance.forUI(EnderscapeItemSounds.RUBBLE_SHIELD_COOLDOWN_OVER, 1));
        });
    }

    private static void receiveStareOverlayPayload(ClientboundStareOverlayPayload payload, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        client.execute(() -> {
            if (EnderscapeClient.stareTicks < MAX_STARE_STICKS) EnderscapeClient.stareTicks += 2;
            if ((staticSoundInstance == null || !client.getSoundManager().isActive(staticSoundInstance)) && EnderscapeConfig.getInstance().endermanStaticSound) client.getSoundManager().play(staticSoundInstance = new EndermanStaticSoundInstance(client));
        });
    }

    private static void receiveStareSoundPayload(ClientboundStareSoundPayload payload, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        int entityId = payload.entityId();
        client.execute(() -> client.getSoundManager().play(new EndermanStareSoundInstance(client, entityId)));
    }

    private static void receiveStructureChangedPayload(ClientboundStructureChangedPayload payload, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        ResourceLocation location = payload.location();

        client.execute(() -> {
            EnderscapeClient.structureMusic = location.equals(Enderscape.END_CITY_RESOURCE_KEY.location()) ? Optional.of(EnderscapeMusic.MUSIC_END_CITY) : Optional.empty();
        });
    }

    private static void receiveTransdimensionalTravelSoundPayload(ClientboundTransdimensionalTravelSoundPayload payload, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        client.execute(() -> client.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(EnderscapeItemSounds.MIRROR_TRANSDIMENSIONAL_TRAVEL, 1.0F, 0.4F)));
    }

    static {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundDashJumpPayload.TYPE, EnderscapeClientNetworking::receiveDashJumpPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundDashJumpSoundPayload.TYPE, EnderscapeClientNetworking::receiveDashJumpSoundPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundMirrorTeleportInfoPayload.TYPE, EnderscapeClientNetworking::receiveMirrorTeleportPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundNebuliteOreSoundPayload.TYPE, EnderscapeClientNetworking::receiveNebuliteOreSoundPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundRubbleShieldCooldownSoundPayload.TYPE, EnderscapeClientNetworking::receiveRubbleShieldCooldownSoundPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundStareOverlayPayload.TYPE, EnderscapeClientNetworking::receiveStareOverlayPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundStareSoundPayload.TYPE, EnderscapeClientNetworking::receiveStareSoundPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundStructureChangedPayload.TYPE, EnderscapeClientNetworking::receiveStructureChangedPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundTransdimensionalTravelSoundPayload.TYPE, EnderscapeClientNetworking::receiveTransdimensionalTravelSoundPayload);
    }
}
