package net.penumbra.enderscape.mixin.block;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.Enderscape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin {

    @Unique
    private static final List<Identifier> VALID_WOOD_TYPES = List.of(
            Enderscape.id("veiled"),
            Enderscape.id("celestial"),
            Enderscape.id("murublight")
    );

    @Unique
    private static final List<Identifier> VALID_SHELVES = List.of(
            Enderscape.id("veiled_shelf"),
            Enderscape.id("celestial_shelf"),
            Enderscape.id("murublight_shelf")
    );

    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void isValid(BlockState state, CallbackInfoReturnable<Boolean> info) {
        BlockEntityType<?> self = (BlockEntityType<?>)(Object) this;

        if (self == BlockEntityTypes.CAMPFIRE && state.getBlock() instanceof CampfireBlock && state.getBlock().equals(VOID_CAMPFIRE)) info.setReturnValue(true);
        if (self == BlockEntityTypes.SHELF && state.getBlock() instanceof ShelfBlock && VALID_SHELVES.contains(state.getBlock().builtInRegistryHolder().key().identifier())) info.setReturnValue(true);
        if (self == BlockEntityTypes.SIGN && state.getBlock() instanceof SignBlock sign && VALID_WOOD_TYPES.contains(Identifier.tryParse(sign.type().name()))) info.setReturnValue(true);
        if (self == BlockEntityTypes.TRIAL_SPAWNER && state.getBlock() instanceof TrialSpawnerBlock && state.is(END_TRIAL_SPAWNER)) info.setReturnValue(true);
        if (self == BlockEntityTypes.VAULT && state.getBlock() instanceof VaultBlock && state.is(END_VAULT)) info.setReturnValue(true);
    }
}
