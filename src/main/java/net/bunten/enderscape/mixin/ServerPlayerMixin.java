package net.bunten.enderscape.mixin;

import com.mojang.authlib.GameProfile;
import net.bunten.enderscape.entity.DashJumpUser;
import net.bunten.enderscape.registry.EnderscapeStats;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    @Shadow
    private static boolean didNotMove(double d, double e, double f) {
        return false;
    }

    public ServerPlayerMixin(Level level, BlockPos pos, float f, GameProfile profile) {
        super(level, pos, f, profile);
    }

    @Inject(at = @At("TAIL"), method = "checkMovementStatistics")
    public void Enderscape$checkMovementStatistics(double x, double y, double z, CallbackInfo ci) {
        if (!isPassenger() && !didNotMove(x, y, z) && DashJumpUser.is(this) && DashJumpUser.dashed(this)) {
            int i = Math.round((float) Math.sqrt(x * x + z * z) * 100.0F);
            if (i > 0) awardStat(EnderscapeStats.RUBBLE_SHIELD_DASH_ONE_CM, i);
        }
    }
}