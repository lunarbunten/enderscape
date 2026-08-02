package net.penumbra.enderscape.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeFluidTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item {

    @Shadow
    @Final
    private Fluid content;

    public BucketItemMixin(Properties properties) {
        super(properties);
    }

    @ModifyArg(method = "playEmptySound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 2)
    private SoundEvent Enderscape$voidLachrymaEmptySound(SoundEvent sound) {
        return content.is(EnderscapeFluidTags.VOID_LACHRYMA) ? EnderscapeItemSounds.VOID_LACHRYMA_BUCKET_EMPTY : sound;
    }

    @WrapOperation(method = "emptyContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/Fluid;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean Enderscape$lachrymaEvaporatesInNether(Fluid instance, TagKey<Fluid> tag, Operation<Boolean> original) {
        return original.call(instance, tag) || instance.is(EnderscapeFluidTags.VOID_LACHRYMA);
    }
}