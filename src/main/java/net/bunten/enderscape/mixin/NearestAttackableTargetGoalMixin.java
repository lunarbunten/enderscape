package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin<T extends LivingEntity> extends TargetGoal {

    public NearestAttackableTargetGoalMixin(Mob mob, boolean bl) {
        super(mob, bl);
    }

    @Redirect(
            method = "findTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    private <B extends Entity> List<B>redirectGetEntitiesOfClass(Level instance, Class<B> entityClass, AABB aabb, Predicate<? super B> predicate) {
        return instance.getEntitiesOfClass(entityClass, aabb, entity -> {
            if (mob instanceof EnderMan && entity instanceof Endermite endermite) {
                return isEndermiteUnsafe(endermite, instance);
            }
            return predicate.test(entity);
        });
    }

    private static boolean isEndermiteUnsafe(LivingEntity entity, Level level) {
        return BlockPos.findClosestMatch(entity.blockPosition(), 6, 6, pos -> level.getBlockState(pos).is(EnderscapeBlockTags.ENDERMITE_SAFE_WHEN_NEARBY)).isEmpty();
    }
}