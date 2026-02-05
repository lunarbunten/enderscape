package net.bunten.enderscape.item;

import net.bunten.enderscape.item.component.FueledTool;
import net.bunten.enderscape.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.bunten.enderscape.item.component.FueledTool.useFuel;
import static net.bunten.enderscape.registry.EnderscapeDataComponents.BACKSTAB_ANGLE;

public class DaggerItem extends EnchantableItem {
    public DaggerItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (user instanceof Player player) player.getCooldowns().addCooldown(this, 20 * 5);

        return super.finishUsingItem(stack, level, user);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    private static boolean canStun(LivingEntity attacker, ItemStackContext context) {
        ItemStack stack = context.stack();
        boolean notOnCooldown = !(attacker instanceof Player) || !((Player) attacker).getCooldowns().isOnCooldown(stack.getItem());
        boolean hasFuel = !FueledTool.is(stack) || FueledTool.fuelExceedsCost(context);
        return notOnCooldown && hasFuel;
    }

    private static void doSingularStun(ItemStackContext context, LivingEntity victim, DaggerItem attack, boolean backstab) {
        LivingEntity attacker = context.user();

        context.serverLevel().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), EnderscapeItemSounds.DAGGER_STUN, attacker.getSoundSource(), 1.0F, 1.0F);
        stun(context, victim, attack, backstab);
    }

    private static void stun(ItemStackContext context, LivingEntity victim, DaggerItem attack, boolean backstab) {
        Vec3 pos = victim.position();
        MobEffectInstance effect = new MobEffectInstance(EnderscapeMobEffects.STUNNED, 20);

        if (effect != null) {
            float multiplier = backstab ? 4 : 1;
            int duration = (int) (effect.getDuration() * multiplier);

            victim.addEffect(new MobEffectInstance(effect.getEffect(), duration, effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon()), context.user());
        }

        context.serverLevel().sendParticles(EnderscapeParticles.VOID_POOF.get(), pos.x, pos.y + 0.5, pos.z, 15, 1, 1, 1, 0.1);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) {
        Level level = attacker.level();
        ItemStackContext context = new ItemStackContext(stack, level, attacker);
        DaggerItem attack = DaggerItem.this;

        if (canStun(attacker, context)) {
            boolean backstab = stack.has(BACKSTAB_ANGLE) && EnderscapeAttributes.isBackstab(stack, context.user().position(), victim);

            List<Entity> affectedEntities = List.of(victim);

            doSingularStun(context, victim, attack, backstab);

            useFuel(context);

            if (attacker instanceof ServerPlayer player) {
                EnderscapeCriteria.STUN_ATTACK.trigger(player, stack, victim, affectedEntities, backstab);
            }

            stack.hurtAndBreak(10, attacker, attacker.getEquipmentSlotForItem(stack));
            stack.finishUsingItem(level, attacker);
            if (attacker instanceof Player player) player.getCooldowns().addCooldown(stack.getItem(), 7 * 20);

            return true;
        }

        return false;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity mob, LivingEntity mob2) {
        stack.hurtAndBreak(1, mob2, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack stack2) {
        return super.isValidRepairItem(stack, stack2);
    }

}
