package net.penumbra.enderscape.registry.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.entity.magnia.MagniaInteractionBehavior;
import net.penumbra.enderscape.mixin.entity.projectile.AbstractArrowAccessor;
import net.penumbra.enderscape.registry.tag.EnderscapeItemTags;

import java.util.function.Predicate;

public class EnderscapeMagniaInteractionBehaviors {

    private static final AttributeModifier MAGNIA_GRAVITY_MODIFIER = new AttributeModifier(Enderscape.id("magnia_gravity"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    static {
        MagniaInteractionBehavior.registerOverride(AbstractArrow.class, arrowLikeBehavior());
        MagniaInteractionBehavior.registerOverride(AbstractMinecart.class, minecartBehavior());
        MagniaInteractionBehavior.registerOverride(ExperienceOrb.class, experienceOrbBehavior());
        MagniaInteractionBehavior.registerOverride(IronGolem.class, ironGolemBehavior());
        MagniaInteractionBehavior.registerOverride(ItemEntity.class, itemBehavior());
        MagniaInteractionBehavior.registerOverride(LivingEntity.class, livingEntityBehavior());
        MagniaInteractionBehavior.registerOverride(Player.class, playerBehavior());
        MagniaInteractionBehavior.registerOverride(SulfurCube.class, sulfurCubeBehavior());
    }

    public static MagniaInteractionBehavior<ItemEntity> itemBehavior() {
        return new MagniaInteractionBehavior.Builder<ItemEntity>()
                .shouldApply(Entity::isAlive)
                .startMoving(entity -> {
                    entity.setPickUpDelay(20);
                    entity.setNoGravity(true);
                }).build();
    }

    public static MagniaInteractionBehavior<ExperienceOrb> experienceOrbBehavior() {
        return new MagniaInteractionBehavior.Builder<ExperienceOrb>().shouldApply(Entity::isAlive).build();
    }


    public static MagniaInteractionBehavior<LivingEntity> livingEntityBehavior() {
        return livingEntityBuilder().build();
    }

    private static <T extends LivingEntity> MagniaInteractionBehavior.Builder<T> livingEntityBuilder() {
        return new MagniaInteractionBehavior.Builder<T>()
                .shouldApply(entity -> baseLivingEntityPredicate().test(entity))
                .movementType(MagniaInteractionBehavior.MovementType.ADD)
                .attractStrength(entity -> 0.01F * getMagnetismFactor(entity))
                .repelStrength(entity -> 0.02F * getMagnetismFactor(entity))
                .startMoving(entity -> {
                    if (!(entity instanceof Player)) {
                        addGravityModifier(entity);
                    }
                    entity.fallDistance = 0;
                }).stopMoving(entity -> {
                    if (!(entity instanceof Player)) {
                        removeGravityModifier(entity);
                    }
                    entity.fallDistance = 0;
                });
    }

    public static MagniaInteractionBehavior<Player> playerBehavior() {
        return new MagniaInteractionBehavior.Builder<Player>()
                .copy(livingEntityBuilder())
                .shouldApply(player -> baseLivingEntityPredicate().test(player) && !player.getAbilities().flying)
                .build();
    }

    public static MagniaInteractionBehavior<LivingEntity> ironGolemBehavior() {
        return livingEntityBuilder()
                .shouldApply(LivingEntity::isAlive)
                .attractStrength(0.075F)
                .repelStrength(0.075F)
                .build();
    }

    public static MagniaInteractionBehavior<Entity> minecartBehavior() {
        return new MagniaInteractionBehavior.Builder<>()
                .shouldApply(Entity::isAlive)
                .movementType(MagniaInteractionBehavior.MovementType.ADD)
                .attractStrength(0.075F)
                .repelStrength(0.075F)
                .build();
    }

    public static MagniaInteractionBehavior<AbstractArrow> arrowLikeBehavior() {
        return new MagniaInteractionBehavior.Builder<AbstractArrow>()
                .shouldApply(entity -> entity.isAlive() && !((AbstractArrowAccessor) entity).callIsInGround())
                .movementType(MagniaInteractionBehavior.MovementType.ADD)
                .attractStrength(0.25F)
                .repelStrength(0.25F)
                .build();
    }

    public static MagniaInteractionBehavior<SulfurCube> sulfurCubeBehavior() {
        return new MagniaInteractionBehavior.Builder<SulfurCube>()
                .copy(livingEntityBuilder())
                .attractStrength(0.075F)
                .repelStrength(0.075F)
                .build();
    }

    private static <T extends LivingEntity> Predicate<T> baseLivingEntityPredicate() {
        return entity -> entity.isAlive() && getMagnetismFactor(entity) > 0.0;
    }

    private static float getMagnetismFactor(LivingEntity entity) {
        int weak = 0, average = 0, strong = 0;

        for (EquipmentSlot slot : EquipmentSlot.VALUES.stream().filter(EquipmentSlot::isArmor).toList()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.is(EnderscapeItemTags.WEAK_MAGNIA_STRENGTH)) weak++;
            else if (stack.is(EnderscapeItemTags.AVERAGE_MAGNIA_STRENGTH)) average++;
            else if (stack.is(EnderscapeItemTags.STRONG_MAGNIA_STRENGTH)) strong++;
        }

        return weak * 0.5F + average + strong * 1.5F;
    }

    private static void addGravityModifier(LivingEntity living) {
        AttributeInstance gravity = living.getAttribute(Attributes.GRAVITY);

        if (!gravity.hasModifier(MAGNIA_GRAVITY_MODIFIER.id())) {
            gravity.addTransientModifier(MAGNIA_GRAVITY_MODIFIER);
        }
    }

    private static void removeGravityModifier(LivingEntity living) {
        AttributeInstance gravity = living.getAttribute(Attributes.GRAVITY);

        if (gravity.hasModifier(MAGNIA_GRAVITY_MODIFIER.id())) {
            gravity.removeModifier(MAGNIA_GRAVITY_MODIFIER);
        }
    }
}