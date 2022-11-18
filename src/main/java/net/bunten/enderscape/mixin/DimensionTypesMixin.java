package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.data.worldgen.DimensionTypes;

/*
 *  Modifies the minimum Y level of The End to be -64
 */
@Mixin(DimensionTypes.class)
public abstract class DimensionTypesMixin {

    @ModifyArgs(method = "bootstrap", at = @At(value = "INVOKE", ordinal = 2, target = "Lnet/minecraft/world/level/dimension/DimensionType;<init>(Ljava/util/OptionalLong;ZZZZDZZIIILnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceLocation;FLnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;)V"))
    private static void injected(Args args) {
        args.set(8, -64);
    }
}