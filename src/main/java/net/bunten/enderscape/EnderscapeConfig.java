package net.bunten.enderscape;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;

public class EnderscapeConfig {

    public static ConfigClassHandler<EnderscapeConfig> HANDLER = ConfigClassHandler.createBuilder(EnderscapeConfig.class)
            .id(Enderscape.id("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("enderscape.json5"))
                    .setJson5(true)
                    .build())
            .build();

    public static EnderscapeConfig getInstance() {
        return HANDLER.instance();
    }

    @SerialEntry public boolean ambienceUpdateDefaultAdditions = true;
    @SerialEntry public boolean ambienceUpdateDefaultFogColor = true;
    @SerialEntry public boolean ambienceUpdateDefaultFoliageColor = true;
    @SerialEntry public boolean ambienceUpdateDefaultGrassColor = true;
    @SerialEntry public boolean ambienceUpdateDefaultLoop = true;
    @SerialEntry public boolean ambienceUpdateDefaultMood = true;
    @SerialEntry public boolean ambienceUpdateDefaultMusic = true;
    @SerialEntry public boolean ambienceUpdateDefaultParticles = true;
    @SerialEntry public boolean ambienceUpdateDefaultSkyColor = true;
    @SerialEntry public boolean ambienceUpdateDefaultWaterColor = true;
    @SerialEntry public boolean ambienceUpdateDefaultWaterFogColor = true;
    @SerialEntry public boolean blockSoundsUpdateEndRods = true;
    @SerialEntry public boolean blockSoundUpdateEndPortalFrame = true;
    @SerialEntry public boolean blockSoundUpdateEndPortals = true;
    @SerialEntry public boolean blockSoundUpdateEndStone = true;
    @SerialEntry public boolean blockSoundUpdateEndStoneBricks = true;
    @SerialEntry public boolean blockSoundUpdateShulkerBoxes = true;
    @SerialEntry public boolean blocksSoundUpdateChorus = true;
    @SerialEntry public boolean blocksSoundUpdatePurpur = true;
    @SerialEntry public boolean chorusFlowerHumming = true;
    @SerialEntry public boolean chorusFlowerPollen = true;
    @SerialEntry public boolean debugHudClientInfo = true;
    @SerialEntry public boolean debugHudEnabled = false;
    @SerialEntry public boolean debugHudMusicInfo = true;
    @SerialEntry public boolean debugHudPlayerInfo = true;
    @SerialEntry public boolean debugMagniaSproutHitboxes = false;
    @SerialEntry public boolean elytraAddFovEffects = true;
    @SerialEntry public boolean elytraAddGlidingSound = true;
    @SerialEntry public boolean elytraAddOpenCloseSounds = true;
    @SerialEntry public boolean elytraSneakToStopGliding = true;
    @SerialEntry public boolean elytraUpdateEquipSound = true;
    @SerialEntry public boolean endermanStaticOverlay = true;
    @SerialEntry public boolean endermanStaticSound = true;
    @SerialEntry public boolean endermanStereoStareSound = true;
    @SerialEntry public boolean endermiteEmissiveEyes = true;
    @SerialEntry public boolean endermiteExpandHitRange = true;
    @SerialEntry public boolean endermiteUpdateSounds = true;
    @SerialEntry public boolean enderPearlAddParticles = true;
    @SerialEntry public boolean enderPearlBreakParticles = true;
    @SerialEntry public boolean enderPearlUpdateTeleportSound = true;
    @SerialEntry public boolean enderPearlUpdateThrowSound = true;
    @SerialEntry public boolean endPortalUpdateParticles = true;
    @SerialEntry public boolean endPortalUpdateTravelSound = true;
    @SerialEntry public boolean mirrorScreenEffectEnabled = true;
    @SerialEntry public boolean mirrorTooltipDisplayCoordinates = false;
    @SerialEntry public boolean mirrorTooltipDisplayDimension = true;
    @SerialEntry public boolean mirrorTooltipDisplayDistance = true;
    @SerialEntry public boolean mirrorTooltipEnabled = true;
    @SerialEntry public boolean mirrorTooltipShiftToDisplay = false;
    @SerialEntry public boolean nebuliteToolHudEnabled = true;
    @SerialEntry public boolean portalParticleEmissive = true;
    @SerialEntry public boolean rubblemiteExpandHitRange = true;
    @SerialEntry public boolean shulkerBulletEnforceOwnerLimit = true;
    @SerialEntry public boolean shulkerBulletLoopSound = true;
    @SerialEntry public boolean shulkerBulletRebalanceLevitation = true;
    @SerialEntry public boolean shulkerHurtByPiercing = true;
    @SerialEntry public boolean silverfishExpandHitRange = true;
    @SerialEntry public boolean skyboxAddDynamicFogDensity = true;
    @SerialEntry public boolean skyboxScalesBrightnessWithGamma = true;
    @SerialEntry public boolean skyboxUpdateEnabled = true;
    @SerialEntry public boolean structureMusicFadingEnabled = true;
    @SerialEntry public boolean tridentsReturnFromVoid = true;
    @SerialEntry public boolean voidPoofParticlesUponDeath = true;

    @SerialEntry public int elytraFovEffectIntensity = 100;
    @SerialEntry public int mirrorScreenEffectOverlayIntensity = 50;
    @SerialEntry public int mirrorScreenEffectVignetteIntensity = 50;
    @SerialEntry public int nebuliteToolHudOffset = 0;
    @SerialEntry public int nebuliteToolHudOpacity = 100;
    @SerialEntry public int shulkerBulletEnforceCountLimit = 3;
    @SerialEntry public int shulkerBulletEnforceDistanceLimit = 30;
    @SerialEntry public int shulkerBulletEnforceTimeLimit = 30;
    @SerialEntry public int skyboxBrightnessScaleFactor = 40;

    static {
        HANDLER.load();
    }
}