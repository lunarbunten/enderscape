package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundRubbleShieldCooldownSoundPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundRubbleShieldCooldownSoundPayload> TYPE = new CustomPacketPayload.Type<>(Enderscape.id("clientbound_rubble_shield_cooldown_sound"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundRubbleShieldCooldownSoundPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundRubbleShieldCooldownSoundPayload::write, ClientboundRubbleShieldCooldownSoundPayload::new);

    private ClientboundRubbleShieldCooldownSoundPayload(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}