package net.bunten.enderscape.client;

import net.bunten.enderscape.registry.EnderscapeMusic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicType;
import net.minecraft.sound.MusicSound;

@Environment(EnvType.CLIENT)
public class ClientUtil {

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static MusicSound getEndMusic() {
        MusicSound music;
        
        var player = client.player;
        var access = player.world.getBiomeAccess();
        var biome = access.getBiome(player.getBlockPos()).value();

        if (client.inGameHud.getBossBarHud().shouldPlayDragonMusic()) {
            music = MusicType.DRAGON;
        } else {
            if (player.getY() >= 128) {
                music = EnderscapeMusic.SKY;
            } else if (player.getY() <= 24) {
                music = EnderscapeMusic.VOID;
            } else {
                music = biome.getMusic().orElse(MusicType.END);
            }
        }

        return music;
    }
}