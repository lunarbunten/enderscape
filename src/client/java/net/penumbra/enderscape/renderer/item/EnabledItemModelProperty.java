package net.penumbra.enderscape.renderer.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.item.ItemStackContext;
import net.penumbra.enderscape.item.component.Enabled;
import net.penumbra.enderscape.item.component.FueledTool;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record EnabledItemModelProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<EnabledItemModelProperty> MAP_CODEC = MapCodec.unit(new EnabledItemModelProperty());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity living, int i, ItemDisplayContext context) {
        return Enabled.get(stack) && FueledTool.fuelExceedsCost(new ItemStackContext(stack, level, living));
    }

    @Override
    public MapCodec<EnabledItemModelProperty> type() {
        return MAP_CODEC;
    }
}