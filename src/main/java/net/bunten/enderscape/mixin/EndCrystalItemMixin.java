package net.bunten.enderscape.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin extends Item {
    public EndCrystalItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void use(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (state.isIn(EnderscapeBlocks.SUPPORTS_END_CRYSTAL) && state.isFullCube(world, pos)) {
            BlockPos pos2 = pos.up();
            if (!world.isAir(pos2)) {
                info.setReturnValue(ActionResult.FAIL);
            } else {
                double x = pos2.getX();
                double y = pos2.getY();
                double z = pos2.getZ();
                List<Entity> list = world.getOtherEntities(null, new Box(x, y, z, x + 1, y + 2, z + 1));

                if (!list.isEmpty()) {
                    info.setReturnValue(ActionResult.FAIL);
                } else {
                    if (world instanceof ServerWorld) {
                        EndCrystalEntity crystal = new EndCrystalEntity(world, x + 0.5, y, z + 0.5);
                        crystal.setShowBottom(false);
                        world.spawnEntity(crystal);

                        world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, pos2);

                        EnderDragonFight dragonFight = ((ServerWorld)world).getEnderDragonFight();
                        if (dragonFight != null) {
                            dragonFight.respawnDragon();
                        }
                    }

                    context.getStack().decrement(1);
                    info.setReturnValue(ActionResult.success(world.isClient()));
                }
            }
        }
    }
}
