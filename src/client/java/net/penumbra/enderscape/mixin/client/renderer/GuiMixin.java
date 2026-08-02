package net.penumbra.enderscape.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.penumbra.enderscape.EnderscapeClient;
import net.penumbra.enderscape.manager.ClientsideDashJumpManager;
import net.penumbra.enderscape.manager.ClientsideVariables;
import net.penumbra.enderscape.manager.ClientsideVoidManager;
import net.penumbra.enderscape.manager.VoidManager;
import org.joml.Vector2i;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow @Final private static Identifier HEART_VEHICLE_CONTAINER_SPRITE;
    @Shadow @Nullable protected abstract LivingEntity getPlayerVehicleWithHealth();
    @Shadow protected abstract int getVehicleMaxHearts(@Nullable LivingEntity vehicle);
    @Shadow @Nullable protected abstract Player getCameraPlayer();

    @Shadow private long lastHealthTime;
    @Shadow private long healthBlinkTime;
    @Shadow private int tickCount;

    @Unique private final ClientsideVariables variables = EnderscapeClient.clientsideVariables();
    @Unique private float lastVoidHealth;

    @Inject(method = "onDisconnected", at = @At("TAIL"))
    public void Enderscape$onDisconnected(CallbackInfo info) {
        variables.reset();
    }

    @Inject(method = "extractHeart", at = @At("TAIL"))
    public void Enderscape$copyPlayerHeartPoints(final GuiGraphicsExtractor graphics, final Gui.HeartType type, final int xo, final int yo, final boolean isHardcore, final boolean blinks, final boolean half, CallbackInfo info) {
        if (type.equals(Gui.HeartType.CONTAINER)) {
            variables.playerHearts.add(new Vector2i(xo, yo));
        }
    }

    @ModifyArgs(method = "extractVehicleHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V", ordinal = 0))
    private void Enderscape$copyVehicleHeartPoints(Args args) {
        if (args.get(1).equals(HEART_VEHICLE_CONTAINER_SPRITE)) {
            variables.vehicleHearts.add(
                    new Vector2i(
                            (int) args.get(2),
                            (int) args.get(3)
                    )
            );
        }
    }

    @Inject(
            method = "extractPlayerHealth",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/Gui;lastHealth:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void Enderscape$setLastVoidHealth(GuiGraphicsExtractor graphics, CallbackInfo info) {
        Player player = getCameraPlayer();

        if (player != null) {
            float current = VoidManager.getVoidedHealth(player);

            if (current != lastVoidHealth) {
                lastHealthTime = Util.getMillis();
                healthBlinkTime = tickCount + (current < lastVoidHealth ? 20 : 10);
            }

            lastVoidHealth = current;
        }
    }

    @Inject(method = "extractHearts", at = @At("HEAD"))
    public void Enderscape$clearPlayerHeartPoints(final GuiGraphicsExtractor graphics, final Player player, final int xLeft, final int yLineBase, final int healthRowHeight, final int heartOffsetIndex, final float maxHealth, final int currentHealth, final int oldHealth, final int absorption, final boolean blink, CallbackInfo info) {
        variables.playerHearts.clear();
    }

    @Inject(method = "extractHearts", at = @At("TAIL"))
    public void Enderscape$extractPlayerVoidedHealth(final GuiGraphicsExtractor graphics, final Player player, final int xLeft, final int yLineBase, final int healthRowHeight, final int heartOffsetIndex, final float maxHealth, final int currentHealth, final int oldHealth, final int absorption, final boolean blink, CallbackInfo info) {
        if (player != null) {
            boolean hardcore = player.level().getLevelData().isHardcore();

            ClientsideVoidManager.extractVoidedHealthOverlay(graphics, player, variables.playerHearts, false, hardcore, blink);
            ClientsideVoidManager.extractOuterVoidWarningOverlay(graphics, player, variables.playerHearts, maxHealth * 2, false, variables.playerOuterVoidWarning);
        }
    }

    @Inject(method = "extractVehicleHealth", at = @At("HEAD"))
    public void Enderscape$clearVehicleHeartPoints(GuiGraphicsExtractor graphics, CallbackInfo info) {
        variables.vehicleHearts.clear();
    }

    @Inject(method = "extractVehicleHealth", at = @At("TAIL"))
    public void Enderscape$extractVehicleVoidedHealth(GuiGraphicsExtractor graphics, CallbackInfo info) {
        LivingEntity vehicle = getPlayerVehicleWithHealth();

        if (vehicle != null) {
            ClientsideVoidManager.extractVoidedHealthOverlay(graphics, vehicle, variables.vehicleHearts, true, false, false);
            ClientsideVoidManager.extractOuterVoidWarningOverlay(graphics, vehicle, variables.vehicleHearts, getVehicleMaxHearts(vehicle) * 2, true, variables.vehicleOuterVoidWarning);
        }
    }

    /*
        Contextual bar changes
     */

    @WrapOperation(
            method = "extractHotbarAndDecorations",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
            )
    )
    public void Enderscape$stopRenderingBarBackground(ContextualBarRenderer instance, GuiGraphicsExtractor graphics, DeltaTracker tracker, Operation<Void> original) {
        if (ClientsideDashJumpManager.skipRenderingDashJumpBar()) {
            original.call(instance, graphics, tracker);
        } else {
            ClientsideDashJumpManager.extractDashJumpChargeBar(graphics);
        }
    }

    @WrapOperation(
            method = "extractHotbarAndDecorations",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
            )
    )
    public void Enderscape$stopRenderingBar(ContextualBarRenderer instance, GuiGraphicsExtractor graphics, DeltaTracker tracker, Operation<Void> original) {
        if (ClientsideDashJumpManager.skipRenderingDashJumpBar()) {
            original.call(instance, graphics, tracker);
        }
    }
}