package net.enderscape.mixin;

import net.enderscape.registry.EndBlocks;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin extends Item {
    public EndCrystalItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void use(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(EndBlocks.SHADOWSTEEL_PILLAR)) {
            BlockPos blockPos2 = blockPos.up();
            if (!world.isAir(blockPos2)) {
                cir.setReturnValue(ActionResult.FAIL);
            } else {
                double d = blockPos2.getX();
                double e = blockPos2.getY();
                double f = blockPos2.getZ();
                List<Entity> list = world.getOtherEntities(null, new Box(d, e, f, d + 1.0D, e + 2.0D, f + 1.0D));
                if (!list.isEmpty()) {
                    cir.setReturnValue(ActionResult.FAIL);
                } else {
                    if (world instanceof ServerWorld) {
                        EndCrystalEntity endCrystalEntity = new EndCrystalEntity(world, d + 0.5D, e, f + 0.5D);
                        endCrystalEntity.setShowBottom(false);
                        world.spawnEntity(endCrystalEntity);
                        world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos2);
                        EnderDragonFight enderDragonFight = ((ServerWorld)world).getEnderDragonFight();
                        if (enderDragonFight != null) {
                            enderDragonFight.respawnDragon();
                        }
                    }

                    context.getStack().decrement(1);
                    cir.setReturnValue(ActionResult.success(world.isClient()));
                }
            }
        }
    }
}
