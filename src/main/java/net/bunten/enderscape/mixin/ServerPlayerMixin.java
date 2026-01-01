package net.bunten.enderscape.mixin;

import com.mojang.authlib.GameProfile;
import net.bunten.enderscape.entity.DashJumpUser;
import net.bunten.enderscape.item.component.DashJump;
import net.bunten.enderscape.registry.EnderscapeDataComponents;
import net.bunten.enderscape.registry.EnderscapeStats;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Shadow
    private static boolean didNotMove(double d, double e, double f) {
        return false;
    }

    @Shadow public abstract ServerLevel level();

    @Unique
    private final ServerPlayer player = (ServerPlayer) (Object) this;

    @Inject(at = @At("HEAD"), method = "jumpFromGround")
    public void Enderscape$jumpFromGround(CallbackInfo info) {
        ItemStack item = player.getUseItem();
        if (player.isUsingItem() && item.has(EnderscapeDataComponents.DASH_JUMP)) DashJump.apply(level(), player, item);
    }

    @Inject(at = @At("TAIL"), method = "checkMovementStatistics")
    public void Enderscape$checkMovementStatistics(double x, double y, double z, CallbackInfo ci) {
        if (!isPassenger() && !didNotMove(x, y, z) && DashJumpUser.is(this) && DashJumpUser.dashed(this)) {
            int i = Math.round((float) Math.sqrt(x * x + z * z) * 100.0F);
            if (i > 0) awardStat(EnderscapeStats.RUBBLE_SHIELD_DASH_ONE_CM, i);
        }
    }
}