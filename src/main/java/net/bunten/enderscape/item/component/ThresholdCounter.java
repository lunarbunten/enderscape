package net.bunten.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.registry.EnderscapeEnchantmentEffectComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.mutable.MutableFloat;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.THRESHOLD_COUNTER;

public record ThresholdCounter(
        int value,
        int threshold
) {

    public static final Codec<ThresholdCounter> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("value", 0).forGetter(ThresholdCounter::value),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("threshold").forGetter(ThresholdCounter::threshold)
            ).apply(instance, ThresholdCounter::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ThresholdCounter> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ThresholdCounter::value,
            ByteBufCodecs.INT,
            ThresholdCounter::threshold,
            ThresholdCounter::new
    );

    public static ThresholdCounter of(int threshold) {
        return new ThresholdCounter(0, threshold);
    }

    public static boolean is(ItemStack stack) {
        return stack.has(THRESHOLD_COUNTER);
    }

    public static ThresholdCounter get(ItemStack stack) {
        return stack.get(THRESHOLD_COUNTER);
    }

    public static int value(ItemStack stack) {
        return ThresholdCounter.get(stack).value();
    }

    public static int threshold(ServerLevel level, ItemStack stack) {
        return ThresholdCounter.modifyThreshold(level, stack, ThresholdCounter.get(stack).threshold);
    }

    public static void set(ItemStack stack, int value) {
        ThresholdCounter counter = ThresholdCounter.get(stack);
        stack.set(THRESHOLD_COUNTER, new ThresholdCounter(value, counter.threshold()));
    }

    public static void increment(ItemStack stack, int count) {
        set(stack, ThresholdCounter.value(stack) + count);
    }

    public static boolean pastThreshold(ServerLevel level, ItemStack stack) {
        ThresholdCounter counter = ThresholdCounter.get(stack);
        return counter.value() >= ThresholdCounter.threshold(level, stack);
    }

    public static int modifyThreshold(ServerLevel level, ItemStack stack, float value) {
        MutableFloat mutable = new MutableFloat(value);
        EnchantmentHelper.runIterationOnItem(stack, (holder, i) -> holder.value().modifyItemFilteredCount(EnderscapeEnchantmentEffectComponents.INTEGER_COUNTER_THRESHOLD.get(), level, i, stack, mutable));
        return Math.max(0, (int) mutable.floatValue());
    }
}