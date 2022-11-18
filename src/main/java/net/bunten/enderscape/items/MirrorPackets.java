package net.bunten.enderscape.items;

import io.netty.buffer.Unpooled;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class MirrorPackets {

    public static final ResourceLocation ID = Enderscape.id("mirror_transdimensional_sound");

    public static void sendPreTeleportPacket(ServerPlayer player, boolean isDifferentDimension) {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBoolean(isDifferentDimension);
        ServerPlayNetworking.send(player, ID, packet);
    }

    @Environment(EnvType.CLIENT)
    public static void initReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, listener, packet, sender) -> {
            boolean isDifferentDimension = packet.readBoolean();
            client.execute(() -> {
                EnderscapeClient.playTransdimensionalSound = isDifferentDimension;
                EnderscapeClient.postMirrorUseTicks = 60;
            });
        });
    }
}