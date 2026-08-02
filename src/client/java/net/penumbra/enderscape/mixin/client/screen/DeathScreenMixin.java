package net.penumbra.enderscape.mixin.client.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.penumbra.enderscape.manager.EndHavenManager;
import net.penumbra.enderscape.network.ServerboundRespawnFromEndHavenPayload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {

    protected DeathScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    @Final
    private LocalPlayer player;

    @Shadow
    @Final
    private boolean hardcore;

    @ModifyArg(
            method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 0),
            index = 1
    )
    private int Enderscape$moveRespawnButton(int original) {
        if (EndHavenManager.promptHavenRespawnChoice(player)) {
            return original + 24;
        }

        return original;
    }

    @ModifyArg(
            method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 1),
            index = 1
    )
    private int moveTitleButton(int original) {
        if (EndHavenManager.promptHavenRespawnChoice(player)) {
            return original + 24;
        }

        return original;
    }

    @WrapOperation(
            method = "init",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1)
    )
    private <T> boolean Enderscape$displayLoadWarning(List<T> instance, T button, Operation<Boolean> original) {
        if (EndHavenManager.promptHavenRespawnChoice(player)) {
            Component message = hardcore ? Component.translatable("screen.enderscape.death.spectate_from_end_haven") : Component.translatable("screen.enderscape.death.respawn_from_end_haven");

            original.call(instance, addRenderableWidget(Button.builder(message, self -> {
                ClientPlayNetworking.send(new ServerboundRespawnFromEndHavenPayload());
                player.respawn();
                self.active = false;
            }).bounds(width / 2 - 100, height / 4 + 72, 200, 20).build()));
        }

        return original.call(instance, button);
    }
}