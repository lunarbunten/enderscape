package net.enderscape.client.mixin;

import net.enderscape.registry.EndMusic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicType;
import net.minecraft.sound.MusicSound;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "getMusicType", at = @At("HEAD"), cancellable = true)
    public void getMusicType(CallbackInfoReturnable<MusicSound> info) {
        final MinecraftClient client = MinecraftClient.getInstance();
        @Nullable ClientPlayerEntity player = client.player;
        if (player != null) {
            assert client.world != null;
            if (client.world.getRegistryKey() == World.END) {
                World world = player.world;
                Biome biome = world.getBiomeAccess().getBiome(player.getBlockPos());
                MusicSound music;
                if (client.inGameHud.getBossBarHud().shouldPlayDragonMusic()) {
                    music = MusicType.DRAGON;
                } else {
                    if (player.isFallFlying()) {
                        music = EndMusic.ELYTRA;
                    } else if (player.getY() <= 20) {
                        music = EndMusic.VOID;
                    } else {
                        music = biome.getMusic().orElse(MusicType.END);
                    }
                }
                info.setReturnValue(music);
            }
        }
    }
}