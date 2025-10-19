package net.bunten.enderscape.client;

import com.google.common.reflect.Reflection;
import net.bunten.enderscape.client.block.MagniaSproutRenderer;
import net.bunten.enderscape.client.renderer.EnderscapeRenderPipelines;
import net.bunten.enderscape.client.sound.EndermanStareSoundInstance;
import net.bunten.enderscape.client.sound.EndermanStaticSoundInstance;
import net.bunten.enderscape.client.hud.HudElement;
import net.bunten.enderscape.client.item.NebuliteToolTooltip;
import net.bunten.enderscape.client.registry.*;
import net.bunten.enderscape.item.NebuliteToolComponent;
import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.sounds.Music;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class EnderscapeClient implements ClientModInitializer {

    public static final List<HudElement> HUD_ELEMENTS = new ArrayList<>();

    public static Optional<Music> structureMusic = Optional.empty();

    public static final int MAX_STARE_STICKS = 100;
    public static int stareTicks;

    public static int postMirrorUseTicks;

    @Nullable public static EndermanStareSoundInstance stareSoundInstance = null;
    @Nullable public static EndermanStaticSoundInstance staticSoundInstance = null;

    public static void register(HudElement element) {
        Objects.requireNonNull(element);
        HUD_ELEMENTS.add(element);
	}

    @Override
    public void onInitializeClient() {

        Reflection.initialize(
                EnderscapeClientNetworking.class,
                EnderscapeRenderPipelines.class,
                EnderscapeParticleProviders.class,
                EnderscapeEntityRenderData.class,
                EnderscapeBlockRenderLayerMap.class,
                EnderscapeBlockColorProviders.class,
                EnderscapeHudElements.class
        );

        BlockEntityRenderers.register(EnderscapeBlockEntities.MAGNIA_SPROUT, MagniaSproutRenderer::new);

        TooltipComponentCallback.EVENT.register((component) -> component instanceof NebuliteToolComponent tool ? new NebuliteToolTooltip(tool.stack()) : null);
    }
}