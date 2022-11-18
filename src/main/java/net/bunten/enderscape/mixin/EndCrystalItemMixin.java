package net.bunten.enderscape.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.EndCrystalItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin extends Item {
    public EndCrystalItemMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> info) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);

        if (state.is(EnderscapeBlocks.SUPPORTS_END_CRYSTAL) && state.isCollisionShapeFullBlock(world, pos)) {
            BlockPos pos2 = pos.above();
            if (!world.isEmptyBlock(pos2)) {
                info.setReturnValue(InteractionResult.FAIL);
            } else {
                double x = pos2.getX();
                double y = pos2.getY();
                double z = pos2.getZ();
                List<Entity> list = world.getEntities(null, new AABB(x, y, z, x + 1, y + 2, z + 1));

                if (!list.isEmpty()) {
                    info.setReturnValue(InteractionResult.FAIL);
                } else {
                    if (world instanceof ServerLevel server) {
                        EndCrystal crystal = new EndCrystal(world, x + 0.5, y, z + 0.5);
                        crystal.setShowBottom(false);
                        world.addFreshEntity(crystal);

                        world.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, pos2);

                        EndDragonFight dragonFight = server.dragonFight();
                        if (dragonFight != null) {
                            dragonFight.tryRespawn();
                        }
                    }

                    context.getItemInHand().shrink(1);
                    info.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide()));
                }
            }
        }
    }
}
