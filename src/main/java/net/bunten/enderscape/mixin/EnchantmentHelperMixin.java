package net.bunten.enderscape.mixin;

import net.bunten.enderscape.item.component.AttackSounds;
import net.bunten.enderscape.item.component.StunAttack;
import net.bunten.enderscape.registry.EnderscapeAttributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(at = @At("HEAD"), method = "doPostAttackEffects")
    private static void Enderscape$doPostAttackEffects(ServerLevel level, Entity entity, DamageSource source, CallbackInfo info) {
        if (entity instanceof LivingEntity victim && source.getEntity() instanceof LivingEntity attacker && source.getWeaponItem() != null) {
            ItemStack stack = source.getWeaponItem();

            if (EnderscapeAttributes.isBackstab(stack, attacker.position(), victim) && AttackSounds.is(stack)) {
                level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), AttackSounds.get(stack).backstab().value(), attacker.getSoundSource(), 1.0F, 1.0F);
            }

            if (StunAttack.is(stack)) StunAttack.apply(level, attacker, victim, stack);
        }
    }
}