package net.penumbra.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.item.ItemStackContext;
import net.penumbra.enderscape.item.component.value.DashCharge;
import net.penumbra.enderscape.item.component.value.DashPower;
import net.penumbra.enderscape.item.component.value.GlidingBasedValue;
import net.penumbra.enderscape.manager.DashJumpManager;
import net.penumbra.enderscape.network.ClientboundDashJumpPayload;
import net.penumbra.enderscape.network.ClientboundDashJumpSoundPayload;
import net.penumbra.enderscape.registry.level.EnderscapeGameEvents;
import net.penumbra.enderscape.registry.server.EnderscapeCriteria;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;

import static net.penumbra.enderscape.registry.component.EnderscapeDataComponents.DASH_JUMP;
import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.DASH_JUMP_TICKS;

public record DashJump(
        DashPower power,
        DashCharge charge,
        GlidingBasedValue durabilityDamage,
        GlidingBasedValue foodExhaustion,
        Holder<SoundEvent> sound,
        float dashDuration,
        boolean stopUsingAfterwards
) {

    public static final DashPower DEFAULT_DASH_POWER = new DashPower(
            new Vec2(2.35F, 0.35F),
            0.7F
    );

    public static final DashCharge DEFAULT_DASH_CHARGE = new DashCharge(
            new GlidingBasedValue(1.0F, 2.0F),
            true
    );

    private static final GlidingBasedValue DEFAULT_DURABILITY_DAMAGE = new GlidingBasedValue(5.0F, 2.0F);
    private static final GlidingBasedValue DEFAULT_FOOD_EXHAUSTION = new GlidingBasedValue(4.0F, 4.0F);

    private static final Holder.Reference<SoundEvent> DEFAULT_DASH_SOUND = EnderscapeItemSounds.RUBBLE_SHIELD_DASH;

    private static final float DEFAULT_DASH_DURATION = 3.0F;
    private static final boolean DEFAULT_STOP_USING_AFTERWARDS = true;

    public static final Codec<DashJump> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    DashPower.CODEC.optionalFieldOf("power", DEFAULT_DASH_POWER).forGetter(DashJump::power),
                    DashCharge.CODEC.optionalFieldOf("charge", DEFAULT_DASH_CHARGE).forGetter(DashJump::charge),
                    GlidingBasedValue.CODEC.optionalFieldOf("durability_damage", DEFAULT_DURABILITY_DAMAGE).forGetter(DashJump::durabilityDamage),
                    GlidingBasedValue.CODEC.optionalFieldOf("food_exhaustion", DEFAULT_DURABILITY_DAMAGE).forGetter(DashJump::foodExhaustion),
                    SoundEvent.CODEC.optionalFieldOf("sound", DEFAULT_DASH_SOUND).forGetter(DashJump::sound),
                    ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("dash_duration", DEFAULT_DASH_DURATION).forGetter(DashJump::dashDuration),
                    Codec.BOOL.optionalFieldOf("stop_using_afterwards", DEFAULT_STOP_USING_AFTERWARDS).forGetter(DashJump::stopUsingAfterwards)
            ).apply(instance, DashJump::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DashJump> STREAM_CODEC = StreamCodec.composite(
            DashPower.STREAM_CODEC,
            DashJump::power,
            DashCharge.STREAM_CODEC,
            DashJump::charge,
            GlidingBasedValue.STREAM_CODEC,
            DashJump::durabilityDamage,
            GlidingBasedValue.STREAM_CODEC,
            DashJump::foodExhaustion,
            SoundEvent.STREAM_CODEC,
            DashJump::sound,
            ByteBufCodecs.FLOAT,
            DashJump::dashDuration,
            ByteBufCodecs.BOOL,
            DashJump::stopUsingAfterwards,
            DashJump::new
    );

    public static final DashJump DEFAULT = new DashJump(
            DEFAULT_DASH_POWER,
            DEFAULT_DASH_CHARGE,
            DEFAULT_DURABILITY_DAMAGE,
            DEFAULT_FOOD_EXHAUSTION,
            DEFAULT_DASH_SOUND,
            DEFAULT_DASH_DURATION,
            DEFAULT_STOP_USING_AFTERWARDS
    );

    public static boolean apply(ServerLevel level, ServerPlayer player, ItemStack stack) {
        ItemStackContext context = new ItemStackContext(stack, level, player);

        if (DashJumpManager.getChargeProgress(player) >= 0.875F && DashJumpManager.hasFoodOrIsCreative(player) && player.onGround() && !player.isInLiquid() && (!FueledTool.is(stack) || FueledTool.fuelExceedsCost(context))) {
            DashJump dashJump = stack.get(DASH_JUMP);
            Vec3 pos = player.position();

            level.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 0.5, pos.z, 5, 0, 0, 0, 0.1);
            level.players().stream().filter(other -> other.distanceToSqr(pos) < 4096).forEach(other -> ServerPlayNetworking.send(other, new ClientboundDashJumpSoundPayload(player.getId(), dashJump.sound().value().location())));

            player.setAttached(DASH_JUMP_TICKS, (int) (dashJump.dashDuration() * 20));

            if (dashJump.stopUsingAfterwards()) {
                player.stopUsingItem();
                stack.finishUsingItem(player.level(), player);
            }

            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            player.gameEvent(EnderscapeGameEvents.DASH_JUMP);

            EnderscapeCriteria.DASH_JUMP.trigger(player, stack);
            ServerPlayNetworking.send(player, new ClientboundDashJumpPayload(dashJump.power().calculate(player)));
            FueledTool.useFuelOrDamage(context, dashJump.durabilityDamage().calculate(player).asInteger(), player.getUsedItemHand().asEquipmentSlot());

            player.causeFoodExhaustion(dashJump.foodExhaustion().calculate(player).asFloat());

            return true;
        }

        return false;
    }

}