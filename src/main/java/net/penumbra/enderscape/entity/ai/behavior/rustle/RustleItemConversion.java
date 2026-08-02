package net.penumbra.enderscape.entity.ai.behavior.rustle;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.entity.rustle.Rustle;
import net.penumbra.enderscape.entity.rustle.RustleConversionPhase;
import net.penumbra.enderscape.item.crafting.value.RustleRecipeEffects;

import java.util.Optional;

import static net.penumbra.enderscape.entity.ai.EnderscapeMemory.RUSTLE_IS_CONVERTING;

public class RustleItemConversion extends Behavior<Rustle> {

    private static final int BEGINNING_ANIMATION_DURATION = calculateDuration(0.5F);
    private static final int ENDING_ANIMATION_DURATION = calculateDuration(0.7F);

    private long beginningTimestamp = -1;

    public RustleItemConversion() {
        super(ImmutableMap.of(RUSTLE_IS_CONVERTING, MemoryStatus.VALUE_PRESENT), BEGINNING_ANIMATION_DURATION + RustleRecipeEffects.MAX_SWELL_DURATION + ENDING_ANIMATION_DURATION);
    }

    @Override
    protected void start(ServerLevel level, Rustle body, long timestamp) {
        super.start(level, body, timestamp);

        beginningTimestamp = timestamp;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Rustle body) {
        return body.getQueuedRecipe().isPresent() && body.isCurrently(RustleConversionPhase.BEGINNING) && body.hasInventoryItem();
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Rustle body, long time) {
        return body.hasInventoryItem() || body.isCurrently(RustleConversionPhase.ENDING);
    }

    @Override
    protected void tick(ServerLevel level, Rustle body, long timestamp) {
        body.getQueuedRecipe().ifPresent(recipe -> {
            int durationUntilEnding = BEGINNING_ANIMATION_DURATION + recipe.effects().swellDuration();

            if (body.isCurrently(RustleConversionPhase.ENDING) && timestamp >= beginningTimestamp + durationUntilEnding + ENDING_ANIMATION_DURATION) {
                body.setConversionPhase(RustleConversionPhase.INACTIVE);
            }

            if (body.isCurrently(RustleConversionPhase.CONVERTING) && timestamp >= beginningTimestamp + durationUntilEnding) {
                spitItem(level, body);
                body.setConversionPhase(RustleConversionPhase.ENDING);
            }

            if (body.isCurrently(RustleConversionPhase.BEGINNING) && timestamp >= beginningTimestamp + BEGINNING_ANIMATION_DURATION) {
                body.setConversionPhase(RustleConversionPhase.CONVERTING);
                body.playSound(recipe.effects().swellSound().value(), 1.0F, recipe.effects().swellSoundPitch());
            }
        });
    }

    @Override
    protected void stop(ServerLevel level, Rustle body, long timestamp) {
        body.setConversionPhase(RustleConversionPhase.INACTIVE);
        body.setQueuedRecipe(Optional.empty());

        body.getBrain().eraseMemory(RUSTLE_IS_CONVERTING);
    }

    private void spitItem(ServerLevel level, Rustle body) {
        body.getQueuedRecipe().ifPresent(recipe -> {
            Rustle.regrowHair(level, body, false);

            body.playSound(recipe.effects().spitSound().value(), 1.0F, 1.0F);
            body.getInventory().removeAllItems();

            Vec3 position = body.getEyePosition();
            level.sendParticles(recipe.effects().convertParticle(), position.x, position.y + 0.4, position.z, 4, 0.2, 0.2, 0.2, 0.2);

            BehaviorUtils.throwItem(
                    body,
                    recipe.result().create(),
                    body.getEyePosition().add(0, 0.4, 0).add(body.getLookAngle().scale(0.125)),
                    new Vec3(0.4F, 0.3F, 0.4F),
                    0.1F
            );

            createExperience(level, position, recipe.experience());
        });
    }

    private static void createExperience(final ServerLevel level, final Vec3 position, final float value) {
        int xpReward = Mth.floor(value);
        float xpFraction = Mth.frac(value);

        if (xpFraction != 0.0F && level.getRandom().nextFloat() < xpFraction) {
            xpReward++;
        }

        ExperienceOrb.award(level, position, xpReward);
    }

    private static int calculateDuration(float inSeconds) {
        return (int) (20 * inSeconds);
    }
}