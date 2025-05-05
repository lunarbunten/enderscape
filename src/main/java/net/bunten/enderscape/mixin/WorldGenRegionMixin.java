package net.bunten.enderscape.mixin;

import net.bunten.enderscape.Enderscape;
import net.minecraft.Util;
import net.minecraft.server.level.WorldGenRegion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {

    @Shadow private @Nullable Supplier<String> currentlyGenerating;

    // this is probably a bad idea
    @Redirect(method = "ensureCanWrite", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V"))
    private void Enderscape$redirectShieldCheck(String string) {
        if (currentlyGenerating != null && currentlyGenerating.get().contains("enderscape") && !Enderscape.IS_DEBUG) return;
        Util.logAndPauseIfInIde(string);
    }
}