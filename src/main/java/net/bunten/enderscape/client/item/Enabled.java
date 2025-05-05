package net.bunten.enderscape.client.item;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.item.MagniaAttractorItem;
import net.bunten.enderscape.item.NebuliteToolContext;
import net.bunten.enderscape.item.NebuliteToolItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record Enabled() implements ConditionalItemModelProperty {
    public static final MapCodec<Enabled> MAP_CODEC = MapCodec.unit(new Enabled());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity living, int i, ItemDisplayContext context) {
        return MagniaAttractorItem.isEnabled(stack) && NebuliteToolItem.fuelExceedsCost(new NebuliteToolContext(stack, level, living));
    }

    @Override
    public MapCodec<Enabled> type() {
        return MAP_CODEC;
    }
}