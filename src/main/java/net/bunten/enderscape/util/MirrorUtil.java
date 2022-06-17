package net.bunten.enderscape.util;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.items.MirrorItem;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class MirrorUtil {
    private static final int BASE_MAX_ENERGY = 5;
    private static final int BASE_DISTANCE_PER_ENERGY = 500;

    private static final String ENERGY_KEY = "NebuliteCharge";
    private static final String DIMENSION_KEY = "LodestoneDimension";
    private static final String POS_KEY = "LodestonePos";

    public static boolean isMirror(ItemStack stack) {
        return stack.getItem() instanceof MirrorItem;
    }

    public static int getEnergy(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) {
            return 0;
        } else {
            return nbt.getInt(ENERGY_KEY);
        }
    }

    public static int getMaximumEnergy(ItemStack stack) {
        return BASE_MAX_ENERGY;
    }

    public static int getDistancePerEnergy(ItemStack stack) {
        return BASE_DISTANCE_PER_ENERGY;
    }

    public static void setEnergy(ItemStack stack, int value) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(ENERGY_KEY, Math.max(0, MathUtil.clamp(value, 0, getMaximumEnergy(stack))));
    }

    public static void addEnergy(ItemStack stack, int value) {
        setEnergy(stack, getEnergy(stack) + value);
    }

    public static void removeCost(ItemStack stack, PlayerEntity mob, BlockPos pos) {
        setEnergy(stack, getEnergy(stack) - getCost(stack, mob, pos));
    }

    public static boolean hasEnoughEnergy(ItemStack stack, PlayerEntity mob) {
        BlockPos pos = getPos(stack);
        return getEnergy(stack) >= getCost(stack, mob, pos) || mob.getAbilities().creativeMode;
    }

    /**
     * Checks if the Mirror's lodestone is active
     * 
     * <p>If the block state at the Mirror's lodestone position
     * is a Lodestone, it returns as true.
     * 
     * @param stack The item stack
     * @param world The player's world
     */
    public static boolean isLodestoneActive(ItemStack stack, ServerWorld world) {
        BlockPos pos = getPos(stack);
        BlockState state = world.getBlockState(pos);
        return state.isIn(EnderscapeBlocks.MIRROR_LODESTONE_BLOCKS);
    }

    /**
     * Checks if the Mirror's dimension NBT matches the player's dimension
     * 
     * @param stack The Mirror item stack
     * @param mob The Player holding the item
     */
    public static boolean isSameDimension(ItemStack stack, PlayerEntity mob) {
        return getDimension(stack) != null && getDimension(stack) == mob.getWorld().getRegistryKey();
    }

    /**
     * Calculates the distance betweeen two BlockPos points
     * 
     * @param pos The first position
     * @param pos2 The second position
     */
    public static float getDistance(BlockPos pos, BlockPos pos2) {
        float x = pos.getX() - pos2.getX();
        float z = pos.getZ() - pos2.getZ();
        return MathUtil.sqrt(x * x + z * z);
    }

    /**
     * Calculates the cost to teleport with Mirror
     * 
     * @param stack The Mirror item stack
     * @param mob The player holding the item
     * @param pos The position to teleport to
     */
    public static int getCost(ItemStack stack, PlayerEntity mob, BlockPos pos) {
        int distance = (int) getDistance(mob.getBlockPos(), pos);
        return mob.getAbilities().creativeMode ? 0 : 1 + distance / getDistancePerEnergy(stack);
    }

    /**
     * Gets the Mirror's Lodestone position
     * 
     * @param stack The Mirror item stack
     */
    @Nullable
    public static BlockPos getPos(ItemStack stack) {
        @Nullable NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            return NbtHelper.toBlockPos(nbt.getCompound(POS_KEY));
        } else {
            return null;
        }
    }

    /**
     * Gets the Mirror's Lodestone dimension
     * 
     * @param stack The Mirror item stack
     */
    @Nullable
    public static RegistryKey<World> getDimension(ItemStack stack) {
        @Nullable NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            return RegistryKey.of(Registry.WORLD_KEY, new Identifier(nbt.getString(DIMENSION_KEY)));
        } else {
            return null;
        }
    }

    /**
     * Checks if the Lodestone is obstructed
     * 
     * @param world The player's world
     * @param pos The Lodestone position
     */
    public static boolean isNotObstructed(World world, BlockPos pos) {
        for (int x = 1; world.getBlockState(pos.up(x)).isAir(); ++x) {
            if (x == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if Mirror is linked
     * 
     * @param stack The Mirror item stack
     */
    public static boolean isLinked(ItemStack stack) {
        @Nullable NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            return nbt.contains(DIMENSION_KEY) || nbt.contains(POS_KEY);
        } else {
            return false;
        }
    }

    @Environment(EnvType.CLIENT)
    public static boolean canDisplayUI(ItemStack stack, PlayerEntity mob) {
        return isMirror(stack) && isLinked(stack);
    }

    public static GlobalPos getGlobalPos(ItemStack stack) {
        return GlobalPos.create(getDimension(stack), getPos(stack));
    }

    public static void writeGlobalPos(NbtCompound nbt, GlobalPos globalPos) {
        nbt.put(POS_KEY, NbtHelper.fromBlockPos(globalPos.getPos()));
        nbt.putString(DIMENSION_KEY, globalPos.getDimension().getValue().toString());
    }
}