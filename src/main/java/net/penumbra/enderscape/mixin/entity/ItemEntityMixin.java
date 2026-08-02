package net.penumbra.enderscape.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.entity.magnia.MagniaAffected;
import net.penumbra.enderscape.item.component.EntityMagnet;
import net.penumbra.enderscape.registry.entity.EnderscapeStats;
import net.penumbra.enderscape.registry.tag.EnderscapeFluidTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getItem();

    @Shadow
    private int pickupDelay;
    @Shadow
    private @Nullable UUID target;

    @Unique
    private final ItemEntity entity = (ItemEntity) (Object) this;

    public ItemEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    // FAPI Fluid API doesn't have something for this?

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;isInWater()Z"))
    private boolean Enderscape$isInVoidLachryma(boolean original) {
        return original || getFluidHeight(EnderscapeFluidTags.VOID_LACHRYMA) > 0.0;
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getFluidHeight(Lnet/minecraft/tags/TagKey;)D", ordinal = 0))
    private double Enderscape$getVoidLachrymaHeight(double original) {
        return Math.max(original, getFluidHeight(EnderscapeFluidTags.VOID_LACHRYMA));
    }

    @Inject(at = @At("HEAD"), method = "playerTouch", cancellable = true)
    private void Enderscape$playerTouch(Player player, CallbackInfo info) {
        if (!level().isClientSide()) {
            ItemStack stack = getItem();
            int count = stack.getCount();

            if (pickupDelay == 0 && (target == null || target.equals(player.getUUID())) && EntityMagnet.tryAddToBundle(player.getInventory(), stack)) {
                player.take(entity, count);

                if (stack.isEmpty()) {
                    discard();
                    stack.setCount(count);
                }

                player.awardStat(Stats.ITEM_PICKED_UP.get(stack.getItem()), count);
                player.onItemPickup(entity);

                info.cancel();
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;I)V", shift = At.Shift.AFTER), method = "playerTouch")
    private void Enderscape$awardItemsPulledStat(Player player, CallbackInfo info) {
        if (player instanceof ServerPlayer server && MagniaAffected.wasMoved(entity)) {
            server.awardStat(EnderscapeStats.ITEMS_ATTRACTED, getItem().getCount());
        }
    }
}