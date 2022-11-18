package net.bunten.enderscape.blocks;

import java.util.function.Predicate;

import io.netty.buffer.Unpooled;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class NebuliteOrePackets {

    public static final ResourceLocation ID = Enderscape.id("nebulite_ore_ambient");

    public static void sendSoundToPlayers(ServerLevel world, BlockPos pos) {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeGlobalPos(GlobalPos.of(world.dimension(), pos));
        Predicate<ServerPlayer> dimension = (player) -> player.getLevel().dimension() == world.dimension() && player.blockPosition().closerThan(pos, 32);
        world.players().stream().filter((dimension)).forEach((player) -> ServerPlayNetworking.send(player, ID, packet));
    }

    @Environment(EnvType.CLIENT)
    public static void initReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, listener, packet, sender) -> {
            GlobalPos pos = packet.readGlobalPos();
            client.execute(() -> {
                playLocalSound(client, pos);
            });
        });
    }

    @Environment(EnvType.CLIENT)
    public static void playLocalSound(Minecraft client, GlobalPos global) {
        BlockPos nebulite = global.pos();
        ResourceKey<Level> dimension = global.dimension();

        ClientLevel world = client.level;
        Entity entity = client.cameraEntity;

        if (world != null && world.dimension() == dimension && entity != null && entity instanceof LivingEntity mob) {
            float volume = MathUtil.nextFloat(world.getRandom(), 0.9F, 1.1F);
            float pitch = MathUtil.nextFloat(world.getRandom(), 0.9F, 1.1F);
            world.playLocalSound(nebulite.getX(), nebulite.getY(), nebulite.getZ(), getAmbientSound(world, mob.blockPosition(), nebulite), SoundSource.BLOCKS, volume, pitch, false);
        }
    }

    @Environment(EnvType.CLIENT)
    public static SoundEvent getAmbientSound(Level world, BlockPos camera, BlockPos nebulite) {
        if (isBlockObstructed(world, nebulite)) {
            return EnderscapeSounds.NEBULITE_ORE_AMBIENT_OBSTRUCTED;
        } else {
            if (camera.closerThan(nebulite, 16)) {
                return EnderscapeSounds.NEBULITE_ORE_AMBIENT;
            } else {
                return EnderscapeSounds.NEBULITE_ORE_AMBIENT_FAR;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static boolean isBlockObstructed(Level world, BlockPos pos) {
        int i = 0;
        for (var dir : Direction.values()) {
            var pos2 = pos.relative(dir);
            if (world.getBlockState(pos2).canOcclude()) {
                i++;
                if (i == 6) return true;
            }
        }
        return false;
    }
}