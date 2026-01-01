package net.bunten.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.entity.DashJumpUser;
import net.bunten.enderscape.item.RubbleShieldItem;
import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.item.component.value.ContextualValue;
import net.bunten.enderscape.network.ClientboundDashJumpPayload;
import net.bunten.enderscape.network.ClientboundDashJumpSoundPayload;
import net.bunten.enderscape.registry.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeGameEvents;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.DASH_JUMP;

public record DashJump(
        DashPower power,
        ContextualValue durabilityDamage,
        ContextualValue foodExhaustion,
        Holder<SoundEvent> sound,
        float particleDuration,
        boolean stopUsingAfterwards
) {

    public static final DashPower DEFAULT_DASH_POWER = new DashPower(
            new Vec2(2.35F, 0.35F),
            0.7F
    );

    public static final ContextualValue DEFAULT_DURABILITY_DAMAGE = new ContextualValue(5.0F, 2.0F);
    public static final ContextualValue DEFAULT_FOOD_EXHAUSTION = new ContextualValue(4.0F, 4.0F);
    public static final Holder.Reference<SoundEvent> DEFAULT_DASH_SOUND = EnderscapeItemSounds.RUBBLE_SHIELD_DASH;

    public static final float DEFAULT_PARTICLE_DURATION = 3.0F;
    public static final boolean DEFAULT_STOP_USING_AFTERWARDS = true;

    public static final Codec<DashJump> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    DashPower.CODEC.optionalFieldOf("power", DEFAULT_DASH_POWER).forGetter(DashJump::power),
                    ContextualValue.CODEC.optionalFieldOf("durability_damage", DEFAULT_DURABILITY_DAMAGE).forGetter(DashJump::durabilityDamage),
                    ContextualValue.CODEC.optionalFieldOf("food_exhaustion", DEFAULT_DURABILITY_DAMAGE).forGetter(DashJump::foodExhaustion),
                    SoundEvent.CODEC.optionalFieldOf("sound", DEFAULT_DASH_SOUND).forGetter(DashJump::sound),
                    ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("particle_duration", DEFAULT_PARTICLE_DURATION).forGetter(DashJump::particleDuration),
                    Codec.BOOL.optionalFieldOf("stop_using_afterwards", DEFAULT_STOP_USING_AFTERWARDS).forGetter(DashJump::stopUsingAfterwards)
            ).apply(instance, DashJump::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DashJump> STREAM_CODEC = StreamCodec.composite(
            DashPower.STREAM_CODEC,
            DashJump::power,
            ContextualValue.STREAM_CODEC,
            DashJump::durabilityDamage,
            ContextualValue.STREAM_CODEC,
            DashJump::foodExhaustion,
            SoundEvent.STREAM_CODEC,
            DashJump::sound,
            ByteBufCodecs.FLOAT,
            DashJump::particleDuration,
            ByteBufCodecs.BOOL,
            DashJump::stopUsingAfterwards,
            DashJump::new
    );

    public static final DashJump DEFAULT = new DashJump(
            DEFAULT_DASH_POWER,
            DEFAULT_DURABILITY_DAMAGE,
            DEFAULT_FOOD_EXHAUSTION,
            DEFAULT_DASH_SOUND,
            DEFAULT_PARTICLE_DURATION,
            DEFAULT_STOP_USING_AFTERWARDS
    );

    public static boolean apply(ServerLevel level, ServerPlayer player, ItemStack stack) {
        DashJump dash = stack.get(DASH_JUMP);

        if (player.onGround() && !player.isInLiquid() && (player.getAbilities().instabuild || player.getFoodData().getFoodLevel() > 6.0F) && (!FueledTool.is(stack) || FueledTool.fuelExceedsCost(new ItemStackContext(stack, level, player)))) {
            Vec3 pos = player.position();
            level.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 0.5, pos.z, 5, 0, 0, 0, 0.1);

            stack.hurtAndBreak(dash.durabilityDamage().calculate(player).asInteger(), player, ServerPlayer.getSlotForHand(player.getUsedItemHand()));
            player.causeFoodExhaustion(dash.foodExhaustion().calculate(player).asFloat());

            level.players().stream().filter(other -> other.distanceToSqr(pos) < 4096).forEach(other -> ServerPlayNetworking.send(other, new ClientboundDashJumpSoundPayload(player.getId(), dash.sound().value().getLocation())));

            DashJumpUser.setDashed(player, true);
            DashJumpUser.setDashTicks(player, (int) (dash.particleDuration() * 20));

            if (dash.stopUsingAfterwards()) {
                player.stopUsingItem();
                stack.finishUsingItem(player.level(), player);
            }
            
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            player.gameEvent(EnderscapeGameEvents.DASH_JUMP);

            HolderLookup.RegistryLookup<Item> shields = level.registryAccess().lookupOrThrow(Registries.ITEM).filterElements(item -> item instanceof RubbleShieldItem);
            shields.listElements().forEach(reference -> player.getCooldowns().addCooldown(reference.value(), 60));

            EnderscapeCriteria.DASH_JUMP.trigger(player, stack);
            ServerPlayNetworking.send(player, new ClientboundDashJumpPayload(dash.power().calculate(player)));

            stack.hurtAndBreak(dash.durabilityDamage().calculate(player).asInteger(), player, ServerPlayer.getSlotForHand(player.getUsedItemHand()));
            player.causeFoodExhaustion(dash.foodExhaustion().calculate(player).asFloat());
            
            return true;
        }

        return false;
    }
}