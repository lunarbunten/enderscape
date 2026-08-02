package net.penumbra.enderscape.block.fluid;

import net.fabricmc.fabric.api.registry.fluid.FluidBehavior;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.registry.tag.EnderscapeEntityTags;
import net.penumbra.enderscape.registry.tag.EnderscapeFluidTags;

@SuppressWarnings("UnstableApiUsage")
public record VoidLachrymaBehavior() implements FluidBehavior {
    @Override
    public void onFluidEntered(TagKey<Fluid> fluid, Entity entity, boolean firstTick) {
        if (!entity.firstTick && !entity.isSpectator()) {
            VoidLachrymaFluid.doSplashEffect(entity);
        }
    }

    @Override
    public void handleFluidInteractionUpdate(TagKey<Fluid> fluid, Entity entity, EntityFluidInteraction interaction, boolean canPushEntity) {
        entity.resetFallDistance();

        if (entity.is(EnderscapeEntityTags.VOID_LACHRYMA_WALKABLE_MOBS) && entity.getFluidHeight(EnderscapeFluidTags.VOID_LACHRYMA) > 0.02) {
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5).add(0.0, 0.08, 0.0));
        }

        if (canPushEntity) {
            interaction.applyCurrentTo(EnderscapeFluidTags.VOID_LACHRYMA, entity, 0.009);
        }
    }

    @Override
    public void travelInFluid(TagKey<Fluid> fluid, LivingEntity entity, Vec3 input, double baseGravity, boolean isFalling, double oldY) {
        entity.moveRelative(0.02F, input);
        entity.move(MoverType.SELF, entity.getDeltaMovement());

        if (entity.getFluidHeight(EnderscapeFluidTags.VOID_LACHRYMA) <= entity.getFluidJumpThreshold()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.8F, 0.5));
            Vec3 movement = entity.getFluidFallingAdjustedMovement(baseGravity, isFalling, entity.getDeltaMovement());
            entity.setDeltaMovement(movement);
        } else {
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5));
        }

        if (baseGravity != 0.0) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, -baseGravity / 4.0, 0.0));
        }

        entity.jumpOutOfFluid(oldY);
    }

    @Override
    public void travelFlyingInFluid(TagKey<Fluid> fluid, LivingEntity entity, Vec3 input, float waterSpeed, float lavaSpeed, float airSpeed) {
        entity.moveRelative(lavaSpeed, input);
        entity.move(MoverType.SELF, entity.getDeltaMovement());
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5F));
    }

    @Override
    public boolean shouldTryFloatingInFluid(TagKey<Fluid> fluid, Entity entity) {
        return !VoidManager.standingOnVoidLachryma(entity);
    }
}