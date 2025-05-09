package net.bunten.enderscape.item;

import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import org.jetbrains.annotations.Nullable;

public class RustleBucketItem extends Item implements DispensibleContainerItem {

    private final EntityType<?> type;
    
    public RustleBucketItem(Properties properties) {
        super(properties);

        this.type = EnderscapeEntities.RUSTLE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (result.getType() == Type.MISS) {
            return InteractionResultHolder.success(stack);
        } else if (result.getType() != Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        } else {
            BlockPos pos = result.getBlockPos();
            if (level.mayInteract(player, pos) && player.mayUseItemAt(pos.relative(result.getDirection()), result.getDirection(), stack)) {
                BlockPos relative = pos.relative(result.getDirection());
                if (emptyContents(player, level, relative, result)) {
                    checkExtraContent(player, level, stack, relative);
                    level.playSound(player, pos, EnderscapeItemSounds.RUSTLE_BUCKET_EMPTY, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.success(ItemUtils.createFilledResult(stack, player, !player.hasInfiniteMaterials() ? new ItemStack(Items.BUCKET) : stack));
                }
            }
            return InteractionResultHolder.fail(stack);
        }
    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level level, ItemStack stack, BlockPos pos) {
        if (level instanceof ServerLevel server) {
            Entity entity = type.create(server, EntityType.createDefaultStackConfig(server, stack, null), pos, MobSpawnType.BUCKET, true, false);

            if (entity instanceof Bucketable bucketable) {
                bucketable.loadFromBucketTag(stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag());
                bucketable.setFromBucket(true);
            }

            if (entity != null) server.addFreshEntityWithPassengers(entity);

            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    @Override
    public boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult result) {
        BlockState state = level.getBlockState(pos);

        if (!state.isAir() && !state.canBeReplaced(Fluids.EMPTY)) {
            return result != null && emptyContents(player, level, result.getBlockPos().relative(result.getDirection()), null);
        }

        return true;
    }
}