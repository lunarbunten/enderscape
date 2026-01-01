package net.bunten.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.network.EnderscapeCodecs;
import net.bunten.enderscape.registry.*;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.BACKSTAB_ANGLE;
import static net.bunten.enderscape.registry.EnderscapeDataComponents.STUN_ATTACK;

public record StunAttack(
        float backstabEffectDurationMultiplier,
        Vec2 range,
        Holder<SoundEvent> normalSound,
        MobEffectInstance effect,
        ParticleOptions particle
) {

    public static final float DEFAULT_BACKSTAB_EFFECT_DURATION_MULTIPLIER = 4.0F;
    public static final Vec2 DEFAULT_RANGE = new Vec2(4.0F, 0.5F);
    public static final Holder<SoundEvent> DEFAULT_NORMAL_SOUND = EnderscapeItemSounds.DAGGER_STUN;
    public static final MobEffectInstance DEFAULT_EFFECT = new MobEffectInstance(EnderscapeMobEffects.STUNNED, 20);
    public static final ParticleOptions DEFAULT_PARTICLE = EnderscapeParticles.VOID_POOF;

//    public static final TargetingConditions VALID_FOR_AREA_OF_EFFECT = TargetingConditions.forCombat();

    public static final Codec<StunAttack> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("backstab_effect_duration_multiplier", DEFAULT_BACKSTAB_EFFECT_DURATION_MULTIPLIER).forGetter(StunAttack::backstabEffectDurationMultiplier),
                    EnderscapeCodecs.VEC2.optionalFieldOf("range", DEFAULT_RANGE).forGetter(StunAttack::range),
                    SoundEvent.CODEC.optionalFieldOf("normal_sound", DEFAULT_NORMAL_SOUND).forGetter(StunAttack::normalSound),
                    MobEffectInstance.CODEC.optionalFieldOf("effect", DEFAULT_EFFECT).forGetter(StunAttack::effect),
                    ParticleTypes.CODEC.optionalFieldOf("particle", DEFAULT_PARTICLE).forGetter(StunAttack::particle)
            ).apply(instance, StunAttack::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, StunAttack> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, StunAttack::backstabEffectDurationMultiplier,
            EnderscapeCodecs.VEC2_STREAM, StunAttack::range,
            SoundEvent.STREAM_CODEC, StunAttack::normalSound,
            MobEffectInstance.STREAM_CODEC, StunAttack::effect,
            ParticleTypes.STREAM_CODEC, StunAttack::particle,
            StunAttack::new
    );

    public static final StunAttack DEFAULT = new StunAttack(
            DEFAULT_BACKSTAB_EFFECT_DURATION_MULTIPLIER,
            DEFAULT_RANGE,
            DEFAULT_NORMAL_SOUND,
            DEFAULT_EFFECT,
            DEFAULT_PARTICLE
    );

    public static boolean is(ItemStack stack) {
        return stack.has(STUN_ATTACK);
    }

    public static StunAttack get(ItemStack stack) {
        return stack.get(STUN_ATTACK);
    }

    public static boolean apply(ServerLevel level, LivingEntity attacker, LivingEntity victim, ItemStack stack) {
        ItemStackContext context = new ItemStackContext(stack, level, attacker);
        StunAttack attack = StunAttack.get(stack);

        if (canStun(attacker, context)) {
            boolean backstab = stack.has(BACKSTAB_ANGLE) && EnderscapeAttributes.isBackstab(stack, context.user().position(), victim);

            List<Entity> affectedEntities = List.of(victim);

            doSingularStun(context, victim, attack, backstab);

            if (attacker instanceof ServerPlayer player) {
                EnderscapeCriteria.STUN_ATTACK.trigger(player, stack, victim, affectedEntities, backstab);
            }

            stack.hurtAndBreak(10, attacker, attacker.getEquipmentSlotForItem(stack));
            stack.finishUsingItem(level, attacker);
            if (attacker instanceof Player player) player.getCooldowns().addCooldown(stack.getItem(), 7 * 20);

            return true;
        }

        return false;
    }

    private static boolean canStun(LivingEntity attacker, ItemStackContext context) {
        ItemStack stack = context.stack();
        boolean notOnCooldown = !(attacker instanceof Player) || !((Player) attacker).getCooldowns().isOnCooldown(stack.getItem());
        boolean hasFuel = !FueledTool.is(stack) || FueledTool.fuelExceedsCost(context);
        return notOnCooldown && hasFuel;
    }

    private static void doSingularStun(ItemStackContext context, LivingEntity victim, StunAttack attack, boolean backstab) {
        LivingEntity attacker = context.user();

        context.serverLevel().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), attack.normalSound(), attacker.getSoundSource(), 1.0F, 1.0F);
        stun(context, victim, attack, backstab);
    }

//    private static void doAreaOfEffectStun(ItemStackContext context, LivingEntity victim, StunAttack attack, boolean backstab, List<Entity> affectedEntities) {
//        LivingEntity attacker = context.user();
//        ServerLevel level = context.serverLevel();
//        Vec3 pos = victim.position();
//
//        level.getEntitiesOfClass(LivingEntity.class, getStunArea(attack, pos), (mob) -> VALID_FOR_AREA_OF_EFFECT.test(attacker, mob)).forEach(other -> {
//            affectedEntities.add(other);
//            stun(context, other, attack, backstab);
//        });
//
//        level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), attack.criticalSound(), attacker.getSoundSource(), 1.0F, 1.0F);
//    }

//    private static AABB getStunArea(StunAttack attack, Vec3 pos) {
//        return new AABB(pos.subtract(0.1, 0.1, 0.1), pos.add(0.1, 0.1, 0.1)).inflate(attack.range().x, attack.range().y, attack.range().x);
//    }

    private static void stun(ItemStackContext context, LivingEntity victim, StunAttack attack, boolean backstab) {
        Vec3 pos = victim.position();
        MobEffectInstance effect = attack.effect();

        if (effect != null) {
            float multiplier = backstab ? attack.backstabEffectDurationMultiplier() : 1;
            int duration = (int) (effect.getDuration() * multiplier);

            victim.addEffect(new MobEffectInstance(effect.getEffect(), duration, effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon()), context.user());
        }

        context.serverLevel().sendParticles(attack.particle(), pos.x, pos.y + 0.5, pos.z, 15, 1, 1, 1, 0.1);
    }
}