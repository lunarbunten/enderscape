package net.penumbra.enderscape.mixin.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.gui.screens.EnderscapeLevelDataPacksScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {

    @Unique private static final int WARNING_COLOR = 0xFF66FF;
    @Unique private static final int DESCRIPTION_COLOR = 0x00F19F;

    @Unique private static final MutableComponent LOAD_WARNING = Component.translatable("screen.enderscape.load_warning").withStyle(Style.EMPTY.withColor(WARNING_COLOR));
    @Unique private static final MutableComponent LOAD_WARNING_DESCRIPTION = Component.translatable("screen.enderscape.load_warning.description").withStyle(Style.EMPTY.withColor(DESCRIPTION_COLOR));

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract void openWorldLoadBundledResourcePack(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, PackRepository packRepository, Runnable onCancel);

    @Inject(
            method = "openWorldCheckWorldStemCompatibility",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void Enderscape$displayLoadWarning(LevelStorageSource.LevelStorageAccess level, WorldStem stem, PackRepository packRepository, Runnable onCancel, CallbackInfo info) {
        if (EnderscapeConfig.getInstance().vanillaWorldWarning && EnderscapeLevelDataPacksScreen.getDataPacks(level).isEmpty()) {
            Runnable proceedCallback = () -> openWorldLoadBundledResourcePack(level, stem, packRepository, onCancel);

            minecraft.setScreen(new BackupConfirmScreen(
                    () -> {
                            stem.close();
                            level.safeClose();
                            onCancel.run();
                        },
                        (backup, eraseCache) -> EditWorldScreen.conditionallyMakeBackupAndShowToast(backup, level).thenAcceptAsync(_ -> proceedCallback.run(), minecraft),
                        LOAD_WARNING,
                        LOAD_WARNING_DESCRIPTION,
                        false
                    )
            );

            info.cancel();
        }
    }
}