package net.bunten.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.entity.DashJumpUser;
import net.bunten.enderscape.network.ClientboundDashJumpPayload;
import net.bunten.enderscape.network.ClientboundDashJumpSoundPayload;
import net.bunten.enderscape.registry.EnderscapeGameEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public record DashJump(int dashTime, float horizontalPower, float verticalPower, float glideVelocityFactor, Holder<SoundEvent> dashSound, boolean stopUsingAfterDash) {

    public static final Codec<DashJump> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("dash_time").forGetter(DashJump::dashTime),
                            ExtraCodecs.POSITIVE_FLOAT.fieldOf("horizontal_power").forGetter(DashJump::horizontalPower),
                            ExtraCodecs.POSITIVE_FLOAT.fieldOf("vertical_power").forGetter(DashJump::verticalPower),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("glide_velocity_factor").forGetter(DashJump::glideVelocityFactor),
                            SoundEvent.CODEC.fieldOf("dash_sound").forGetter(DashJump::dashSound),
                            Codec.BOOL.optionalFieldOf("stop_using_after_dash", true).forGetter(DashJump::stopUsingAfterDash)
                    ).apply(instance, DashJump::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DashJump> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            DashJump::dashTime,
            ByteBufCodecs.FLOAT,
            DashJump::horizontalPower,
            ByteBufCodecs.FLOAT,
            DashJump::verticalPower,
            ByteBufCodecs.FLOAT,
            DashJump::glideVelocityFactor,
            SoundEvent.STREAM_CODEC,
            DashJump::dashSound,
            ByteBufCodecs.BOOL,
            DashJump::stopUsingAfterDash,
            DashJump::new
    );

    public boolean apply(ServerLevel level, ServerPlayer player, ItemStack stack, DashJump jump) {
        if (player.onGround() && !player.isInLiquid()) {
            Vec3 pos = player.position();
            level.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 0.5, pos.z, 5, 0, 0, 0, 0.1);
            stack.hurtAndBreak(player.isFallFlying() ? 9 : 3, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));

            for (ServerPlayer other : level.players()) {
                if (other.distanceToSqr(pos) < 4096) ServerPlayNetworking.send(other, new ClientboundDashJumpSoundPayload(player.getId(), jump.dashSound.value().location()));
            }

            DashJumpUser.setDashed(player, true);
            DashJumpUser.setDashTicks(player, jump.dashTime());

            if (jump.stopUsingAfterDash()) {
                player.stopUsingItem();
                stack.finishUsingItem(player.level(), player);
            }

            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            player.gameEvent(EnderscapeGameEvents.DASH_JUMP);

            ServerPlayNetworking.send(player, new ClientboundDashJumpPayload(horizontalPower, verticalPower, glideVelocityFactor));

            return true;
        }

        return false;
    }
}