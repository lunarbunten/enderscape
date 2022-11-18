package net.bunten.enderscape.items;

import net.bunten.enderscape.blocks.NebuliteCauldronBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public abstract class NebuliteChargedItem extends Item {

    public static final String ENERGY = "Energy";
    public static final String MAX_ENERGY = "MaximumEnergy";

    public NebuliteChargedItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static boolean is(ItemStack stack) {
        return stack.getItem() instanceof NebuliteChargedItem;
    }

    public static int getEnergy(ItemStack stack) {
        if (stack.getItem() instanceof NebuliteChargedItem item) {
            CompoundTag nbt = stack.getTag();
            return nbt == null ? 0 : Math.max(0, nbt.getInt(ENERGY));
        }
        return 0;
    }

    public static ItemStack setEnergy(ItemStack stack, int value) {
        if (stack.getItem() instanceof NebuliteChargedItem item) {
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putInt(ENERGY, MathUtil.clamp(value, 0, item.getMaximumEnergy(stack)));
        }
        return stack;
    }

    public abstract int getMaximumEnergy(ItemStack stack);

    public static ItemStack setMaximumEnergy(ItemStack stack, int value) {
        if (stack.getItem() instanceof NebuliteChargedItem item) {
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putInt(MAX_ENERGY, Math.max(0, value));
        }
        return stack;
    }

    public abstract int getUseCost(ChargedUsageContext context);

    public static ItemStack removeUseCost(ChargedUsageContext context) {
        ItemStack stack = context.getStack();
        return setEnergy(stack, getEnergy(stack) - context.getChargedItem().getUseCost(context));
    }

    public static boolean canUse(ChargedUsageContext context) {
        if (context.getStack().getItem() instanceof NebuliteChargedItem) {
            return getEnergy(context.getStack()) >= context.getChargedItem().getUseCost(context);
        } else {
            return false;
        }
    }

    public abstract int energyPerGem(ItemStack stack);

    @Environment(EnvType.CLIENT)
    public abstract boolean showUI(ItemStack stack, Player player);

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemCooldowns manager = player.getCooldowns();

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getItemInHand();

        if (!manager.isOnCooldown(this)) {
            if (state.is(EnderscapeBlocks.NEBULITE_CAULDRON)) {
                if (getEnergy(stack) < getMaximumEnergy(stack)) {
                    manager.addCooldown(this, 5);
                    setEnergy(stack, getEnergy(stack) + energyPerGem(stack));
                    NebuliteCauldronBlock.reduceLevel(world, pos, state);
                    world.gameEvent(GameEvent.ITEM_INTERACT_FINISH, player.position(), GameEvent.Context.of(player));
                    world.playSound(null, context.getClickedPos(), EnderscapeSounds.NEBULITE_CAULDRON_DIP, SoundSource.PLAYERS, 0.9F, 1.1F);
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.FAIL;
                }
            } else if (state.is(Blocks.CAULDRON)) {
                return InteractionResult.FAIL;
            }
        }

        return super.useOn(context);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.min(1 + 12 * getEnergy(stack) / getMaximumEnergy(stack), 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF66FF;
    }
}