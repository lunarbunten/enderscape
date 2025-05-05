package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientboundDashJumpSoundPayload(int entityId, ResourceLocation soundEvent) implements CustomPacketPayload {
    public static final Type<ClientboundDashJumpSoundPayload> TYPE = new Type<>(Enderscape.id("clientbound_dash_jump_sound"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundDashJumpSoundPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundDashJumpSoundPayload::write, ClientboundDashJumpSoundPayload::new);

    private ClientboundDashJumpSoundPayload(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeResourceLocation(soundEvent);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}