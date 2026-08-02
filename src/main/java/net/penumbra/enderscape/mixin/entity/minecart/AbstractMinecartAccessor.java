package net.penumbra.enderscape.mixin.entity.minecart;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractMinecart.class)
public interface AbstractMinecartAccessor {
    @Invoker
    void callComeOffTrack(ServerLevel serverLevel);
}
