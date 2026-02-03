package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Predicate;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin extends TargetGoal {

    public NearestAttackableTargetGoalMixin(Mob mob, boolean bl) {
        super(mob, bl);
    }

    @WrapOperation(
            method = "findTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    private <B extends Entity> List<B>redirectGetEntitiesOfClass(Level level, Class<B> entityClass, AABB aabb, Predicate<B> predicate, Operation<List<B>> original) {
        if (mob instanceof EnderMan) return original.call(level, entityClass, aabb, Enderscape$canTarget(level, predicate));
        return original.call(level, entityClass, aabb, predicate);
    }

    @NotNull
    @Unique
    private <B extends Entity> Predicate<B> Enderscape$canTarget(Level level, Predicate<B> predicate) {
        return (B entity) -> {
            if (entity instanceof Endermite endermite) {
                return BlockPos.findClosestMatch(endermite.blockPosition(), 6, 6, pos -> level.getBlockState(pos).is(EnderscapeBlockTags.ENDERMITE_SAFE_WHEN_NEARBY)).isEmpty();
            } else {
                return predicate.test(entity);
            }
        };
    }
}