package net.bunten.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.item.component.value.FuelDisplay;
import net.bunten.enderscape.item.component.value.FuelHud;
import net.bunten.enderscape.item.component.value.FuelSounds;
import net.bunten.enderscape.item.component.value.FuelTooltip;
import net.bunten.enderscape.registry.EnderscapeDataComponents;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.CURRENT_FUEL;
import static net.bunten.enderscape.registry.EnderscapeDataComponents.FUELED_TOOL;

public record FueledTool(
        TagKey<Item> fuels,
        int maximumFuel,
        int fuelPerUse,
        FuelDisplay display,
        FuelSounds sounds
) {

    public static final TagKey<Item> DEFAULT_FUELS = EnderscapeItemTags.NEBULITE_TOOL_FUELS;
    public static final int DEFAULT_FUEL_PER_USE = 1;
    public static final FuelDisplay DEFAULT_FUEL_DISPLAY = FuelDisplay.DEFAULT;
    public static final FuelSounds DEFAULT_FUEL_SOUNDS = FuelSounds.DEFAULT;

    public static final Codec<FueledTool> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    TagKey.codec(Registries.ITEM).optionalFieldOf("fuels", DEFAULT_FUELS).forGetter(FueledTool::fuels),
                    ExtraCodecs.POSITIVE_INT.fieldOf("maximum_fuel").forGetter(FueledTool::maximumFuel),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("fuel_per_use", DEFAULT_FUEL_PER_USE).forGetter(FueledTool::fuelPerUse),
                    FuelDisplay.CODEC.optionalFieldOf("display", DEFAULT_FUEL_DISPLAY).forGetter(FueledTool::display),
                    FuelSounds.CODEC.optionalFieldOf("sounds", DEFAULT_FUEL_SOUNDS).forGetter(FueledTool::sounds)
            ).apply(instance, FueledTool::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FueledTool> STREAM_CODEC = StreamCodec.composite(
            streamCodec(Registries.ITEM),
            FueledTool::fuels,
            ByteBufCodecs.INT,
            FueledTool::maximumFuel,
            ByteBufCodecs.INT,
            FueledTool::fuelPerUse,
            FuelDisplay.STREAM_CODEC,
            FueledTool::display,
            FuelSounds.STREAM_CODEC,
            FueledTool::sounds,
            FueledTool::new
    );

    public static <T> StreamCodec<ByteBuf, TagKey<T>> streamCodec(ResourceKey<? extends Registry<T>> key) {
        return ResourceLocation.STREAM_CODEC.map((location) -> TagKey.create(key, location), TagKey::location);
    }

    public static FueledTool simple(int maximumFuel) {
        return Builder.create(maximumFuel).build();
    }

    public static boolean is(ItemStack stack) {
        return stack.has(EnderscapeDataComponents.FUELED_TOOL);
    }

    public static FueledTool get(ItemStack stack) {
        return stack.get(EnderscapeDataComponents.FUELED_TOOL);
    }

    public static int currentFuel(ItemStack stack) {
        return stack.has(CURRENT_FUEL) ? Math.max(0, stack.get(CURRENT_FUEL)) : 0;
    }

    public static void setFuel(ItemStack stack, int value) {
        if (FueledTool.is(stack)) {
            stack.set(CURRENT_FUEL, Mth.clamp(value, 0, FueledTool.get(stack).maximumFuel()));
        } else throw new IllegalStateException(stack.getItem() + " missing component of " + FUELED_TOOL);
    }

    public static int maxFuel(ItemStack stack) {
        if (FueledTool.is(stack)) {
            return Math.max(0, FueledTool.get(stack).maximumFuel());
        } else throw new IllegalStateException(stack.getItem() + " missing component of " + FUELED_TOOL);
    }

    public static TagKey<Item> fuels(ItemStack stack) {
        return FueledTool.get(stack).fuels();
    }

    public static FuelHud hud(ItemStack stack) {
        return FueledTool.get(stack).display().hud();
    }

    public static FuelTooltip tooltip(ItemStack stack) {
        return FueledTool.get(stack).display().tooltip();
    }

    public static boolean tryFuel(ItemStack stack, ItemStack other, ClickAction action, Player player) {
        return tryFuelOnce(stack, other, action, player) || tryFuelCompletely(other, stack, action, player);
    }

    public static boolean tryFuelOnce(ItemStack fueled, ItemStack fuel, ClickAction action, Player player) {
        if (action == ClickAction.PRIMARY && FueledTool.is(fueled) && fuel.is(FueledTool.fuels(fueled))) {
            FueledTool tool = FueledTool.get(fueled);

            if (FueledTool.currentFuel(fueled) < FueledTool.maxFuel(fueled)) {
                FueledTool.setFuel(fueled, FueledTool.currentFuel(fueled) + 1);
                fuel.shrink(1);

                player.playSound(tool.sounds().addFuel().value(), 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            } else {
                player.playSound(tool.sounds().fuelFull().value(), 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            }

            return true;
        }

        return false;
    }

    public static boolean tryFuelCompletely(ItemStack fueled, ItemStack fuel, ClickAction action, Player player) {
        if (action == ClickAction.PRIMARY && FueledTool.is(fueled) && fuel.is(FueledTool.fuels(fueled))) {
            FueledTool tool = FueledTool.get(fueled);

            if (FueledTool.currentFuel(fueled) < FueledTool.maxFuel(fueled)) {
                int added = Math.min(fuel.getCount(), FueledTool.maxFuel(fueled) - FueledTool.currentFuel(fueled));
                FueledTool.setFuel(fueled, FueledTool.currentFuel(fueled) + added);
                fuel.shrink(added);

                player.playSound(tool.sounds().addFuel().value(), 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            } else {
                player.playSound(tool.sounds().fuelFull().value(), 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            }

            return true;
        }

        return false;
    }

    public static int fuelCost(ItemStackContext context) {
        ItemStack stack = context.stack();
        if (FueledTool.is(stack)) {
            if (LodestoneTeleportation.isLinked(stack)) {
                return LodestoneTeleportation.fuelCost(context);
            } else {
                return FueledTool.get(stack).fuelPerUse();
            }
        } else throw new IllegalStateException(stack.getItem() + " missing component of " + FUELED_TOOL);
    }

    public static boolean useFuel(ItemStackContext context) {
        ItemStack stack = context.stack();

        if (FueledTool.is(stack)) {
            boolean hasCounter = ThresholdCounter.is(stack);
            if (hasCounter) ThresholdCounter.increment(stack, 1);

            if (!hasCounter || ThresholdCounter.pastThreshold(context.serverLevel(), stack)) {
                setFuel(stack, currentFuel(stack) - FueledTool.fuelCost(context));

                if (hasCounter) ThresholdCounter.set(stack, 0);
                if (Enabled.is(stack)) Enabled.set(stack, true);

                LivingEntity user = context.user();
                Holder<SoundEvent> useFuelSound = get(stack).sounds().useFuel();

                if (useFuelSound.value() != SoundEvents.EMPTY && user != null) {
                    context.level().playSound(null, user.getX(), user.getY(), user.getZ(), useFuelSound.value(), user.getSoundSource(), 1, 1);
                }

                return true;
            }
        }

        return false;
    }

    public static boolean fuelExceedsCost(ItemStackContext context) {
        ItemStack stack = context.stack();

        if (FueledTool.is(stack)) {
            return currentFuel(stack) >= FueledTool.fuelCost(context);
        } else {
            return true;
        }
    }

    public static class Builder {
        private TagKey<Item> fuels = FueledTool.DEFAULT_FUELS;
        private int maximumFuel;
        private int fuelPerUse = FueledTool.DEFAULT_FUEL_PER_USE;
        private FuelDisplay.Builder fuelDisplayBuilder = FuelDisplay.Builder.create();
        private FuelSounds.Builder fuelSoundsBuilder = FuelSounds.Builder.create();

        public static Builder create(int maximumFuel) {
            Builder builder = new Builder();
            builder.maximumFuel = maximumFuel;
            return builder;
        }

        public Builder fuels(TagKey<Item> fuels) {
            this.fuels = fuels;
            return this;
        }

        public Builder fuelPerUse(int fuelPerUse) {
            this.fuelPerUse = fuelPerUse;
            return this;
        }

        public Builder fuelDisplay(FuelDisplay.Builder fuelDisplayBuilder) {
            this.fuelDisplayBuilder = fuelDisplayBuilder;
            return this;
        }

        public Builder fuelSounds(FuelSounds.Builder fuelSoundsBuilder) {
            this.fuelSoundsBuilder = fuelSoundsBuilder;
            return this;
        }

        public FueledTool build() {
            return new FueledTool(
                    fuels,
                    maximumFuel,
                    fuelPerUse,
                    fuelDisplayBuilder.build(),
                    fuelSoundsBuilder.build()
            );
        }
    }
}