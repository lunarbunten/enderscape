package net.penumbra.enderscape.registry.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.EnderscapeClient;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.manager.ClientsideConstants;
import net.penumbra.enderscape.manager.ClientsideVariables;
import net.penumbra.enderscape.network.*;
import net.penumbra.enderscape.registry.EnderscapeRegistries;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import net.penumbra.enderscape.registry.sound.EnderscapeUiSounds;
import net.penumbra.enderscape.sound.EndermanStareSoundInstance;
import net.penumbra.enderscape.sound.StructureMusic;
import net.penumbra.enderscape.util.BlockUtil;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class EnderscapeClientNetworking {

    private static void receiveDashJumpPayload(ClientboundDashJumpPayload payload, ClientPlayNetworking.Context context) {
        Minecraft minecraft = context.client();
        LocalPlayer player = minecraft.player;
        Vec2 power = payload.power();

        minecraft.execute(() -> {
            if (player == null || !player.isAlive() || player.isSpectator()) return;

            Vec3 travel = new Vec3(player.xxa, player.yya, player.zza).normalize();

            float sinYRot = Mth.sin(player.getYRot() * (Mth.PI / 180));
            float cosYRot = Mth.cos(player.getYRot() * (Mth.PI / 180));

            player.setDeltaMovement(new Vec3(travel.x * power.x * cosYRot - travel.z * power.x * sinYRot, power.y, travel.z * power.x * cosYRot + travel.x * power.x * sinYRot));
        });
    }

    private static void receiveDashJumpSoundPayload(ClientboundDashJumpSoundPayload payload, ClientPlayNetworking.Context context) {
        Minecraft minecraft = context.client();

        minecraft.execute(() -> {
            ClientLevel level = minecraft.level;

            if (level != null) {
                Entity entity = level.getEntity(payload.entityId());

                if (entity != null && entity.isAlive() && !entity.isSpectator()) {
                    level.registryAccess().lookupOrThrow(Registries.SOUND_EVENT).get(payload.soundEvent()).ifPresent(sound -> {
                        level.playLocalSound(entity, sound.value(), entity.getSoundSource(), 1.0F, 1.0F);
                    });
                }
            }
        });
    }

    private static void receiveHealthVoidPurifySoundPayload(ClientboundHealthVoidPurifySoundPayload payload, ClientPlayNetworking.Context context) {
        Minecraft minecraft = context.client();
        LocalPlayer player = minecraft.player;

        minecraft.execute(() -> {
            if (player != null) {
                player.playSound(EnderscapeUiSounds.HEALTH_VOID_PURIFY, 1.0F, 1.0F);
            }
        });
    }

    private static void receiveLodestoneTeleportationInfoPayload(ClientboundLodestoneTeleportationInfoPayload payload, ClientPlayNetworking.Context context) {
        Minecraft minecraft = context.client();
        ClientsideVariables variables = EnderscapeClient.clientsideVariables();

        minecraft.execute(() -> {
            variables.lodestoneTeleportationOverlayTexture = Optional.of(payload.overlayTexture());
            variables.lodestoneTeleportationVignetteTexture = Optional.of(payload.vignetteTexture());
            variables.lodestoneTeleportationTicks = ClientsideConstants.MAX_LODESTONE_TELEPORTATION_TICKS;
        });
    }

    private static void receiveNebuliteOreSoundPayload(ClientboundNebuliteOreSoundPayload payload, ClientPlayNetworking.Context context) {
        Minecraft minecraft = context.client();
        minecraft.execute(() -> {
            BlockPos nebulite = payload.globalPos().pos();
            ResourceKey<Level> dimension = payload.globalPos().dimension();

            ClientLevel level = minecraft.level;
            Entity entity = minecraft.getCameraEntity();

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
        Minecraft minecraft = context.client();
        minecraft.execute(() -> {
            if (minecraft.player != null) minecraft.getSoundManager().play(SimpleSoundInstance.forUI(EnderscapeUiSounds.RUBBLE_SHIELD_COOLDOWN_OVER, 1));
        });
    }

    private static void receiveSkipSongPayload(ClientboundSkipSongPayload payload, ClientPlayNetworking.Context context) {
        Minecraft minecraft = context.client();
        minecraft.execute(() -> {
            minecraft.getMusicManager().stopPlaying();
        });
    }

    private static void receiveStructureChangedPayload(ClientboundStructureChangedPayload payload, ClientPlayNetworking.Context context) {
        Minecraft minecraft = context.client();
        ClientsideVariables variables = EnderscapeClient.clientsideVariables();
        Identifier location = payload.location();
        LocalPlayer player = minecraft.player;

        minecraft.execute(() -> {
            if (minecraft.level == null || player == null) return;
            Registry<StructureMusic> registry = minecraft.level.registryAccess().lookupOrThrow(EnderscapeRegistries.STRUCTURE_MUSIC);
            boolean creative = player.getAbilities().instabuild && player.getAbilities().mayfly;

            variables.structureMusic = registry.stream()
                    .filter(music -> music.permittedStructures().contains(location))
                    .map(mus -> mus.music().select(creative, player.isUnderWater()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();
        });
    }

    private static void receiveSystemMessagePayload(ClientboundSystemMessagePayload payload, ClientPlayNetworking.Context context) {
        Minecraft minecraft = context.client();
        LocalPlayer player = minecraft.player;

        minecraft.execute(() -> {
            if (player != null) {
                player.sendSystemMessage(Component.translatable(payload.translationKey()));
            }
        });
    }

    private static void receiveTargetedByEndermanPayload(ClientboundTargetedByEndermanPayload payload, ClientPlayNetworking.Context context) {
        Minecraft minecraft = context.client();
        ClientsideVariables variables = EnderscapeClient.clientsideVariables();
        int endermanId = payload.endermanId();
        boolean playStereoStareSound = payload.playStereoStareSound();

        minecraft.execute(() -> {
            if (variables.stareSoundInstance == null || !minecraft.getSoundManager().isActive(variables.stareSoundInstance)) {
                if (playStereoStareSound) {
                    minecraft.getSoundManager().play(variables.stareSoundInstance = new EndermanStareSoundInstance(minecraft, endermanId));
                }

                if (EnderscapeConfig.getInstance().endermanAngerOverlay) {
                    variables.endermanAngerTicks = ClientsideConstants.ENDERMAN_ANGER_MAX_TICKS;
                }
            }
        });
    }

    private static void receiveTransdimensionalTravelSoundPayload(ClientboundTransdimensionalTravelSoundPayload payload, ClientPlayNetworking.Context context) {
        Minecraft minecraft = context.client();
        minecraft.execute(() -> {
            ClientLevel level = minecraft.level;

            if (level != null) {
                level.registryAccess().lookupOrThrow(Registries.SOUND_EVENT).get(payload.soundEvent()).ifPresent(sound -> {
                    minecraft.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(sound.value(), 1.0F, 0.4F));
                });
            }
        });
    }

    static {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundDashJumpPayload.TYPE, EnderscapeClientNetworking::receiveDashJumpPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundDashJumpSoundPayload.TYPE, EnderscapeClientNetworking::receiveDashJumpSoundPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundHealthVoidPurifySoundPayload.TYPE, EnderscapeClientNetworking::receiveHealthVoidPurifySoundPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundLodestoneTeleportationInfoPayload.TYPE, EnderscapeClientNetworking::receiveLodestoneTeleportationInfoPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundNebuliteOreSoundPayload.TYPE, EnderscapeClientNetworking::receiveNebuliteOreSoundPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundRubbleShieldCooldownSoundPayload.TYPE, EnderscapeClientNetworking::receiveRubbleShieldCooldownSoundPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundSkipSongPayload.TYPE, EnderscapeClientNetworking::receiveSkipSongPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundStructureChangedPayload.TYPE, EnderscapeClientNetworking::receiveStructureChangedPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundSystemMessagePayload.TYPE, EnderscapeClientNetworking::receiveSystemMessagePayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundTargetedByEndermanPayload.TYPE, EnderscapeClientNetworking::receiveTargetedByEndermanPayload);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundTransdimensionalTravelSoundPayload.TYPE, EnderscapeClientNetworking::receiveTransdimensionalTravelSoundPayload);
    }
}
