package net.penumbra.enderscape.mixin.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.penumbra.enderscape.config.EnderscapeConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.ClientLevelData.class)
public abstract class ClientLevelDataMixin {

    @ModifyConstant(method = "voidDarknessOnsetRange", constant = @Constant(floatValue = 32.0F))
    public float Enderscape$changeVoidDarknessOnsetRange(float original) {
        return -EnderscapeConfig.getInstance().outerVoidHeightTreshold;
    }
}