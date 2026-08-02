package net.penumbra.enderscape.mixin.entity.silverfish;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.penumbra.enderscape.config.EnderscapeConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Silverfish.class)
public abstract class SilverfishMixin extends Monster {
    protected SilverfishMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
            method = "checkSilverfishSpawnRules",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/Silverfish;checkAnyLightMonsterSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z"
            )
    )
    private static boolean Enderscape$changeSpawnRules(EntityType<Silverfish> type, LevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random, Operation<Boolean> original) {
        if (EnderscapeConfig.getInstance().silverfishNaturalSpawnsObeyLightLevel && level instanceof ServerLevelAccessor server) {
            return checkMonsterSpawnRules(type, server, reason, pos, random);
        } else {
            return original.call(type, level, reason, pos, random);
        }
    }
}