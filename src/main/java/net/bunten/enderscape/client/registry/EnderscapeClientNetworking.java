package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.client.sound.EndermanStareSoundInstance;
import net.bunten.enderscape.client.sound.EndermanStaticSoundInstance;
import net.bunten.enderscape.network.*;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.bunten.enderscape.sound.StructureMusic;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

import static net.bunten.enderscape.client.EnderscapeClient.*;

public class EnderscapeClientNetworking {

    public static void receiveDashJumpPayload(ClientboundDashJumpPayload payload, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        Vec2 power = payload.power();

        client.execute(() -> {
            if (player == null || !player.isAlive() || player.isSpectator()) return;

            Vec3 travel = new Vec3(player.xxa, player.yya, player.zza).normalize();

            float sinYRot = Mth.sin(player.getYRot() * (Mth.PI / 180));
            float cosYRot = Mth.cos(player.getYRot() * (Mth.PI / 180));

            player.setDeltaMovement(new Vec3(travel.x * power.x * cosYRot - travel.z * power.x * sinYRot, power.y, travel.z * power.x * cosYRot + travel.x * power.x * sinYRot));
        });
    }

    public static void receiveDashJumpSoundPayload(ClientboundDashJumpSoundPayload payload, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
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

    public static void receiveLodestoneTeleportationInfoPayload(ClientboundLodestoneTeleportationInfoPayload payload, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        client.execute(() -> {
            lodestoneTeleportationOverlayTexture = Optional.of(payload.overlayTexture());
            lodestoneTeleportationVignetteTexture = Optional.of(payload.vignetteTexture());
            lodestoneTeleportationTicks = MAX_LODESTONE_TELEPORTATION_TICKS;
        });
    }

    public static void receiveNebuliteOreSoundPayload(ClientboundNebuliteOreSoundPayload payload, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
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

    public static void receiveRubbleShieldCooldownSoundPayload(ClientboundRubbleShieldCooldownSoundPayload payload, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        client.execute(() -> {
            if (client.player != null) client.getSoundManager().play(SimpleSoundInstance.forUI(EnderscapeItemSounds.RUBBLE_SHIELD_COOLDOWN_OVER, 1));
        });
    }

    public static void receiveStareOverlayPayload(ClientboundStareOverlayPayload payload, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        client.execute(() -> {
            if (EnderscapeClient.stareTicks < MAX_STARE_STICKS) EnderscapeClient.stareTicks += 2;
            if ((staticSoundInstance == null || !client.getSoundManager().isActive(staticSoundInstance)) && EnderscapeConfig.getInstance().endermanStaticSound) client.getSoundManager().play(staticSoundInstance = new EndermanStaticSoundInstance(client));
        });
    }

    public static void receiveStareSoundPayload(ClientboundStareSoundPayload payload, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        int entityId = payload.entityId();
        client.execute(() -> {
            if ((stareSoundInstance == null || !client.getSoundManager().isActive(stareSoundInstance))) client.getSoundManager().play(stareSoundInstance = new EndermanStareSoundInstance(client, entityId));
        });
    }

    public static void receiveStructureChangedPayload(ClientboundStructureChangedPayload payload, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        ResourceLocation location = payload.location();

        client.execute(() -> {
            if (client.level == null) return;

            Registry<StructureMusic> registry = client.level.registryAccess().registryOrThrow(EnderscapeRegistries.STRUCTURE_MUSIC);
            EnderscapeClient.structureMusic = registry.stream().filter(music -> music.permittedStructures().contains(location)).map(StructureMusic::music).findFirst();
        });
    }

    public static void receiveTransdimensionalTravelSoundPayload(ClientboundTransdimensionalTravelSoundPayload payload, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        client.execute(() -> {
            SoundEvent soundEvent = client.level.registryAccess()
                    .lookupOrThrow(Registries.SOUND_EVENT)
                    .getOrThrow(ResourceKey.create(Registries.SOUND_EVENT, payload.soundEvent()))
                    .value();
            client.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(soundEvent, 1.0F, 0.4F));
        });
    }
}
