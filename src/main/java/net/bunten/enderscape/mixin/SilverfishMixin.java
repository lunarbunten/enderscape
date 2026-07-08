package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn;

@Mixin(Silverfish.class)
public abstract class SilverfishMixin {

    @Inject(method = "checkSilverfishSpawnRules", at = @At("RETURN"), cancellable = true)
    private static void Enderscape$adjustSilverfishSpawnRules(EntityType<Silverfish> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> info) {
        if (EnderscapeConfig.getInstance().silverfishNaturalSpawnsObeyLightLevel && !reason.equals(MobSpawnType.SPAWNER) && level instanceof ServerLevelAccessor server) {
            boolean isDarkEnough = MobSpawnType.ignoresLightRequirements(reason) || isDarkEnoughToSpawn(server, pos, random);
            if (!isDarkEnough) info.setReturnValue(false);
        }
    }
}