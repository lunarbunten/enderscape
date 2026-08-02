package net.penumbra.enderscape.manager;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.entity.EnderscapeDamageTypes;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.level.EnderscapeEnvironmentAttributes;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.server.EnderscapeGameRules;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;
import net.penumbra.enderscape.registry.tag.EnderscapeDamageTypeTags;
import net.penumbra.enderscape.registry.tag.EnderscapeFluidTags;
import net.penumbra.enderscape.registry.tag.EnderscapeItemTags;
import net.penumbra.enderscape.registry.tag.EnderscapeMobEffectTags;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.*;
import static net.penumbra.enderscape.registry.tag.EnderscapeEntityTags.*;

public class VoidManager {

    public static final int MAXIMUM_VOID_TICKS = 90;
    public static final float OUTER_VOID_MAXIMUM_MODIFIER = 2.5F;
    public static final int DEFAULT_MAXIMUM_OUTER_VOID_TICKS = 20;

    private static final Identifier SPEED_MODIFIER_VOID_ID = Enderscape.id("void");

    public static final int TIME_UNTIL_VOID_DESTRUCTION = 60 * 20;
    public static final float ITEM_DESTRUCTION_CHANCE_EACH_TICK = 0.125F;

    public static void tickTail(Entity entity) {
        tickVoidedParticles(entity);

        if (entity.level() instanceof ServerLevel server) {
            if (entity instanceof LivingEntity living) {
                removeSpeedModifier(living);
                tryAddSpeedModifier(living);

                tickVoidDamage(living, server);
                tickFullVoidedHealth(living, server);

                applyAmbientVoidPurification(server, living);
            } else {
                tickVoidedEntityDestruction(entity, server);
            }

            tickVoidRelatedValues(entity);
            VoidLachrymaItemConversionManager.tickConversion(entity, server);
        }
    }

    private static void applyAmbientVoidPurification(ServerLevel server, LivingEntity entity) {
        if (server.getGameTime() % 80L == 0 && hasVoidedHealth(entity) && canPurifyVoidAt(server, BlockPos.containing(entity.getEyePosition()))) {
            entity.addEffect(new MobEffectInstance(EnderscapeMobEffects.VOID_PURIFICATION, 11 * 20, 0, true, false, true));
        }
    }

    private static boolean canPurifyVoidAt(ServerLevel level, BlockPos pos) {
        if (level.environmentAttributes().getValue(EnderscapeEnvironmentAttributes.PURIFIES_VOIDED_HEALTH, pos)) {
            return level.canSeeSky(pos) && !level.isRainingAt(pos);
        } else {
            return false;
        }
    }

    private static void tickVoidedParticles(Entity entity) {
        if (hasVoidTicks(entity) && !entity.isSpectator()) {
            RandomSource random = entity.getRandom();

            double size = entity.getBoundingBox().getSize() / 1.5;
            double chance = Math.min(1.0, getVoidTicksPercentage(entity) * size);

            if (random.nextFloat() < chance) {
                Vec3 pos = new Vec3(
                        entity.getRandomX(0.85),
                        entity.getRandomY() - 0.25,
                        entity.getRandomZ(0.85)
                );

                entity.level().addParticle(EnderscapeParticles.VOID_STARS,
                        pos.x(),
                        pos.y(),
                        pos.z(),
                        0.0,
                        Mth.nextDouble(random, 0.01, 0.03),
                        0.0
                );
            }
        }
    }

