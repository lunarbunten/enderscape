package net.bunten.enderscape.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DaggerItem extends Item {
    public DaggerItem(Item.Properties properties) {
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

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity mob, LivingEntity mob2) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity mob, LivingEntity mob2) {
        stack.hurtAndBreak(1, mob2, EquipmentSlot.MAINHAND);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack stack2) {
        return super.isValidRepairItem(stack, stack2);
    }
}
