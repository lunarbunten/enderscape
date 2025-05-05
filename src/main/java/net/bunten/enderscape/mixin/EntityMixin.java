package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {

    @Unique
    private final Entity self = (Entity) (Object) this;

    @ModifyReturnValue(method = "getPickRadius", at = @At(value = "RETURN"))
    public float expandHitRange(float original) {
        float expanded = 0.35F;

        if (self instanceof Silverfish && EnderscapeConfig.getInstance().silverfishExpandHitRange) {
            return expanded;
        }
        if (self instanceof Endermite && EnderscapeConfig.getInstance().endermiteExpandHitRange) {
            return expanded;
        }
        if (self instanceof Rubblemite && EnderscapeConfig.getInstance().rubblemiteExpandHitRange) {
            return expanded;
        }

        return original;
    }
}
