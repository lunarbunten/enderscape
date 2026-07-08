package net.bunten.enderscape.mixin;

import net.bunten.enderscape.Enderscape;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.TrialSpawnerBlock;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin {

    @Unique
    private static final List<ResourceLocation> VALID_WOOD_TYPES = List.of(
            Enderscape.id("veiled"),
            Enderscape.id("celestial"),
            Enderscape.id("murublight")
    );

    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void isValid(BlockState state, CallbackInfoReturnable<Boolean> info) {
        if (state.getBlock() instanceof CampfireBlock && state.getBlock().equals(VOID_CAMPFIRE)) info.setReturnValue(true);
        if (state.getBlock() instanceof SignBlock sign && VALID_WOOD_TYPES != null && VALID_WOOD_TYPES.contains(ResourceLocation.tryParse(sign.type().name()))) info.setReturnValue(true);
        if (state.getBlock() instanceof TrialSpawnerBlock && state.is(END_TRIAL_SPAWNER.get())) info.setReturnValue(true);
        if (state.getBlock() instanceof VaultBlock && state.is(END_VAULT.get())) info.setReturnValue(true);
    }
}