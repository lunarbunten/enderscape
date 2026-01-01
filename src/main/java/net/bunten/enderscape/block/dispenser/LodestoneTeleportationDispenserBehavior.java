package net.bunten.enderscape.block.dispenser;

import net.bunten.enderscape.item.LodestoneTeleportationCheck;
import net.bunten.enderscape.item.LodestoneTrackerContext;
import net.bunten.enderscape.item.component.LodestoneTeleportation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

import static net.bunten.enderscape.registry.tag.EnderscapeEntityTags.BLACKLISTED_FROM_MIRROR_IN_DISPENSER_TELEPORTATION;

public class LodestoneTeleportationDispenserBehavior extends OptionalDispenseItemBehavior {

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        ServerLevel level = source.level();
        LodestoneTeleportation.updateLodestoneTracker(stack, level);

        if (!level.isClientSide()) {
            BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
            for (LivingEntity mob : level.getEntitiesOfClass(LivingEntity.class, new AABB(pos), EntitySelector.NO_SPECTATORS.and(entity -> !entity.getType().is(BLACKLISTED_FROM_MIRROR_IN_DISPENSER_TELEPORTATION)))) {
                setSuccess(tryTeleport(stack, mob, level));
                break;
            }
        }

        return stack;
    }

    private static boolean tryTeleport(ItemStack stack, LivingEntity mob, ServerLevel level) {
        if (mob instanceof Player player && player.getCooldowns().isOnCooldown(stack)) return false;
        LodestoneTrackerContext tracker = new LodestoneTrackerContext(stack, level, mob);
        for (LodestoneTeleportationCheck check : LodestoneTeleportationCheck.CHECKS_IN_ORDER) if (check.fails(tracker)) return false;
        return LodestoneTeleportation.teleport(tracker, true);
    }
}