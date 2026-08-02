package net.penumbra.enderscape.gui.components;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.penumbra.enderscape.Enderscape;
import org.jspecify.annotations.Nullable;

public class EnderscapeEditDatapacksButton extends Button.Plain {

    protected EnderscapeEditDatapacksButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, message, onPress, createNarration);
    }

    @Override
    protected void extractContents(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractContents(graphics, mouseX, mouseY, a);

        int size = 12;
        int halfSize = size / 2;
        int height = (getY() + ((getHeight() - size) / 2));

        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                Enderscape.id("widgets/edit_datapacks"),
                getX() + halfSize,
                height,
                size,
                size,
                ARGB.white(alpha)
        );

        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                Enderscape.id("widgets/edit_datapacks"),
                getX() + getWidth() - (size + halfSize),
                height,
                size,
                size,
                ARGB.white(alpha)
        );
    }

    public static EnderscapeEditDatapacksButton.Builder editDatapacksBuilder(final Component message, final OnPress onPress) {
        return new EnderscapeEditDatapacksButton.Builder(message, onPress);
    }

    public static class Builder {
        private final Component message;
        private final OnPress onPress;
        private @Nullable Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private CreateNarration createNarration;

        public Builder(final Component message, final OnPress onPress) {
            createNarration = DEFAULT_NARRATION;
            this.message = message;
            this.onPress = onPress;
        }

        public Builder pos(final int x, final int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(final int width) {
            this.width = width;
            return this;
        }

        public Builder size(final int width, final int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder bounds(final int x, final int y, final int width, final int height) {
            return pos(x, y).size(width, height);
        }

        public Builder tooltip(final @Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder createNarration(final CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        public EnderscapeEditDatapacksButton build() {
            EnderscapeEditDatapacksButton button = new EnderscapeEditDatapacksButton(x, y, width, height, message, onPress, createNarration);
            button.setTooltip(tooltip);
            return button;
        }
    }
}