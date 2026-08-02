package net.penumbra.enderscape.mixin.client.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.gui.components.EnderscapeEditDatapacksButton;
import net.penumbra.enderscape.gui.screens.EnderscapeLevelDataPacksScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;

@Environment(EnvType.CLIENT)
@Mixin(EditWorldScreen.class)
public abstract class EditWorldScreenMixin {

    @Shadow
    @Final
    private LevelStorageSource.LevelStorageAccess levelAccess;

    @Shadow
    @Final
    private BooleanConsumer callback;

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/layouts/LinearLayout;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;",
                    ordinal = 9
            )
    )
    public <T extends LayoutElement> T Enderscape$addDataPacksButton(LinearLayout parent, T child, Operation<T> original) {
        final T called = original.call(parent, child);

        if (EnderscapeConfig.getInstance().editWorldEnderscapeDataPacksButton) {
            final Minecraft minecraft = Minecraft.getInstance();

            try {
                LevelSettings settings = levelAccess.fixAndGetSummary().getSettings();
                DataPackConfig config = settings.dataConfiguration().dataPacks();

                EnderscapeEditDatapacksButton button = EnderscapeEditDatapacksButton.editDatapacksBuilder(EnderscapeLevelDataPacksScreen.NAME, _ -> {
                            try {
                                minecraft.setScreenAndShow(EnderscapeLevelDataPacksScreen.create(levelAccess, callback));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).width(200).build();

                button.active = !EnderscapeLevelDataPacksScreen.getDataPacks(config).isEmpty();

                if (!button.isActive()) {
                    button.setTooltip(Tooltip.create(Component.translatable("screen.enderscape.datapack_settings.unavailable")));
                }

                parent.addChild(button);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return called;
    }
}