package net.penumbra.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.item.ItemStackContext;
import net.penumbra.enderscape.registry.enchantment.EnderscapeEnchantmentEffectComponents;
import net.penumbra.enderscape.registry.entity.EnderscapeAttachments;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.server.EnderscapeCriteria;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.ArrayList;
import java.util.List;

import static net.penumbra.enderscape.registry.component.EnderscapeDataComponents.STUN_ATTACK;

public record StunAttack(
        Holder<SoundEvent> stunSound,
        Holder<SoundEvent> missSound,
        MobEffectInstance effect,
        ParticleOptions particle,
        float cooldownDuration,
        float shieldDisableDuration
) {

    private static final Holder<SoundEvent> DEFAULT_STUN_SOUND = EnderscapeItemSounds.DAGGER_STUN;
    private static final Holder<SoundEvent> DEFAULT_MISS_SOUND = EnderscapeItemSounds.DAGGER_STUN_MISS;
    private static final MobEffectInstance DEFAULT_EFFECT = new MobEffectInstance(EnderscapeMobEffects.STUNNED, 3 * 20);
    private static final ParticleOptions DEFAULT_PARTICLE = EnderscapeParticles.VOID_POOF;
    private static final float DEFAULT_COOLDOWN_DURATION = 7.0F;
    private static final float DEFAULT_SHIELD_DISABLE_DURATION = 1.5F;

    public static final Codec<StunAttack> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    SoundEvent.CODEC.optionalFieldOf("stun_sound", DEFAULT_STUN_SOUND).forGetter(StunAttack::stunSound),
                    SoundEvent.CODEC.optionalFieldOf("miss_sound", DEFAULT_MISS_SOUND).forGetter(StunAttack::missSound),
                    MobEffectInstance.CODEC.optionalFieldOf("effect", DEFAULT_EFFECT).forGetter(StunAttack::effect),
                    ParticleTypes.CODEC.optionalFieldOf("particle", DEFAULT_PARTICLE).forGetter(StunAttack::particle),
                    Codec.FLOAT.optionalFieldOf("cooldown_duration", DEFAULT_COOLDOWN_DURATION).forGetter(StunAttack::cooldownDuration),
                    Codec.FLOAT.optionalFieldOf("shield_disable_duration", DEFAULT_SHIELD_DISABLE_DURATION).forGetter(StunAttack::shieldDisableDuration)
            ).apply(instance, StunAttack::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, StunAttack> STREAM_CODEC = StreamCodec.composite(
            SoundEvent.STREAM_CODEC, StunAttack::stunSound,
            SoundEvent.STREAM_CODEC, StunAttack::missSound,
            MobEffectInstance.STREAM_CODEC, StunAttack::effect,
            ParticleTypes.STREAM_CODEC, StunAttack::particle,
            ByteBufCodecs.FLOAT, StunAttack::cooldownDuration,
            ByteBufCodecs.FLOAT, StunAttack::shieldDisableDuration,
            StunAttack::new
    );

    public static final StunAttack DEFAULT = new StunAttack(
            DEFAULT_STUN_SOUND,
            DEFAULT_MISS_SOUND,
            DEFAULT_EFFECT,
            DEFAULT_PARTICLE,
            DEFAULT_COOLDOWN_DURATION,
            DEFAULT_SHIELD_DISABLE_DURATION
    );

    public static boolean is(ItemStack stack) {
        return stack.has(STUN_ATTACK);
    }

    public static StunAttack get(ItemStack stack) {
        return stack.get(STUN_ATTACK);
    }

    public static boolean isMeleeStun(ItemStack stack) {
        return areaOfEffectStunRadius(stack).equals(Vec2.ZERO);
    }

    public static InteractionResult miss(ServerLevel level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        StunAttack attack = StunAttack.get(stack);
        Vec2 radius = areaOfEffectStunRadius(stack);

        if (canUseStun(level, stack, player) && radius != Vec2.ZERO) {
            ItemStackContext context = new ItemStackContext(stack, level, player);
            List<Entity> affectedEntities = new ArrayList<>();

            doAreaOfEffectStun(context, attack, radius, affectedEntities);
            finishUsingStun(context, attack, affectedEntities);
        } else {
            player.getCooldowns().addCooldown(stack, cooldownDurationScaled(stack, player, attack) / 4);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), attack.missSound(), player.getSoundSource(), 1.0F, 1.0F);
        }

        return InteractionResult.SUCCESS_SERVER;
    }

    public static boolean apply(ServerLevel level, ItemStack stack, Player player, LivingEntity victim) {
        ItemStackContext context = new ItemStackContext(stack, level, player);

        if (canUseStun(level, stack, player) && isMeleeStun(stack) && canStunVictim(player, victim)) {
            StunAttack attack = StunAttack.get(stack);

            doMeleeStun(context, victim);
            finishUsingStun(context, attack, victim.hasEffect(attack.effect().getEffect()) ? List.of(victim) : List.of());

            return true;
        }

        return false;
    }

    private static void doMeleeStun(ItemStackContext context, LivingEntity victim) {
        LivingEntity attacker = context.user();
        context.serverLevel().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), getStunSound(context.stack()), attacker.getSoundSource(), 1.0F, 1.0F);

        if (attacker instanceof Player player) {
            try {
                player.setAttached(EnderscapeAttachments.PERFORMING_STUN_ATTACK, Unit.INSTANCE);
                player.attack(victim);
            } finally {
                player.removeAttached(EnderscapeAttachments.PERFORMING_STUN_ATTACK);
            }
        }
    }

    public static void applyStun(ServerLevel level, LivingEntity victim, DamageSource source) {
        if (source.getEntity() instanceof LivingEntity attacker) {
            StunWeaponInfo info = getWeaponInfo(attacker);
            ItemStack stack = info.stack();

            if (StunAttack.is(stack)) {
                stun(new ItemStackContext(stack, level, attacker), victim, get(stack), meleeStunDurationMultiplier(victim, info.mainHand()));
            }
        }
    }

    public static StunWeaponInfo getWeaponInfo(LivingEntity attacker) {
        boolean mainHand = StunAttack.is(attacker.getMainHandItem());

        return new StunWeaponInfo(
                mainHand ? attacker.getMainHandItem() : attacker.getOffhandItem(),
                mainHand
        );
    }

    private static double meleeStunDurationMultiplier(LivingEntity victim, boolean mainHand) {
        return (victim instanceof Player ? 0.75 : 1.0) * (mainHand ? 1.0 : 0.5);
    }

    private static void doAreaOfEffectStun(ItemStackContext context, StunAttack attack, Vec2 radius, List<Entity> affectedEntities) {
        LivingEntity attacker = context.user();
        ServerLevel level = context.serverLevel();
        AABB stunArea = getStunArea(attacker.position(), radius);

        level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), getStunSound(context.stack()), attacker.getSoundSource(), 1.0F, 1.0F);

        level.getEntitiesOfClass(LivingEntity.class, stunArea, (mob) -> canStunVictim(attacker, mob)).forEach(other -> {
            double distance = getDistanceInEllipsoid(other.position(), stunArea.getCenter(), radius);

            if (distance <= 1.0F) {
                double durationMultiplier = 1.0 - Math.min(distance, 1.0);

                if (durationMultiplier > 0.0F && stun(context, other, attack, durationMultiplier)) {
                    affectedEntities.add(other);
                }
            }
        });

        Iterable<BlockPos> randomPositions = BlockPos.randomBetweenClosed(
                level.getRandom(),
                (int) (radius.lengthSquared()),
                Mth.floor(stunArea.minX),
                Mth.floor(stunArea.minY),
                Mth.floor(stunArea.minZ),
                Mth.floor(stunArea.maxX),
                Mth.floor(stunArea.maxY),
                Mth.floor(stunArea.maxZ)
        );

        randomPositions.forEach(pos -> {
            if (getDistanceInEllipsoid(pos.getCenter(), stunArea.getCenter(), radius) <= 1.0F) {
                level.sendParticles(attack.particle(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2, 1, 1, 1, 0.1);
            }
        });
    }

    private static boolean stun(ItemStackContext context, LivingEntity victim, StunAttack attack, double durationMultiplier) {
        MobEffectInstance effect = attack.effect();

        if (effect == null) {
            return false;
        } else {
            Vec3 position = victim.position();
            context.serverLevel().sendParticles(attack.particle(), position.x, position.y + 0.5, position.z, 15, 1, 1, 1, 0.1);

            victim.addEffect(
                    new MobEffectInstance(
                            effect.getEffect(),
                            (int) (effectDuration(context.stack(), context.user(), attack) * durationMultiplier),
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()
                    ),
                    context.user()
            );

            return true;
        }
    }

    private static void finishUsingStun(ItemStackContext context, StunAttack attack, List<Entity> affectedEntities) {
        if (context.user() instanceof Player player) {
            ItemStack stack = context.stack();

            if (player instanceof ServerPlayer server) {
                EnderscapeCriteria.STUN_ATTACK.trigger(server, stack, affectedEntities);
            }

            FueledTool.useFuelOrDamage(context, 10, player.getEquipmentSlotForItem(stack));

            player.getCooldowns().addCooldown(stack, cooldownDurationScaled(stack, player, attack));
            stack.finishUsingItem(context.level(), player);
        }
    }

    private static double getDistanceInEllipsoid(Vec3 position, Vec3 center, Vec2 radius) {
        double dx = position.x - center.x, dy = position.y - center.y, dz = position.z - center.z;
        return (dx * dx) / (radius.x * radius.x) + (dy * dy) / (radius.y * radius.y) + (dz * dz) / (radius.x * radius.x);
    }

    private static AABB getStunArea(Vec3 origin, Vec2 radius) {
        return new AABB(origin.subtract(0.1), origin.add(0.1)).inflate(radius.x, radius.y, radius.x);
    }

    private static int cooldownDurationScaled(ItemStack stack, LivingEntity user, StunAttack attack) {
        MutableFloat mutable = new MutableFloat(attack.cooldownDuration());
        EnchantmentHelper.runIterationOnItem(stack, (holder, level) -> holder.value().modifyUnfilteredValue(EnderscapeEnchantmentEffectComponents.STUN_ATTACK_COOLDOWN_TIME, user.getRandom(), level, mutable));
        return (int) (Math.max(0.0F, mutable.floatValue()) * 20);
    }

    private static Vec2 areaOfEffectStunRadius(final ItemStack stack) {
        return EnchantmentHelper.pickHighestLevel(stack, EnderscapeEnchantmentEffectComponents.AREA_OF_EFFECT_STUN_ATTACK_RADIUS).orElse(Vec2.ZERO);
    }

    private static float effectDuration(ItemStack stack, LivingEntity user, StunAttack attack) {
        MutableFloat mutable = new MutableFloat(attack.effect.getDuration());
        EnchantmentHelper.runIterationOnItem(stack, (holder, level) -> holder.value().modifyUnfilteredValue(EnderscapeEnchantmentEffectComponents.STUN_ATTACK_EFFECT_DURATION, user.getRandom(), level, mutable));
        return Math.max(0.0F, mutable.floatValue());
    }

    private static Holder<SoundEvent> getStunSound(final ItemStack stack) {
        return EnchantmentHelper.pickHighestLevel(stack, EnderscapeEnchantmentEffectComponents.STUN_ATTACK_SOUND).orElse(DEFAULT_STUN_SOUND);
    }

    public static boolean canStunVictim(LivingEntity attacker, LivingEntity victim) {
        return attacker != victim && victim.canBeSeenAsEnemy() && !attacker.isAlliedTo(victim);
    }

    public static boolean canUseStun(Level level, ItemStack stack, Player player) {
        return !player.getCooldowns().isOnCooldown(stack) && FueledTool.fuelExceedsCost(new ItemStackContext(stack, level, player));
    }

    public record StunWeaponInfo(ItemStack stack, boolean mainHand) {}
}
