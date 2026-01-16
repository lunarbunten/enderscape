package net.bunten.enderscape.client.item;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.item.component.Enabled;
import net.bunten.enderscape.item.component.FueledTool;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
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