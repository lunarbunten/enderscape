package net.bunten.enderscape.items;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.blocks.NebuliteCauldronBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.registry.EnderscapeStats;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Vanishable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class MirrorItem extends Item implements Vanishable {
    private static final int BASE_MAX_ENERGY = 5;
    private static final int BASE_DISTANCE_PER_ENERGY = 500;

    private static final String ENERGY_KEY = "NebuliteCharge";
    private static final String DIMENSION_KEY = "LodestoneDimension";
    private static final String POS_KEY = "LodestonePos";

    public MirrorItem(Settings settings) {
        super(settings.maxCount(1));
    }

    public static boolean isMirror(ItemStack stack) {
        return stack.getItem() instanceof MirrorItem;
    }

    public static int getEnergyLevel(ItemStack stack) {
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

    protected static void setEnergyLevel(ItemStack stack, int value) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(ENERGY_KEY, Math.max(0, MathUtil.clamp(value, 0, getMaximumEnergy(stack))));
    }

    protected static void addEnergy(ItemStack stack, int value) {
        setEnergyLevel(stack, getEnergyLevel(stack) + value);
    }

    private static boolean hasEnoughEnergy(ItemStack stack, PlayerEntity mob) {
        BlockPos pos = getLodestonePos(stack);
        return getEnergyLevel(stack) >= getEnergyCost(stack, mob, pos) || mob.getAbilities().creativeMode;
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
    private static boolean isLodestoneActive(ItemStack stack, ServerWorld world) {
        BlockPos pos = getLodestonePos(stack);
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
        @Nullable String lodeDim = getLodestoneDimension(stack);
        String mobDim = mob.world.getRegistryKey().getValue().toString();

        return lodeDim != null && lodeDim.equals(mobDim);
    }

    /**
     * Calculates the distance betweeen two BlockPos points
     * 
     * @param pos The first position
     * @param pos2 The second position
     */
    private static float getDistance(BlockPos pos, BlockPos pos2) {
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
    public static int getEnergyCost(ItemStack stack, PlayerEntity mob, BlockPos pos) {
        int distance = (int) getDistance(mob.getBlockPos(), pos);
        return mob.getAbilities().creativeMode ? 0 : 1 + distance / getDistancePerEnergy(stack);
    }

    /**
     * Gets the Mirror's Lodestone position
     * 
     * @param stack The Mirror item stack
     */
    @Nullable
    public static BlockPos getLodestonePos(ItemStack stack) {
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
    public static String getLodestoneDimension(ItemStack stack) {
        @Nullable NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            return nbt.getString(DIMENSION_KEY);
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

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return isLinked(stack) || getEnergyLevel(stack) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.min(1 + 12 * getEnergyLevel(stack) / getMaximumEnergy(stack), 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return MathUtil.packRgb(1, 0.4F, 1);
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext ctx) {
        if (isLinked(stack)) {
            BlockPos pos = getLodestonePos(stack);
            if (Screen.hasShiftDown()) {
                tooltip.add(Text.translatable("item.enderscape.mirror.desc.position", pos.getX(), pos.getY(), pos.getZ()).formatted(Formatting.DARK_GREEN));
                tooltip.add(Text.translatable("item.enderscape.mirror.desc.dimension", getLodestoneDimension(stack)).formatted(Formatting.DARK_GREEN));
            } else {
                tooltip.add(Text.translatable(getTranslationKey() + ".desc.unshifted").formatted(Formatting.GRAY));
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static boolean canDisplayUI(ItemStack stack, PlayerEntity mob) {
        return isMirror(stack) && isLinked(stack);
    }

    /**
     * Sends the Player a failure message
     * 
     * @param string The name of the message to send
     * @param mob The player to send the message to
     * @param stack The Mirror item stack
     */
    private TypedActionResult<ItemStack> fail(String string, PlayerEntity mob, ItemStack stack) {
        Util.playSound(mob, EnderscapeSounds.ITEM_MIRROR_FAILURE, 0.65F, MathUtil.nextFloat(mob.getRandom(), 0.9F, 1.1F));
        mob.sendMessage(Text.translatable("item.enderscape.mirror.message." + string).formatted(Formatting.RED), true);
        mob.getItemCooldownManager().set(this, 20);

        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

    /**
     * Goes through the checks to try teleporting with the Mirror
     * 
     * @param world The player's world
     * @param mob The player to teleport
     * @param stack The Mirror item stack
     */
    private TypedActionResult<ItemStack> tryTeleport(ServerWorld world, PlayerEntity mob, ItemStack stack) {
        BlockPos pos = getLodestonePos(stack);

        boolean active = isLodestoneActive(stack, world);
        boolean open = isNotObstructed(world, pos);
        boolean energy = hasEnoughEnergy(stack, mob);

        if (!energy) {
            return fail("need_energy", mob, stack);  
        } else {
            if (!active) {
                return fail("missing", mob, stack);
            } else {
                if (!open) {
                    return fail("obstructed", mob, stack);
                } else {
                    teleport(world, stack, mob, pos);
                    return new TypedActionResult<>(ActionResult.SUCCESS, stack);
                }
            }
        }
    }

    /**
     * Teleport the player to the Lodestone position
     * 
     * @param world The player's world
     * @param mob The player to teleport
     * @param stack The Mirror item stack
     * @param pos The Lodestone position
     */
    private void teleport(ServerWorld world, ItemStack stack, PlayerEntity mob, BlockPos pos) {
        setEnergyLevel(stack, getEnergyLevel(stack) - getEnergyCost(stack, mob, pos));
        mob.fallDistance = 0;

        if (mob.isFallFlying()) {
            mob.stopFallFlying();
        } else if (mob.hasVehicle()) {
            mob.stopRiding();
        }

        if (!isSameDimension(stack, mob)) {
            mob.moveToWorld(world);
        }

        mob.teleport(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, true);
        world.emitGameEvent(GameEvent.TELEPORT, mob.getPos(), GameEvent.Emitter.of(mob));

        Vec3d vec = mob.getPos();

        world.spawnParticles(ParticleTypes.REVERSE_PORTAL, vec.x, vec.y + 0.5, vec.z, 50, 0.5, 1, 0.5, 0.1);
        Util.playSound(mob, EnderscapeSounds.ITEM_MIRROR_TELEPORT, 0.65F, 1);

        mob.getItemCooldownManager().set(this, 10);

        mob.incrementStat(Stats.USED.getOrCreateStat(this));
        mob.incrementStat(EnderscapeStats.MIRROR_TELEPORT);

        if (mob instanceof ServerPlayerEntity server) {
            EnderscapeCriteria.MIRROR_TELEPORT.trigger(server, stack, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity mob, Hand hand) {
        ItemStack stack = mob.getStackInHand(hand);
        if (!world.isClient()) {
            if (isLinked(stack)) {
                if (isSameDimension(stack, mob)) {
                    return tryTeleport((ServerWorld) world, mob, stack);
                } else {
                    return fail("wrong_dimension", mob, stack);
                }
            } else {
                return fail("unlinked", mob, stack);
            }
        } else {
            return new TypedActionResult<>(ActionResult.PASS, stack);
        }
    }

    public ActionResult useOnBlock(ItemUsageContext ctx) {
        PlayerEntity mob = ctx.getPlayer();
        ItemCooldownManager manager = mob.getItemCooldownManager();

        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = ctx.getStack();

        if (state.isIn(EnderscapeBlocks.MIRROR_LODESTONE_BLOCKS)) {
            NbtCompound nbt = stack.getOrCreateNbt();

            nbt.put(POS_KEY, NbtHelper.fromBlockPos(pos));
            nbt.putString(DIMENSION_KEY, world.getRegistryKey().getValue().toString());
            Util.playSound(world, pos, EnderscapeSounds.ITEM_MIRROR_LINK, SoundCategory.PLAYERS, 1, 1);

            return ActionResult.SUCCESS;
        }

        if (!manager.isCoolingDown(this)) {
            if (state.isOf(EnderscapeBlocks.NEBULITE_CAULDRON)) {
                if (getEnergyLevel(stack) < getMaximumEnergy(stack)) {
                    manager.set(this, 5);
                    addEnergy(stack, 1);
                    NebuliteCauldronBlock.reduceLevel(world, pos, state);
                    Util.playSound(mob, EnderscapeSounds.BLOCK_NEBULITE_CAULDRON_DIP, 0.6F, MathUtil.nextFloat(mob.getRandom(), 0.9F, 1.1F));
    
                    return ActionResult.SUCCESS;
                } else {
                    return ActionResult.FAIL;
                }
            } else if (state.isOf(Blocks.CAULDRON)) {
                return ActionResult.FAIL;
            }
        }

        return super.useOnBlock(ctx);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return isLinked(stack) || super.hasGlint(stack);
    }
}