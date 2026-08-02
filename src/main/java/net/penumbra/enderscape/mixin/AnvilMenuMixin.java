package net.penumbra.enderscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.registry.tag.EnderscapeItemTags;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.penumbra.enderscape.item.component.FueledTool.*;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition) {
        super(menuType, i, inventory, containerLevelAccess, itemCombinerMenuSlotDefinition);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z", ordinal = 1), method = "createResult")
    public boolean Enderscape$allowCombiningFueledTools(ItemStack stack, Operation<Boolean> original) {
        return (original.call(stack) || is(stack)) && !stack.is(EnderscapeItemTags.CANNOT_COMBINE_IN_ANVIL);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 4), method = "createResult")
    public void Enderscape$combineFuelAmounts(ResultContainer container, int i, ItemStack result, Operation<Void> original) {
        ItemStack input1 = inputSlots.getItem(0).copy();
        ItemStack input2 = inputSlots.getItem(1).copy();
        
        if (is(input1) && is(input2) && is(result)) {
            setFuel(result, Math.min(maxFuel(result), currentFuel(input1) + currentFuel(input2)));
        }

        original.call(container, i, result);
    }
}