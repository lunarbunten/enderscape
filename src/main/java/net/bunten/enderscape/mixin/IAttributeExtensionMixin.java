package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeAttributes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.extensions.IAttributeExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IAttributeExtension.class)
public interface IAttributeExtensionMixin {

    @Inject(method = "getBaseId", at = @At("HEAD"), cancellable = true)
    default void getBaseId(CallbackInfoReturnable<ResourceLocation> cir) {
        if (this == EnderscapeAttributes.BACKSTAB_DAMAGE.value()) cir.setReturnValue(EnderscapeAttributes.BASE_BACKSTAB_DAMAGE_ID);
    }
}
