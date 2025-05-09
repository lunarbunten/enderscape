package net.bunten.enderscape.mixin;

import net.bunten.enderscape.entity.drifter.AbstractDrifter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(TemptingSensor.class)
public abstract class TemptingSensorMixin {

    @Shadow protected abstract boolean playerHoldingTemptation(Player player);

    @Unique
    private static final TargetingConditions Enderscape$DRIFTER_TEMPT_TARGETING = TargetingConditions.forNonCombat().range(24.0).ignoreLineOfSight();

    @Inject(method = "doTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/PathfinderMob;)V", at = @At("HEAD"), cancellable = true)
    private void Enderscape$modifyDrifterTemptDistance(ServerLevel level, PathfinderMob mob, CallbackInfo info) {
        if (mob instanceof AbstractDrifter drifter) {
            Brain<AbstractDrifter> brain = drifter.getBrain();
            List<Player> list = level.players()
                    .stream()
                    .filter(EntitySelector.NO_SPECTATORS)
                    .filter(player -> Enderscape$DRIFTER_TEMPT_TARGETING.test(drifter, player))
                    .filter(player -> drifter.closerThan(player, 24.0))
                    .filter(this::playerHoldingTemptation)
                    .filter(player -> !drifter.hasPassenger(player))
                    .sorted(Comparator.comparingDouble(drifter::distanceToSqr))
                    .collect(Collectors.toList());
            if (!list.isEmpty()) {
                Player player = list.get(0);
                brain.setMemory(MemoryModuleType.TEMPTING_PLAYER, player);
            } else {
                brain.eraseMemory(MemoryModuleType.TEMPTING_PLAYER);
            }

            info.cancel();
        }
    }
}