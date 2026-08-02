package net.penumbra.enderscape.mixin.level;

import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(EnderDragonFight.class)
public interface EnderDragonFightAccessor {

    @Accessor
    List<Integer> getGateways();

    @Invoker
    void callSpawnNewGateway();
}