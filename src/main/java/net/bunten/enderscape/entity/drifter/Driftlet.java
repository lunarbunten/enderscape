package net.bunten.enderscape.entity.drifter;

import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class Driftlet extends AbstractDrifter {
    public static int MAX_GROWTH_AGE = 24000;
    private int growthAge;
    
    public Driftlet(EntityType<? extends AbstractDrifter> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseDrifterAttributes().add(Attributes.MAX_HEALTH, 8).add(Attributes.FLYING_SPEED, 0.5);
    }

    private void ageUp() {
        if (level() instanceof ServerLevel server) {
            convertTo(EnderscapeEntities.DRIFTER, ConversionParams.single(this, false, false), mob -> {
                mob.finalizeSpawn(server, level().getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.CONVERSION, null);
                mob.setPersistenceRequired();
                mob.fudgePositionAfterSizeChange(getDimensions(getPose()));
            });
        }
    }

    private void setGrowthAge(int value) {
        growthAge = value;
        if (growthAge >= MAX_GROWTH_AGE && level().noCollision(getBoundingBox().inflate(1))) {
            ageUp();
        }
    }

    private void increaseAge(int seconds) {
        setGrowthAge(growthAge + seconds * 20);
    }

    private int getTicksUntilGrowth() {
        return Math.max(0, MAX_GROWTH_AGE - growthAge);
    }

    @Override
    public SoundEvent getJumpSound() {
        return EnderscapeEntitySounds.DRIFTLET_JUMP;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide()) {
            setGrowthAge(growthAge + 1);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Age", growthAge);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getInt("Age").ifPresent(this::setGrowthAge);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(EnderscapeItemTags.DRIFTER_FOOD)) {
            usePlayerItem(player, hand, stack);
            increaseAge(AgeableMob.getSpeedUpSecondsWhenFeeding(getTicksUntilGrowth()));
            level().addParticle(ParticleTypes.HAPPY_VILLAGER, getRandomX(1), getRandomY() + 0.5, getRandomZ(1), 0, 0, 0);
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return EnderscapeEntitySounds.DRIFTLET_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return EnderscapeEntitySounds.DRIFTLET_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return EnderscapeEntitySounds.DRIFTLET_DEATH;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }
}