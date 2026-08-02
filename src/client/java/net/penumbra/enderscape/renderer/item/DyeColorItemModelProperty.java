package net.penumbra.enderscape.renderer.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import org.jspecify.annotations.Nullable;

public class DyeColorItemModelProperty implements SelectItemModelProperty<DyeColor> {
    public static final Codec<DyeColor> VALUE_CODEC = DyeColor.CODEC;
    public static final Type<DyeColorItemModelProperty, DyeColor> TYPE = Type.create(MapCodec.unit(new DyeColorItemModelProperty()), VALUE_CODEC);

    @Nullable
    public DyeColor get(final ItemStack stack, @Nullable final ClientLevel level, @Nullable final LivingEntity owner, final int seed, final ItemDisplayContext context) {
        return stack.get(EnderscapeDataComponents.DYE_COLOR);
    }

    @Override
    public Type<DyeColorItemModelProperty, DyeColor> type() {
        return TYPE;
    }

    @Override
    public Codec<DyeColor> valueCodec() {
        return VALUE_CODEC;
    }
}
