package net.penumbra.enderscape.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.penumbra.enderscape.EnderscapeClient;
import net.penumbra.enderscape.manager.ClientsideVariables;
import net.penumbra.enderscape.network.ServerboundSpawnTotemParticlesPayload;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {

    @Shadow
    private int itemActivationTicks;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private Vector3f previousTranslation;

    @Unique
    private Quaternionfc previousQuatYP;

    @Unique
    private Quaternionfc previousQuatXP;

    @Unique
    private Quaternionfc previousQuatZP;

    @Unique
    private static final int TOTEM_GLITCH_TIMESTAMP = 20;

    @Unique
    private static final int TOTEM_SHATTER_TIMESTAMP = 12;

    @Unique
    private final ClientsideVariables variables = EnderscapeClient.clientsideVariables();

    @Unique
    private float glitchTime;

    @WrapOperation(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 0))
    public void Enderscape$changeTotemTranslation(PoseStack pose, float x, float y, float z, Operation<Void> original) {
        glitchTime += minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);

        if (variables.displayVoidedTotemEffect) {
            if (itemActivationTicks <= TOTEM_SHATTER_TIMESTAMP) {
                original.call(pose, 0.0F, 0.0F, -9999.0F);

                if (itemActivationTicks == TOTEM_SHATTER_TIMESTAMP)
                    ClientPlayNetworking.send(new ServerboundSpawnTotemParticlesPayload());

                return;
            } else if (itemActivationTicks <= TOTEM_GLITCH_TIMESTAMP) {
                if (glitchTime > 0.1F) {
                    glitchTime = 0.0F;

                    RandomSource random = minecraft.level.getRandom();
                    float rx = previousTranslation.x + range(random, 0.05F);
                    float ry = previousTranslation.y + (range(random, 0.025F));
                    original.call(pose, rx, ry, previousTranslation.z);

                } else {
                    original.call(pose, previousTranslation.x, previousTranslation.y, previousTranslation.z);

                }
                return;
            }
        }

        original.call(pose, x, y, z);
        previousTranslation = new Vector3f(x, y, z);
    }

    @WrapOperation(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", ordinal = 0))
    public void Enderscape$changeTotemRotationYP(PoseStack pose, Quaternionfc quat, Operation<Void> original) {
        boolean voided = variables.displayVoidedTotemEffect && itemActivationTicks <= TOTEM_GLITCH_TIMESTAMP;
        if (!voided) previousQuatYP = quat;
        original.call(pose, voided ? previousQuatYP : quat);
    }

    @WrapOperation(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", ordinal = 1))
    public void Enderscape$changeTotemRotationXP(PoseStack pose, Quaternionfc quat, Operation<Void> original) {
        boolean voided = variables.displayVoidedTotemEffect && itemActivationTicks <= TOTEM_GLITCH_TIMESTAMP;
        if (!voided) previousQuatXP = quat;
        original.call(pose, voided ? previousQuatXP : quat);
    }

    @WrapOperation(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", ordinal = 2))
    public void Enderscape$changeTotemRotationZP(PoseStack pose, Quaternionfc quat, Operation<Void> original) {
        boolean voided = variables.displayVoidedTotemEffect && itemActivationTicks <= TOTEM_GLITCH_TIMESTAMP;
        if (!voided) previousQuatZP = quat;
        original.call(pose, voided ? previousQuatZP : quat);
    }

    @Unique
    private static float range(RandomSource random, float range) {
        return Mth.nextFloat(random, -range, range);
    }
}