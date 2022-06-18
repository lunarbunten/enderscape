package net.bunten.enderscape.items;

import java.util.List;

import net.bunten.enderscape.blocks.NebuliteCauldronBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.registry.EnderscapeStats;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.MirrorUtil;
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
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class MirrorItem extends Item implements Vanishable {

    public MirrorItem(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return MirrorUtil.isLinked(stack) || super.hasGlint(stack);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return MirrorUtil.isLinked(stack) || MirrorUtil.getEnergy(stack) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.min(1 + 12 * MirrorUtil.getEnergy(stack) / MirrorUtil.getMaximumEnergy(stack), 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xFF66FF;
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext ctx) {
        if (MirrorUtil.isLinked(stack)) {
            BlockPos pos = MirrorUtil.getPos(stack);
            if (Screen.hasShiftDown()) {
                var dimensionID = Text.translatable("dimension." + MirrorUtil.getDimension(stack).getValue().toTranslationKey());
                tooltip.add(Text.translatable("item.enderscape.mirror.desc.position", pos.getX(), pos.getY(), pos.getZ()).formatted(Formatting.DARK_GREEN));
                tooltip.add(Text.translatable("item.enderscape.mirror.desc.dimension", dimensionID).formatted(Formatting.DARK_GREEN));
            } else {
                tooltip.add(Text.translatable(getTranslationKey() + ".desc.unshifted").formatted(Formatting.GRAY));
            }
        }
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
        BlockPos pos = MirrorUtil.getPos(stack);

        boolean active = MirrorUtil.isLodestoneActive(stack, world);
        boolean open = MirrorUtil.isNotObstructed(world, pos);
        boolean energy = MirrorUtil.hasEnoughEnergy(stack, mob);

        if (!energy) {
            return fail("need_energy", mob, stack);  
        } else {
            if (!active) {
                return fail("missing", mob, stack);
            } else {
                if (!open) {
                    return fail("obstructed", mob, stack);
                } else {
                    return teleport(world, stack, mob, pos);
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
    private TypedActionResult<ItemStack> teleport(ServerWorld world, ItemStack stack, PlayerEntity mob, BlockPos pos) {
        MirrorUtil.removeCost(stack, mob, pos);
        mob.fallDistance = 0;

        if (mob.isFallFlying()) {
            mob.stopFallFlying();
        } else if (mob.hasVehicle()) {
            mob.stopRiding();
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

        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity mob, Hand hand) {
        ItemStack stack = mob.getStackInHand(hand);
        if (!world.isClient()) {
            if (MirrorUtil.isLinked(stack)) {
                if (MirrorUtil.isSameDimension(stack, mob)) {
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

        GlobalPos globalPos = GlobalPos.create(ctx.getWorld().getRegistryKey(), ctx.getBlockPos());

        World world = ctx.getWorld();
        BlockPos pos = globalPos.getPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = ctx.getStack();

        if (state.isIn(EnderscapeBlocks.MIRROR_LODESTONE_BLOCKS)) {
            MirrorUtil.writeGlobalPos(stack.getOrCreateNbt(), globalPos);
            Util.playSound(world, pos, EnderscapeSounds.ITEM_MIRROR_LINK, SoundCategory.PLAYERS, 1, 1);

            return ActionResult.SUCCESS;
        }

        if (!manager.isCoolingDown(this)) {
            if (state.isOf(EnderscapeBlocks.NEBULITE_CAULDRON)) {
                if (MirrorUtil.getEnergy(stack) < MirrorUtil.getMaximumEnergy(stack)) {
                    manager.set(this, 5);
                    MirrorUtil.addEnergy(stack, 1);
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
}