package net.enderscape.items;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.enderscape.blocks.NebuliteCauldronBlock;
import net.enderscape.interfaces.SendsMessage;
import net.enderscape.registry.EndBlocks;
import net.enderscape.registry.EndCriteria;
import net.enderscape.registry.EndSounds;
import net.enderscape.util.EndMath;
import net.enderscape.util.Util;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MirrorItem extends Item implements SendsMessage, Vanishable {
    public MirrorItem(Settings settings) {
        super(settings.maxCount(1));
    }

    public static int getCharge(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt == null ? 0 : nbt.getInt("NebuliteCharge");
    }

    public static int getMaxCharge(ItemStack stack) {
        return 5;
    }

    private static void setCharge(ItemStack stack, int value) {
        stack.getOrCreateNbt().putInt("NebuliteCharge", Math.max(0, EndMath.clamp(value, 0, getMaxCharge(stack))));
    }

    private static void addCharge(ItemStack stack, int value) {
        setCharge(stack, getCharge(stack) + value);
    }

    private static boolean hasEnoughCharge(ItemStack stack, PlayerEntity mob) {
        BlockPos pos = getLodestonePos(stack);
        return getCharge(stack) >= getTeleportCost(stack, mob, pos) || mob.getAbilities().creativeMode;
    }

    // Checks if the lodestone is not missing
    private static boolean isLodestoneActive(ItemStack stack, ServerWorld world) {
        BlockPos pos = getLodestonePos(stack);
        BlockState state = world.getBlockState(pos);
        return state.isOf(Blocks.LODESTONE);
    }

    // Checks if the lodestone is in the right dimension
    private static boolean isSameDimension(ItemStack stack, PlayerEntity mob) {
        @Nullable String lodeDim = getLodestoneDimension(stack);
        String mobDim = mob.world.getRegistryKey().getValue().toString();

        return lodeDim != null && lodeDim.equals(mobDim);
    }

    // Get distance between two points
    private static float getDistance(BlockPos pos, BlockPos pos2) {
        float x = pos.getX() - pos2.getX();
        float z = pos.getZ() - pos2.getZ();
        return EndMath.sqrt(x * x + z * z);
    }

    // Calculates the cost of teleportation based on distances and other factors
    public static int getTeleportCost(ItemStack stack, PlayerEntity mob, BlockPos pos) {
        int extra = isSameDimension(stack, mob) ? 1 : 5;
        int cost = extra + ((int) getDistance(mob.getBlockPos(), pos) / 500);
        return mob.getAbilities().creativeMode ? 0 : cost;
    }

    // Get the lodestone position
    @Nullable
    public static BlockPos getLodestonePos(ItemStack stack) {
        @Nullable NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            return NbtHelper.toBlockPos(nbt.getCompound("LodestonePos"));
        } else {
            return null;
        }
    }

    // Get lodestone dimension
    @Nullable
    public static String getLodestoneDimension(ItemStack stack) {
        @Nullable NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            return nbt.getString("LodestoneDimension");
        } else {
            return null;
        }
    }

    public static boolean isNotObstructed(World world, BlockPos pos) {
        for (int x = 1; world.getBlockState(pos.up(x)).isAir(); ++x) {
            if (x == 2) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLinked(ItemStack stack) {
        @Nullable NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            return nbt.contains("LodestoneDimension") || nbt.contains("LodestonePos");
        } else {
            return false;
        }
    }

    @Override
    public int getEnchantability() {
        return 1;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.min(1 + 12 * getCharge(stack) / getMaxCharge(stack), 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return EndMath.packRgb(1, 0.4F, 1);
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext ctx) {
        if (isLinked(stack)) {
            BlockPos pos = getLodestonePos(stack);
            if (Screen.hasShiftDown()) {
                tooltip.add(new TranslatableText("item.enderscape.mirror.desc.position", pos.getX(), pos.getY(), pos.getZ()).formatted(Formatting.DARK_GREEN));
                tooltip.add(new TranslatableText("item.enderscape.mirror.desc.dimension", getLodestoneDimension(stack)).formatted(Formatting.DARK_GREEN));
            } else {
                tooltip.add(new TranslatableText(getTranslationKey() + ".desc.unshifted").formatted(Formatting.GRAY));
            }
        }
    }

    // Send player the failure message
    private void fail(String string, PlayerEntity mob) {
        Util.playSound(mob, EndSounds.ITEM_MIRROR_FAILURE, 0.65F, EndMath.nextFloat(mob.getRandom(), 0.9F, 1.1F));
        mob.sendMessage(new TranslatableText("item.enderscape.mirror.message." + string).formatted(Formatting.RED), true);
        mob.getItemCooldownManager().set(this, 20);
    }

    // Go through all the checks before teleportation
    private void tryTeleport(ServerWorld world, PlayerEntity mob, ItemStack stack) {
        BlockPos pos = getLodestonePos(stack);

        boolean active = isLodestoneActive(stack, world);
        boolean open = isNotObstructed(world, pos);
        boolean charge = hasEnoughCharge(stack, mob);

        if (active && open && charge) {
            teleport(world, stack, mob, pos);
        } else if (active && open) {
            fail("no_levels", mob);
        } else if (active) {
            fail("obstructed", mob);
        } else {
            fail("missing", mob);
        }
    }

    // Actually teleport
    private void teleport(ServerWorld world, ItemStack stack, PlayerEntity mob, BlockPos pos) {
        setCharge(stack, getCharge(stack) - getTeleportCost(stack, mob, pos));
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

        Vec3d vec = mob.getPos();
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL, vec.x, vec.y + 0.5, vec.z, 50, 0.5, 1, 0.5, 0.1);
        Util.playSound(mob, EndSounds.ITEM_MIRROR_TELEPORT, 0.65F, 1);

        mob.getItemCooldownManager().set(this, 10);

        if (mob instanceof ServerPlayerEntity server) {
            EndCriteria.MIRROR_TELEPORT.trigger(server, stack, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity mob, Hand hand) {
        ItemStack stack = mob.getStackInHand(hand);
        if (!world.isClient()) {
            boolean same = isSameDimension(stack, mob);
            if (isLinked(stack)) {
                if (same) {
                    tryTeleport((ServerWorld) world, mob, stack);
                } else {
                    fail("wrong_dimension", mob);
                }
            } else {
                fail("unlinked", mob);
            }
            return new TypedActionResult<>(ActionResult.SUCCESS, stack);
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

        if (state.isOf(Blocks.LODESTONE)) {
            NbtCompound nbt = stack.getOrCreateNbt();

            nbt.put("LodestonePos", NbtHelper.fromBlockPos(pos));
            nbt.putString("LodestoneDimension", world.getRegistryKey().getValue().toString());
            Util.playSound(world, pos, EndSounds.ITEM_MIRROR_LINK, SoundCategory.PLAYERS, 1, 1);
            return ActionResult.SUCCESS;
        } else if (state.isOf(EndBlocks.NEBULITE_CAULDRON) && !manager.isCoolingDown(this)) {
            if (getCharge(stack) < getMaxCharge(stack)) {
                manager.set(this, 5);
                addCharge(stack, 1);
                NebuliteCauldronBlock.reduceLevel(world, pos, state);
                Util.playSound(mob, EndSounds.BLOCK_NEBULITE_CAULDRON_DIP, 0.6F, EndMath.nextFloat(mob.getRandom(), 0.9F, 1.1F));
                return ActionResult.SUCCESS;
            } else {
                fail("full_charge", mob);
                return ActionResult.SUCCESS;
            }
        } else {
            return super.useOnBlock(ctx);
        }
    }

    public String getTranslationKey(ItemStack stack) {
        return isLinked(stack) ? getTranslationKey() + ".linked" : super.getTranslationKey(stack);
    }

    @Override
    public boolean canDisplay(ItemStack stack, PlayerEntity mob) {
        return isLinked(stack) && !mob.getAbilities().creativeMode;
    }

    @Override
    public TranslatableText getMessage(ItemStack stack, PlayerEntity mob) {
        BlockPos pos = MirrorItem.getLodestonePos(stack);
        if (pos != null) {
            return new TranslatableText("item.enderscape.mirror.message.cost", MirrorItem.getTeleportCost(stack, mob, pos));
        } else {
            return null;
        }
    }

    @Override
    public int getColor(ItemStack stack, PlayerEntity mob) {
        return hasEnoughCharge(stack, mob) ? 0xDF5CFF : 0xE55757;
    }
}