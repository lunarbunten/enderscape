package net.penumbra.enderscape.mixin.block.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerStateData;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mixin(TrialSpawnerStateData.class)
public abstract class TrialSpawnerDataMixin {

    @Shadow @Final
    public Set<UUID> detectedPlayers;

    @Shadow
    long nextMobSpawnsAt;

    @Inject(method = "tryDetectPlayers", at = @At("HEAD"), cancellable = true)
    private void Enderscape$tryDetectPlayers(ServerLevel level, BlockPos pos, TrialSpawner spawner, CallbackInfo info) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_TRIAL_SPAWNER)) {
            info.cancel();

            if ((pos.asLong() + level.getGameTime()) % 20L == 0L) {
                if (!spawner.getState().equals(TrialSpawnerState.COOLDOWN)) {
                    List<UUID> nearbyPlayers = spawner.getPlayerDetector().detect(level, spawner.getEntitySelector(), pos, spawner.getRequiredPlayerRange(), true);

                    if (!spawner.getState().equals(TrialSpawnerState.COOLDOWN)) {
                        boolean noPreviouslyDetected = spawner.getStateData().detectedPlayers.isEmpty();
                        List<UUID> newlyDetectedPlayers = noPreviouslyDetected ? nearbyPlayers : spawner.getPlayerDetector().detect(level, spawner.getEntitySelector(), pos, spawner.getRequiredPlayerRange(), false);

                        if (detectedPlayers.addAll(newlyDetectedPlayers)) {
                            nextMobSpawnsAt = Math.max(level.getGameTime() + 40L, this.nextMobSpawnsAt);
                            level.levelEvent(3013, pos, this.detectedPlayers.size());
                        }
                    }
                }
            }
        }
    }
}