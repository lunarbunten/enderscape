package net.bunten.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.ATTACK_SOUNDS;

public record AttackSounds(
        Holder<SoundEvent> noDamage,
        Holder<SoundEvent> weak,
        Holder<SoundEvent> strong,
        Holder<SoundEvent> crit,
        Holder<SoundEvent> knockback,
        Holder<SoundEvent> sweep,
        Holder<SoundEvent> backstab
) {

    public static final Holder<SoundEvent> DEFAULT_NO_DAMAGE = Holder.direct(SoundEvents.PLAYER_ATTACK_NODAMAGE);
    public static final Holder<SoundEvent> DEFAULT_WEAK = Holder.direct(SoundEvents.PLAYER_ATTACK_WEAK);
    public static final Holder<SoundEvent> DEFAULT_STRONG = Holder.direct(SoundEvents.PLAYER_ATTACK_STRONG);
    public static final Holder<SoundEvent> DEFAULT_CRIT = Holder.direct(SoundEvents.PLAYER_ATTACK_CRIT);
    public static final Holder<SoundEvent> DEFAULT_KNOCKBACK = Holder.direct(SoundEvents.PLAYER_ATTACK_KNOCKBACK);
    public static final Holder<SoundEvent> DEFAULT_SWEEP = Holder.direct(SoundEvents.PLAYER_ATTACK_SWEEP);
    public static final Holder<SoundEvent> DEFAULT_BACKSTAB = Holder.direct(SoundEvents.EMPTY);

    public static final Codec<AttackSounds> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    SoundEvent.CODEC.optionalFieldOf("no_damage", DEFAULT_NO_DAMAGE).forGetter(AttackSounds::noDamage),
                    SoundEvent.CODEC.optionalFieldOf("weak", DEFAULT_WEAK).forGetter(AttackSounds::weak),
                    SoundEvent.CODEC.optionalFieldOf("strong", DEFAULT_STRONG).forGetter(AttackSounds::strong),
                    SoundEvent.CODEC.optionalFieldOf("crit", DEFAULT_CRIT).forGetter(AttackSounds::crit),
                    SoundEvent.CODEC.optionalFieldOf("knockback", DEFAULT_KNOCKBACK).forGetter(AttackSounds::knockback),
                    SoundEvent.CODEC.optionalFieldOf("sweep", DEFAULT_SWEEP).forGetter(AttackSounds::sweep),
                    SoundEvent.CODEC.optionalFieldOf("backstab", DEFAULT_BACKSTAB).forGetter(AttackSounds::backstab)
            ).apply(instance, AttackSounds::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AttackSounds> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public AttackSounds decode(RegistryFriendlyByteBuf buf) {
            Holder<SoundEvent> noDamage = SoundEvent.STREAM_CODEC.decode(buf);
            Holder<SoundEvent> weak = SoundEvent.STREAM_CODEC.decode(buf);
            Holder<SoundEvent> strong = SoundEvent.STREAM_CODEC.decode(buf);
            Holder<SoundEvent> crit = SoundEvent.STREAM_CODEC.decode(buf);
            Holder<SoundEvent> knockback = SoundEvent.STREAM_CODEC.decode(buf);
            Holder<SoundEvent> sweep = SoundEvent.STREAM_CODEC.decode(buf);
            Holder<SoundEvent> backstab = SoundEvent.STREAM_CODEC.decode(buf);

            return new AttackSounds(noDamage, weak, strong, crit, knockback, sweep, backstab);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, AttackSounds value) {
            SoundEvent.STREAM_CODEC.encode(buf, value.noDamage());
            SoundEvent.STREAM_CODEC.encode(buf, value.weak());
            SoundEvent.STREAM_CODEC.encode(buf, value.strong());
            SoundEvent.STREAM_CODEC.encode(buf, value.crit());
            SoundEvent.STREAM_CODEC.encode(buf, value.knockback());
            SoundEvent.STREAM_CODEC.encode(buf, value.sweep());
            SoundEvent.STREAM_CODEC.encode(buf, value.backstab());
        }
    };

    public static boolean is(ItemStack stack) {
        return stack.has(ATTACK_SOUNDS);
    }

    public static AttackSounds get(ItemStack stack) {
        return stack.get(ATTACK_SOUNDS);
    }

    public static class Builder {
        private Holder<SoundEvent> noDamage = DEFAULT_NO_DAMAGE;
        private Holder<SoundEvent> weak = DEFAULT_WEAK;
        private Holder<SoundEvent> strong = DEFAULT_STRONG;
        private Holder<SoundEvent> crit = DEFAULT_CRIT;
        private Holder<SoundEvent> knockback = DEFAULT_KNOCKBACK;
        private Holder<SoundEvent> sweep = DEFAULT_SWEEP;
        private Holder<SoundEvent> backstab = DEFAULT_BACKSTAB;

        public static Builder create() {
            return new Builder();
        }

        public Builder noDamage(Holder<SoundEvent> sound) {
            this.noDamage = sound;
            return this;
        }

        public Builder weak(Holder<SoundEvent> sound) {
            this.weak = sound;
            return this;
        }

        public Builder strong(Holder<SoundEvent> sound) {
            this.strong = sound;
            return this;
        }

        public Builder crit(Holder<SoundEvent> sound) {
            this.crit = sound;
            return this;
        }

        public Builder knockback(Holder<SoundEvent> sound) {
            this.knockback = sound;
            return this;
        }

        public Builder sweep(Holder<SoundEvent> sound) {
            this.sweep = sound;
            return this;
        }

        public Builder backstab(Holder<SoundEvent> sound) {
            this.backstab = sound;
            return this;
        }

        public AttackSounds build() {
            return new AttackSounds(noDamage, weak, strong, crit, knockback, sweep, backstab);
        }
    }
}