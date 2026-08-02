package net.penumbra.enderscape.renderer.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import org.jspecify.annotations.Nullable;

public class RubbleShieldVariantItemModelProperty implements SelectItemModelProperty<Identifier> {
    public static final Codec<Identifier> VALUE_CODEC = Identifier.CODEC;
    public static final SelectItemModelProperty.Type<RubbleShieldVariantItemModelProperty, Identifier> TYPE = Type.create(MapCodec.unit(new RubbleShieldVariantItemModelProperty()), VALUE_CODEC);

    @Nullable
    public Identifier get(final ItemStack stack, @Nullable final ClientLevel level, @Nullable final LivingEntity owner, final int seed, final ItemDisplayContext context) {
        return stack.get(EnderscapeDataComponents.RUBBLE_SHIELD_VARIANT);
    }

    @Override
    public SelectItemModelProperty.Type<RubbleShieldVariantItemModelProperty, Identifier> type() {
        return TYPE;
    }

    @Override
    public Codec<Identifier> valueCodec() {
        return VALUE_CODEC;
    }
}
