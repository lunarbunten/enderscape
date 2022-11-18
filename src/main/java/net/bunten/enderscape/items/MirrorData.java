package net.bunten.enderscape.items;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.enchantments.EnderscapeEnchantments;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MirrorData {

    private static final String POS = "LinkedPos";
    private static final String DIMENSION = "LinkedDimension";
    private static final String LINKED_BLOCK = "LinkedBlock";
    private static final String COST_INCREASE_DISTANCE = "CostIncreaseDistance";

    public static int getLightspeed(ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnderscapeEnchantments.LIGHTSPEED, stack);
    }

    public static boolean isTransdimensional(ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnderscapeEnchantments.TRANSDIMENSIONAL, stack) > 0;
    }

    public static void writeData(ItemStack stack, BlockPos pos, ResourceKey<Level> resourceKey, BlockState state) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.put(POS, NbtUtils.writeBlockPos(pos));
        nbt.putString(DIMENSION, resourceKey.location().toString());
        nbt.putString(LINKED_BLOCK, Registry.BLOCK.getKey(state.getBlock()).toString());
    }

    public static boolean isLinkable(BlockState state) {
        return state.is(EnderscapeBlocks.MIRROR_LINKABLE_BLOCKS);
    }

    public static boolean isLinked(ItemStack stack) {
        return stack.getTag() != null ? stack.getTag().contains(DIMENSION) || stack.getTag().contains(POS) : false;
    }

    public static boolean canTravelTo(MirrorUsageContext context) {
        return isTransdimensional(context.getStack()) ? true : isSameDimension(context);
    }

    public static boolean isSameDimension(ItemStack stack, ResourceKey<Level> intiial) {
        return dimension(stack) != null && intiial == dimension(stack);
    }

    public static boolean isSameDimension(MirrorUsageContext context) {
        return context.getUsageDimension() == context.getLinkedDimension();
    }

    public static boolean isTooFar(MirrorUsageContext context) {
        return context.getChargedItem().getUseCost(context) > context.getMirrorItem().getMaximumEnergy(context.getStack());
    }

    public static boolean isLinkedBlockActive(MirrorUsageContext context) {
        ItemStack stack = context.getStack();
        BlockState state = context.getLinkedLevel().getBlockState(context.getLinkedPos());
        if (state.getBlock() == linkedBlock(stack)) {
            return true;
        } else {
            if (isLinkable(state)) {
                writeData(stack, context.getLinkedPos(), context.getLinkedDimension(), state);
                return state.getBlock() == linkedBlock(stack);
            } else {
                return false;
            }
        }
    }

    public static boolean isObstructed(MirrorUsageContext context) {
        ServerLevel level = context.getLinkedLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getLinkedPos();
        Iterable<VoxelShape> shapes = level.getBlockCollisions(player, new AABB(pos.above(), pos.above().offset(1, 2, 1)));
        for (VoxelShape shape : shapes) {
            if (!shape.isEmpty()) return true;
        }
        return false;
    }

    @Nullable
    public static BlockPos pos(ItemStack stack) {
        if (stack.getTag() == null) {
            return null;
        } else {
            return Optional.ofNullable(NbtUtils.readBlockPos(stack.getTag().getCompound(POS))).orElse(null);
        }
    }

    @Nullable
    public static ResourceKey<Level> dimension(ItemStack stack) {
        if (stack.getTag() == null) {
            return null;
        } else {
            return Optional.ofNullable(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(stack.getTag().getString(DIMENSION)))).orElse(null);
        }
    }

    @Nullable
    public static Block linkedBlock(ItemStack stack) {
        if (stack.getTag() == null) {
            return null;
        } else {
            return Optional.ofNullable(Registry.BLOCK.get(new ResourceLocation(stack.getTag().getString(LINKED_BLOCK)))).orElse(null);
        }
    }

    @Nullable
    public static MutableComponent linkedBlockName(ItemStack stack) {
        return linkedBlock(stack) != null ? linkedBlock(stack).getName() : null;
    }

    public static int costIncreaseDistance(ItemStack stack) {
        return baseCostIncreaseDistance(stack) + (getLightspeed(stack) * baseCostIncreaseDistance(stack));
    }

    public static int baseCostIncreaseDistance(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains(COST_INCREASE_DISTANCE)) {
            return Math.max(1, stack.getTag().getInt(COST_INCREASE_DISTANCE));
        } else {
            return 500;
        }
    }

    public static ItemStack setCostIncreaseDistance(ItemStack stack, int value) {
        stack.getOrCreateTag().putInt(COST_INCREASE_DISTANCE, Math.max(1, value));
        return stack;
    }

    public static float horizontalDistance(BlockPos pos, BlockPos pos2) {
        float x = pos.getX() - pos2.getX();
        float z = pos.getZ() - pos2.getZ();
        return MathUtil.sqrt(x * x + z * z);
    }

    public static int roundedHorizontalDistance(BlockPos pos, BlockPos pos2) {
        return (int) horizontalDistance(pos, pos2);
    }
}