package net.penumbra.enderscape.mixin.item;

import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EnderpearlItem.class)
public abstract class EnderpearlItemMixin extends Item {

    public EnderpearlItemMixin(Properties properties) {
        super(properties);
    }

    @ModifyArgs(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void Enderscape$playSound(Args args) {
        if (EnderscapeConfig.getInstance().enderPearlUpdateThrowSound) {
            args.set(4, EnderscapeItemSounds.ENDER_PEARL_THROW);
            args.set(6, 1F);
            args.set(7, 1F);
        }
    }
}