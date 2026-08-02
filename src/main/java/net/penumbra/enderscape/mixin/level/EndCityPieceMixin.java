package net.penumbra.enderscape.mixin.level;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
import net.minecraft.world.level.storage.loot.LootTable;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.registry.server.EnderscapeLootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EndCityPieces.EndCityPiece.class)
public abstract class EndCityPieceMixin {

    @ModifyArg(
            method = "handleDataMarker",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/RandomizableContainer;setBlockEntityLootTable(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceKey;)V"
            ),
            index = 3
    )
    protected ResourceKey<LootTable> Enderscape$changeEndCityTreasureLootTable(ResourceKey<LootTable> original) {
        return EnderscapeConfig.getInstance().supplementVanillaEndCityTreasureLoot ? EnderscapeLootTables.END_CITY_TREASURE_POST_SUPPLEMENTS : original;
    }
}