package net.penumbra.enderscape.mixin.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.penumbra.enderscape.entity.ai.EnderscapePathTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WalkNodeEvaluator.class)
public abstract class WalkNodeEvaluatorMixin {

    @Inject(method = "getPathTypeStatic(Lnet/minecraft/world/level/pathfinder/PathfindingContext;Lnet/minecraft/core/BlockPos$MutableBlockPos;)Lnet/minecraft/world/level/pathfinder/PathType;", at = @At(value = "HEAD"), cancellable = true)
    private static void Enderscape$getPathTypeStatic(PathfindingContext context, BlockPos.MutableBlockPos pos, CallbackInfoReturnable<PathType> info) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        PathType type = context.getPathTypeFromState(x, y, z);

        if (type == PathType.OPEN && y >= context.level().getMinY() + 1) {
            PathType belowType = context.getPathTypeFromState(x, y - 1, z);

            if (belowType.equals(EnderscapePathTypes.VOID_FIRE)) {
                info.setReturnValue(EnderscapePathTypes.VOID_FIRE);
            }

            if (belowType.equals(EnderscapePathTypes.VOID_LACHRYMA)) {
                info.setReturnValue(EnderscapePathTypes.VOID_LACHRYMA);
            }

            if (belowType.equals(EnderscapePathTypes.VOID_SHALE)) {
                info.setReturnValue(EnderscapePathTypes.VOID_SHALE);
            }
        }
    }
}