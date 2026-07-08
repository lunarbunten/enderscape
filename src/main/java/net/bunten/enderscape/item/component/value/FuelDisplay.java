package net.bunten.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record FuelDisplay(
        int barColor,
        FuelHud hud,
        FuelTooltip tooltip
) {

    public static final int DEFAULT_BAR_COLOR = 0xFF66FF;

    public static final Codec<FuelDisplay> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.optionalFieldOf("bar_color", DEFAULT_BAR_COLOR).forGetter(FuelDisplay::barColor),
                    FuelHud.CODEC.optionalFieldOf("hud", FuelHud.DEFAULT).forGetter(FuelDisplay::hud),
                    FuelTooltip.CODEC.optionalFieldOf("tooltip", FuelTooltip.DEFAULT).forGetter(FuelDisplay::tooltip)
            ).apply(instance, FuelDisplay::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FuelDisplay> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            FuelDisplay::barColor,
            FuelHud.STREAM_CODEC,
            FuelDisplay::hud,
            FuelTooltip.STREAM_CODEC,
            FuelDisplay::tooltip,
            FuelDisplay::new
    );

    public static final FuelDisplay DEFAULT = Builder.create().build();

    public static ResourceLocation segmentOf(ResourceLocation segments, int index) {
        return switch (index) {
            case 0 -> segments.withSuffix("/start");
            case 1 -> segments.withSuffix("/loop");
            case 2 -> segments.withSuffix("/end");
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
    }

    public static class Builder {
        private int barColor = DEFAULT_BAR_COLOR;
        private FuelHud hud = FuelHud.DEFAULT;
        private FuelTooltip tooltip = FuelTooltip.DEFAULT;

        public static Builder create() {
            return new Builder();
        }

        public Builder barColor(int barColor) {
            this.barColor = barColor;
            return this;
        }

        public Builder hud(FuelHud hud) {
            this.hud = hud;
            return this;
        }

        public Builder tooltip(FuelTooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public FuelDisplay build() {
            return new FuelDisplay(
                    barColor,
                    hud,
                    tooltip
            );
        }
    }
}