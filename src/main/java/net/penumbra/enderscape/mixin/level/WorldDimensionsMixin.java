package net.penumbra.enderscape.mixin.level;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.penumbra.enderscape.util.MixinUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldDimensions.class)
public class WorldDimensionsMixin {
    @WrapMethod(method = "bake")
    private WorldDimensions.Complete wrapMethodWithScope(Registry<LevelStem> baseDimensions, Operation<WorldDimensions.Complete> original) {
        // Make base dimensions accessible in Entry below.
        return ScopedValue.where(MixinUtil.BASE_REGISTRIES, baseDimensions).call(() -> original.call(baseDimensions));
    }

    @Mixin(targets = "net/minecraft/world/level/levelgen/WorldDimensions$1Entry")
    public static class Entry {
        @WrapOperation(method = "registrationInfo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/WorldDimensions;checkStability(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;)Lcom/mojang/serialization/Lifecycle;"))
        private Lifecycle Enderscape$builtinEndWorldgenReplacementIsStable(ResourceKey<LevelStem> key, LevelStem dimension, Operation<Lifecycle> original) {
            if (key.equals(LevelStem.END)) {
                var entry = MixinUtil.BASE_REGISTRIES.get().registrationInfo(LevelStem.END);

                if (entry.isPresent() && entry.get().lifecycle() == Lifecycle.stable()
                        && entry.get().knownPackInfo().isPresent() && entry.get().knownPackInfo().get().id().startsWith("enderscape")) {
                    return Lifecycle.stable();
                }
            }

            return original.call(key, dimension);
        }
    }
}
