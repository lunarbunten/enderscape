package net.penumbra.enderscape.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.config.value.EndFlashStyle;
import net.penumbra.enderscape.config.value.LightingStyle;

public class EnderscapeConfig {

    public static final ConfigClassHandler<EnderscapeConfig> HANDLER = ConfigClassHandler.createBuilder(EnderscapeConfig.class)
            .id(Enderscape.id("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("enderscape.json5"))
                    .setJson5(true)
                    .build())
            .build();

    public static EnderscapeConfig getInstance() {
        return HANDLER.instance();
    }

    @SerialEntry public boolean creativeTabEnabled = true;
    @SerialEntry public boolean includeItemsInVanillaCreativeTabs = true;
    @SerialEntry public boolean includeColoredMirrorsInCreativeTabs = true;

    @SerialEntry public boolean defaultResourcePackImprovedVisuals = true;

    @SerialEntry public boolean defaultDataPackFixLevitationAdvancement = true;
    @SerialEntry public boolean defaultDataPackFixVanillaRecipes = true;
    @SerialEntry public boolean defaultDataPackNewEndCities = true;
    @SerialEntry public boolean defaultDataPackNewStrongholds = true;
    @SerialEntry public boolean defaultDataPackNewTerrain = true;

    @SerialEntry public boolean supplementVanillaEndCityTreasureLoot = true;
    @SerialEntry public boolean supplementVanillaStrongholdLibraryLoot = true;

    @SerialEntry public boolean editWorldEnderscapeDataPacksButton = true;
    @SerialEntry public boolean vanillaWorldWarning = true;

    @SerialEntry public boolean ambienceUpdateFoliageColors = true;
    @SerialEntry public boolean ambienceUpdateGrassColors = true;
    @SerialEntry public boolean ambienceUpdateWaterColors = true;
    @SerialEntry public boolean ambienceUpdateWaterFogColors = true;
    @SerialEntry public boolean elytraHungerExhaustion = true;
    @SerialEntry public boolean updateEndRodSoundType = true;
    @SerialEntry public boolean updateEndPortalFrameSoundType = true;
    @SerialEntry public boolean updateEndPortalSoundType = true;
    @SerialEntry public boolean updateEndStoneSoundType = true;
    @SerialEntry public boolean updateEndStoneBrickSoundType = true;
    @SerialEntry public boolean updateShulkerBoxSoundType = true;
    @SerialEntry public boolean updateChorusSoundType = true;
    @SerialEntry public boolean updatePurpurSoundType = true;
    @SerialEntry public boolean chorusFlowerHumming = true;
    @SerialEntry public boolean chorusFlowerPollen = true;
    @SerialEntry public boolean elytraAddGlidingSound = true;
    @SerialEntry public boolean elytraAddOpenCloseSounds = true;
    @SerialEntry public boolean elytraSneakToStopGliding = true;
    @SerialEntry public boolean elytraUpdateEquipSound = true;
    @SerialEntry public boolean endermanLightSensitiveEyes = true;
    @SerialEntry public boolean endermanAngerOverlay = true;
    @SerialEntry public boolean endermanStereoStareSound = true;
    @SerialEntry public boolean endermanUpdateRenderer = true;
    @SerialEntry public boolean endermiteEmissiveEyes = true;
    @SerialEntry public boolean endermiteExpandHitRange = true;
    @SerialEntry public boolean endermiteNaturalSpawnsObeyLightLevel = true;
    @SerialEntry public boolean endermiteUpdateSounds = true;
    @SerialEntry public boolean enderPearlAddFlyingParticles = true;
    @SerialEntry public boolean enderPearlAddShatteringParticles = true;
    @SerialEntry public boolean enderPearlUpdateTeleportSound = true;
    @SerialEntry public boolean enderPearlUpdateThrowSound = true;
    @SerialEntry public boolean endPortalUpdateParticles = true;
    @SerialEntry public boolean endPortalUpdateTravelSound = true;
    @SerialEntry public boolean entityUpdatePortalParticles = true;
    @SerialEntry public boolean endFlashInfluencesSkybox = true;
    @SerialEntry public boolean fogDensityUpdated = true;
    @SerialEntry public boolean mirrorTooltipDisplayCoordinates = false;
    @SerialEntry public boolean mirrorTooltipDisplayDimension = true;
    @SerialEntry public boolean mirrorTooltipDisplayDistance = true;
    @SerialEntry public boolean mirrorTooltipShiftToDisplay = false;
    @SerialEntry public boolean nebuliteToolHudEnabled = true;
    @SerialEntry public boolean portalParticleEmissive = true;
    @SerialEntry public boolean rubblemiteExpandHitRange = true;
    @SerialEntry public boolean shulkerBulletEnforceOwnerLimit = true;
    @SerialEntry public boolean shulkerBulletLoopSound = true;
    @SerialEntry public boolean shulkerBulletRebalanceLevitation = true;
    @SerialEntry public boolean shulkerHurtByPiercing = true;
    @SerialEntry public boolean silverfishDelayBeforeInfestingStone = true;
    @SerialEntry public boolean silverfishExpandHitRange = true;
    @SerialEntry public boolean silverfishNaturalSpawnsObeyLightLevel = true;
    @SerialEntry public boolean skyboxUpdateEnabled = true;
    @SerialEntry public boolean structureMusicFadingEnabled = true;
    @SerialEntry public boolean tridentsReturnFromVoid = true;
    @SerialEntry public boolean voidPoofParticlesUponDeath = true;

    @SerialEntry public int outerVoidHeightTreshold = -8;
    @SerialEntry public int elytraFovEffectIntensity = 80;
    @SerialEntry public int mirrorScreenEffectIntensity = 50;
    @SerialEntry public int nebuliteToolHudOffset = 13;
    @SerialEntry public int nebuliteToolHudOpacity = 100;
    @SerialEntry public int rubbleShieldFovEffectIntensity = 50;
    @SerialEntry public int shulkerBulletEnforceCountLimit = 3;
    @SerialEntry public int shulkerBulletEnforceDistanceLimit = 30;
    @SerialEntry public int shulkerBulletEnforceTimeLimit = 30;
    @SerialEntry public int skyboxBrightnessScaleFactor = 40;

    @SerialEntry public float endFlashFrequency = 2.0F;

    @SerialEntry public EndFlashStyle endFlashStyle = EndFlashStyle.IMPROVED;
    @SerialEntry public LightingStyle lightingStyle = LightingStyle.IMPROVED;

    static {
        HANDLER.load();
    }
}