    protected static void removeSpeedModifier(LivingEntity entity) {
        AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            if (speed.getModifier(SPEED_MODIFIER_VOID_ID) != null) speed.removeModifier(SPEED_MODIFIER_VOID_ID);
        }
    }

    protected static void tryAddSpeedModifier(LivingEntity entity) {
        if (!entity.level().getBlockState(entity.getOnPosLegacy()).isAir()) {
            if (getVoidTicks(entity) > 0.0F) {
                AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                if (speed == null) return;

                float slowAmount = -0.05F * getVoidTicksPercentage(entity);
                speed.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_VOID_ID, slowAmount, AttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    private static void tickVoidDamage(LivingEntity entity, ServerLevel level) {
        if (!hasMaximumVoidTicks(entity)) return;

        VoidDamageType type = VoidDamageType.get(entity);
        if (entity.tickCount % type.interval(entity) == 0) {
            entity.hurtServer(level, level.damageSources().source(type.damageType()), 1.0F);
        }
    }

    private static void tickFullVoidedHealth(LivingEntity entity, ServerLevel level) {
        if (!entity.isDeadOrDying() && hasVoidedHealth(entity) && hasFullyVoidedHealth(entity)) {
            entity.hurtServer(level, level.damageSources().source(EnderscapeDamageTypes.VOID), entity.getMaxHealth());
        }
    }

    public static boolean standingOnVoidLachryma(Entity entity) {
        return entity.is(VOID_LACHRYMA_WALKABLE_MOBS) && entity.level().getFluidState(entity.blockPosition()).is(EnderscapeFluidTags.VOID_LACHRYMA);
    }

    private static void tickVoidedEntityDestruction(Entity entity, ServerLevel level) {
        if (shouldDestroyEntity(entity)) {
            Vec3 position = entity.position();
            RandomSource random = level.getRandom();

            if (random.nextFloat() <= 0.125F) {
                level.sendParticles(EnderscapeParticles.VOID_ENTITY_DESTRUCTION, position.x, position.y + 0.5, position.z, 1, 0, 0, 0, 0);

                if (random.nextFloat() <= 0.05F) {
                    entity.playSound(EnderscapeEntitySounds.VOID_CORRUPTION_AMBIENT, 1.0F, 1.0F);
                }
            }

            if (getVoidDestructionTicks(entity) >= TIME_UNTIL_VOID_DESTRUCTION) {
                tryDestroyEntity(entity, level, position);
            } else {
                modifyVoidDestructionTicks(entity, value -> value + 1);
            }
        } else {
            setVoidDestructionTicks(entity, 0);
        }
    }

    private static void tryDestroyEntity(Entity entity, ServerLevel level, Vec3 position) {
        if (entity.getRandom().nextFloat() <= ITEM_DESTRUCTION_CHANCE_EACH_TICK) {
            level.sendParticles(EnderscapeParticles.VOID_POOF, position.x, position.y + 0.5, position.z, 8, 0, 0, 0, 0);
            entity.playSound(EnderscapeEntitySounds.VOID_CORRUPTION_DESTRUCTION, 1.0F, 1.0F);
            entity.discard();
        }
    }

    private static boolean shouldDestroyEntity(Entity entity) {
        if (!hasMaximumVoidTicks(entity)) {
            return false;
        } else {
            if (entity instanceof ItemEntity item) {
                return !VoidLachrymaItemConversionManager.hasRecipe(item.getItem());
            } else {
                return true;
            }
        }
    }

    private static void tickVoidRelatedValues(Entity entity) {
        if (hasVoidImmunityTicks(entity)) {
            modifyVoidImmunityTicks(entity, value -> value - 1);
        }

        if (hasOuterVoidTickRemovalDelay(entity)) {
            modifyOuterVoidTickRemovalDelay(entity, value -> value - 1);
        } else if (hasOuterVoidTicks(entity)) {
            entity.removeAttached(OUTER_VOID_TICKS);
        }

        if (hasVoidTickDownDelay(entity)) {
            modifyVoidTickDownDelay(entity, value -> value - 1);
        } else if (hasVoidTicks(entity)) {
            modifyVoidTicks(entity, value -> value - 1);
        }

        tickAmbientVoiding(entity);
    }

    private static void tickAmbientVoiding(Entity entity) {
        if (canBeVoided(entity) && !isAmbientVoidingImmune(entity)) {
            float rate = getAmbientVoidingRate(entity);

            if (rate > 0.0F && !hasVoidTickDownDelay(entity)) {
                modifyVoidTicks(entity, value -> value + 1);
                modifyVoidTickDownDelay(entity, value -> Math.max(value, (int) (20 / rate)));
            }
        }
    }

    private static boolean isAmbientVoidingImmune(Entity entity) {
        return entity instanceof LivingEntity living && living.is(AMBIENT_VOIDING_IMMUNE);
    }

    private static float getAmbientVoidingRate(Entity entity) {
        return entity.level().environmentAttributes().getValue(EnderscapeEnvironmentAttributes.AMBIENT_ENTITY_VOIDING_RATE, entity.blockPosition());
    }

    private static float getAmbientVoidDamageRate(Entity entity) {
        return entity.level().environmentAttributes().getValue(EnderscapeEnvironmentAttributes.AMBIENT_VOID_DAMAGE_INTERVAL, entity.blockPosition());
    }

    public static boolean allowLivingEntityDamage(LivingEntity entity, DamageSource source, float amount) {
        if (source.is(EnderscapeDamageTypeTags.IS_VOID) && !source.is(EnderscapeDamageTypes.OUTER_VOID) && entity.hasEffect(EnderscapeMobEffects.VOID_RESISTANCE)) {
            return false;
        }

        return true;
    }

    public static boolean canVoidHealth(LivingEntity entity) {
        if (hasVoidImmunityTicks(entity) || entity.is(VOID_IMMUNE)) {
            return false;
        } else if (entity instanceof ServerPlayer player) {
            return player.level().getGameRules().get(EnderscapeGameRules.VOID_DAMAGE_OVERRIDES_HEALTH);
        } else {
            return true;
        }
    }

    public static boolean hasFullyVoidedHealth(LivingEntity entity) {
        return getVoidedHealth(entity) >= entity.getMaxHealth();
    }

    private static boolean tryVoidHealth(LivingEntity entity, DamageSource source, float amount) {
        if (!source.is(EnderscapeDamageTypeTags.VOIDS_HEALTH) || source.is(EnderscapeDamageTypes.OUTER_VOID)) return false;
        if (!canVoidHealth(entity) || hasFullyVoidedHealth(entity)) return false;

        modifyVoidedHealth(entity, value -> value + amount);

        return true;
    }

    public static float convertVoidDamage(LivingEntity entity, DamageSource source, float health) {
        if (!tryVoidHealth(entity, source, entity.getHealth() - health)) return health;

        if (hasFullyVoidedHealth(entity)) {
            return 0.0F;
        } else {
            return entity.getHealth();
        }
    }

    public static void afterLivingEntityDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
        if (!source.is(EnderscapeDamageTypeTags.VOIDS_HEALTH)) return;

        if (source.is(EnderscapeDamageTypes.OUTER_VOID) && canVoidHealth(entity)) {
            modifyVoidedHealth(entity, value -> value + damageTaken);

            float health = entity.getHealth();
            float maxHealth = entity.getMaxHealth();
            float nonVoided = maxHealth - getVoidedHealth(entity);

            if (health < nonVoided) {
                entity.setHealth(Mth.clamp(health + damageTaken, 0.0F, maxHealth));
            }
        }

        if (hasVoidedHealth(entity)) {
            for (MobEffectInstance instance : entity.getActiveEffects()) {
                if (instance.getEffect().is(EnderscapeMobEffectTags.UNSUPPORTED_WITH_VOIDED_HEALTH)) {
                    entity.removeEffect(instance.getEffect());
                }
            }
        }
    }

    public static void tickInOuterVoid(LivingEntity entity, Operation<Void> hurt) {
        if (!entity.isDeadOrDying()) {
            if (VoidManager.hasMaxOuterVoidTicks(entity)) {
                hurt.call(entity, entity.level().damageSources().source(EnderscapeDamageTypes.OUTER_VOID), entity.getMaxHealth());
            } else {
                VoidManager.modifyOuterVoidTicks(entity, value -> value + 1);
                VoidManager.setOuterVoidTickRemovalDelay(entity, 20);
            }
        } else {
            entity.removeAttached(OUTER_VOID_TICKS);
        }
    }

    /*
        Outer void tick removal delay
     */

    public static int getOuterVoidTickRemovalDelay(Entity entity) {
        return entity.getAttachedOrElse(OUTER_VOID_TICK_REMOVAL_DELAY, 0);
    }

    private static boolean hasOuterVoidTickRemovalDelay(Entity entity) {
        return entity.hasAttached(OUTER_VOID_TICK_REMOVAL_DELAY);
    }

    public static void setOuterVoidTickRemovalDelay(Entity entity, int value) {
        if (entity.is(VOID_IMMUNE)) return;

        entity.setAttached(OUTER_VOID_TICK_REMOVAL_DELAY, value);
        if (getOuterVoidTickRemovalDelay(entity) <= 0) entity.removeAttached(OUTER_VOID_TICK_REMOVAL_DELAY);
    }

    public static void modifyOuterVoidTickRemovalDelay(Entity entity, UnaryOperator<Integer> modifier) {
        if (!entity.hasAttached(OUTER_VOID_TICK_REMOVAL_DELAY)) {
            setOuterVoidTickRemovalDelay(entity, 0);
        }

        setOuterVoidTickRemovalDelay(entity, modifier.apply(getOuterVoidTickRemovalDelay(entity)));
    }

    /*
        Void tick down delay
     */

    public static int getVoidTickDownDelay(Entity entity) {
        return entity.getAttachedOrElse(VOID_TICK_DOWN_DELAY, 0);
    }

    private static boolean hasVoidTickDownDelay(Entity entity) {
        return entity.hasAttached(VOID_TICK_DOWN_DELAY);
    }

    public static void setVoidTickDownDelay(Entity entity, int value) {
        if (canBeVoided(entity)) {
            entity.setAttached(VOID_TICK_DOWN_DELAY, value);
            if (getVoidTickDownDelay(entity) <= 0) entity.removeAttached(VOID_TICK_DOWN_DELAY);
        } else if (entity.hasAttached(VOID_TICK_DOWN_DELAY)) {
            entity.removeAttached(VOID_TICK_DOWN_DELAY);
        }
    }

    public static void modifyVoidTickDownDelay(Entity entity, UnaryOperator<Integer> modifier) {
        if (!entity.hasAttached(VOID_TICK_DOWN_DELAY)) {
            setVoidTickDownDelay(entity, 0);
        }

        setVoidTickDownDelay(entity, modifier.apply(getVoidTickDownDelay(entity)));
    }

    /*
        Void Ticks
     */

    public static boolean canBeVoided(Entity entity) {
        if (entity instanceof LivingEntity living) return !living.is(VOID_IMMUNE);
        if (entity instanceof ItemEntity item) return !item.getItem().is(EnderscapeItemTags.VOID_IMMUNE);
        return false;
    }

    public static boolean hasVoidTicks(Entity entity) {
        return getVoidTicks(entity) > 0.0F;
    }

    public static int getVoidTicks(Entity entity) {
        if (!canBeVoided(entity)) return 0;

        return entity.getAttachedOrElse(VOID_TICKS, 0);
    }

    public static float getVoidTicksPercentage(Entity entity) {
        if (entity.isSpectator()) {
            return 0.0F;
        } else {
            return (float) Math.min(getVoidTicks(entity), MAXIMUM_VOID_TICKS) / MAXIMUM_VOID_TICKS;
        }
    }

    public static boolean hasMaximumVoidTicks(Entity entity) {
        return getVoidTicksPercentage(entity) >= 1.0F;
    }

    public static void setVoidTicks(Entity entity, int value) {
        if (canBeVoided(entity)) {
            entity.setAttached(VOID_TICKS, Math.min(value, MAXIMUM_VOID_TICKS));
            if (getVoidTicks(entity) <= 0) entity.removeAttached(VOID_TICKS);
        } else if (entity.hasAttached(VOID_TICKS)) {
            entity.removeAttached(VOID_TICKS);
        }
    }

    public static void modifyVoidTicks(Entity entity, UnaryOperator<Integer> modifier) {
        if (!entity.hasAttached(VOID_TICKS)) {
            setVoidTicks(entity, 0);
        }

        setVoidTicks(entity, modifier.apply(getVoidTicks(entity)));
    }

    /*
        In Outer Void Ticks
     */

    public static boolean hasOuterVoidTicks(Entity entity) {
        return getOuterVoidTicks(entity) > 0;
    }

    public static boolean hasMaxOuterVoidTicks(Entity entity) {
        return getOuterVoidTicks(entity) >= maxOuterVoidTicks(entity);
    }

    public static int maxOuterVoidTicks(Entity entity) {
        if (entity instanceof LivingEntity living) {
            return (int) (living.getMaxHealth() * OUTER_VOID_MAXIMUM_MODIFIER);
        } else {
            return DEFAULT_MAXIMUM_OUTER_VOID_TICKS;
        }
    }

    public static float getOuterVoidTicksPercentage(Entity entity) {
        int maximum = maxOuterVoidTicks(entity);
        return (float) Math.min(getOuterVoidTicks(entity), maximum) / maximum;
    }

    public static int getOuterVoidTicks(Entity entity) {
        return entity.getAttachedOrElse(OUTER_VOID_TICKS, 0);
    }

    public static void setOuterVoidTicks(Entity entity, int value) {
        entity.setAttached(OUTER_VOID_TICKS, Math.min(value, maxOuterVoidTicks(entity)));
        if (getOuterVoidTicks(entity) <= 0) entity.removeAttached(OUTER_VOID_TICKS);
    }

    public static void modifyOuterVoidTicks(Entity entity, UnaryOperator<Integer> modifier) {
        if (!entity.hasAttached(OUTER_VOID_TICKS)) {
            setOuterVoidTicks(entity, 0);
        }

        setOuterVoidTicks(entity, modifier.apply(getOuterVoidTicks(entity)));
    }

    /*
        Voided Health
     */

    public static boolean hasVoidedHealth(LivingEntity entity) {
        return getVoidedHealth(entity) > 0.0F;
    }

    public static float getVoidedHealth(LivingEntity entity) {
        return entity.getAttachedOrElse(VOIDED_HEALTH, 0.0F);
    }

    public static void setVoidedHealth(LivingEntity entity, float value) {
        if (entity.is(VOID_IMMUNE)) return;

        float clamped = Math.clamp(value, 0.0F, entity.getMaxHealth());
        entity.setAttached(VOIDED_HEALTH, clamped);
        if (getVoidedHealth(entity) <= 0) entity.removeAttached(VOIDED_HEALTH);
    }

    public static void modifyVoidedHealth(LivingEntity entity, UnaryOperator<Float> modifier) {
        if (!entity.hasAttached(VOIDED_HEALTH)) {
            setVoidedHealth(entity, 0.0F);
        }

        setVoidedHealth(entity, modifier.apply(getVoidedHealth(entity)));
    }

    /*
        Void Immunity Ticks
     */

    public static int getVoidImmunityTicks(Entity entity) {
        return entity.getAttachedOrElse(VOID_IMMUNITY_TICKS, 0);
    }

    public static void setVoidImmunityTicks(Entity entity, int value) {
        entity.setAttached(VOID_IMMUNITY_TICKS, value);
        if (getVoidImmunityTicks(entity) <= 0) entity.removeAttached(VOID_IMMUNITY_TICKS);
    }

    public static void modifyVoidImmunityTicks(Entity entity, UnaryOperator<Integer> modifier) {
        if (!entity.hasAttached(VOID_IMMUNITY_TICKS)) {
            setVoidImmunityTicks(entity, 0);
        }

        setVoidImmunityTicks(entity, modifier.apply(getVoidImmunityTicks(entity)));
    }

    public static boolean hasVoidImmunityTicks(Entity entity) {
        return getVoidImmunityTicks(entity) > 0;
    }

    /*
        Void Destruction Ticks
     */

    public static int getVoidDestructionTicks(Entity entity) {
        return entity.getAttachedOrElse(VOID_DESTRUCTION_TICKS, 0);
    }

    public static void setVoidDestructionTicks(Entity entity, int value) {
        entity.setAttached(VOID_DESTRUCTION_TICKS, value);
        if (getVoidDestructionTicks(entity) <= 0) entity.removeAttached(VOID_DESTRUCTION_TICKS);
    }

    public static void modifyVoidDestructionTicks(Entity entity, UnaryOperator<Integer> modifier) {
        if (!entity.hasAttached(VOID_DESTRUCTION_TICKS)) {
            setVoidDestructionTicks(entity, 0);
        }

        setVoidDestructionTicks(entity, modifier.apply(getVoidDestructionTicks(entity)));
    }

    /*
        Void Lachryma
     */

    public static boolean inVoidLachryma(Entity entity) {
        return entity.isInFluid(EnderscapeFluidTags.VOID_LACHRYMA);
    }

    private enum VoidDamageType {
        VOID_LACHRYMA(
                EnderscapeDamageTypes.VOID_LACHRYMA,
                (entity) -> inVoidLachryma(entity) || entity.getInBlockState().is(EnderscapeBlocks.VOID_LACHRYMA_CAULDRON),
                10
        ),
        VOID_CORRUPTION(
                EnderscapeDamageTypes.VOID,
                (entity) -> entity.hasEffect(EnderscapeMobEffects.VOID_CORRUPTION),
                (entity) -> {
                    int amplification = entity.getEffect(EnderscapeMobEffects.VOID_CORRUPTION).getAmplifier();
                    return Math.max(10, 40 >> amplification);
                }
        ),
        VOID_BIOME(
                EnderscapeDamageTypes.VOID,
                (entity) -> getAmbientVoidingRate(entity) > 0.0F,
                (entity) -> (int) (getAmbientVoidDamageRate(entity) * 20.0F)
        ),
        DEFAULT(
                EnderscapeDamageTypes.VOID,
                LivingEntity::isAlive,
                40
        );

        private final Function<LivingEntity, Integer> interval;
        private final ResourceKey<DamageType> damageType;
        private final Predicate<LivingEntity> predicate;

        VoidDamageType(ResourceKey<DamageType> damageType, Predicate<LivingEntity> predicate, Function<LivingEntity, Integer> interval) {
            this.damageType = damageType;
            this.predicate = predicate;
            this.interval = interval;
        }

        VoidDamageType(ResourceKey<DamageType> damageType, Predicate<LivingEntity> predicate, int interval) {
            this(damageType, predicate, ((_) -> interval));
        }

        public int interval(LivingEntity entity) {
            int baseInterval = interval.apply(entity);
            int value = baseInterval - (int) (entity.getPercentFrozen() * 20);

            return Math.max(10, value);
        }

        public ResourceKey<DamageType> damageType() {
            return damageType;
        }

        public static VoidDamageType get(LivingEntity entity) {
            return Arrays.stream(values())
                    .filter(type -> type != DEFAULT && type.predicate.test(entity))
                    .min(Comparator.comparingInt(type -> type.interval(entity)))
                    .orElse(DEFAULT);
        }
    }
}