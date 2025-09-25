package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.bunten.enderscape.Enderscape;
import net.minecraft.server.level.WorldGenRegion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {

    @Shadow private @Nullable Supplier<String> currentlyGenerating;

    // this is probably a bad idea
    @WrapWithCondition(method = "ensureCanWrite", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V"))
    private boolean Enderscape$redirectShieldCheck(String string) {
        if (currentlyGenerating != null && currentlyGenerating.get().contains("enderscape") && !Enderscape.IS_DEBUG) return false;
        return true;
    }
}