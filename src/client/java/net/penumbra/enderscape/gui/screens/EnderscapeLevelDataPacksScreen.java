package net.penumbra.enderscape.gui.screens;

import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.ScrollableLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.SwitchGrid;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import net.minecraft.world.level.storage.WorldData;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnderscapeLevelDataPacksScreen extends Screen {

    public static final Component NAME = Component.translatable("screen.enderscape.datapack_settings");
    private static final Component INFO = Component.translatable("screen.enderscape.datapack_settings.info").withStyle(ChatFormatting.RED);
    private static final Component SAVE_BUTTON = Component.translatable("selectWorld.edit.save");

    private static final Predicate<String> IS_ENDERSCAPE = (string) -> string.contains("enderscape:");

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final Object2BooleanMap<String> dataPacks;
    private final BooleanConsumer callback;
    private final LevelStorageAccess level;

    @Nullable private ScrollableLayout scrollArea;

    public EnderscapeLevelDataPacksScreen(final LevelStorageAccess level, final BooleanConsumer callback) {
        super(NAME);
        this.callback = callback;
        this.level = level;

        dataPacks = getDataPacks(level);
    }

    public static Object2BooleanMap<String> getDataPacks(LevelStorageAccess level) {
        try {
            LevelSettings settings = level.fixAndGetSummary().getSettings();
            DataPackConfig config = settings.dataConfiguration().dataPacks();
            return getDataPacks(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object2BooleanMap<String> getDataPacks(DataPackConfig config) {
        Object2BooleanMap<String> map = new Object2BooleanLinkedOpenHashMap<>();

        map.putAll(Stream.concat(
                config.getEnabled().stream().filter(IS_ENDERSCAPE).map(string -> Map.entry(string, true)),
                config.getDisabled().stream().filter(IS_ENDERSCAPE).map(string -> Map.entry(string, false))
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return map;
    }

    public static EnderscapeLevelDataPacksScreen create(final LevelStorageAccess level, final BooleanConsumer callback) throws IOException {
        return new EnderscapeLevelDataPacksScreen(level, callback);
    }

    @Override
    protected void init() {
        layout.addTitleHeader(NAME, font);
        LinearLayout content = layout.addToContents(LinearLayout.vertical());
        
        content.addChild(new MultiLineTextWidget(INFO, font).setCentered(true).setMaxWidth(340), s -> s.paddingBottom(15));

        SwitchGrid.Builder builder = SwitchGrid.builder(299).withInfoUnderneath(2, true).withRowSpacing(2);
        dataPacks.forEach((pack, selected) -> addSwitch(pack, builder));

        scrollArea = new ScrollableLayout(minecraft, builder.build().layout(), 130);
        scrollArea.setMinWidth(310);
        content.addChild(scrollArea);

        LinearLayout bottomButtonRow = LinearLayout.horizontal().spacing(4);

        bottomButtonRow.addChild(Button.builder(SAVE_BUTTON, button -> {
            try {
                save();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            callback.accept(true);
        }).width(98).build());

        bottomButtonRow.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> callback.accept(false)).width(98).build());

        LinearLayout footer = layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(bottomButtonRow);

        layout.visitWidgets(this::addRenderableWidget);

        repositionElements();
    }

    private void addSwitch(String pack, SwitchGrid.Builder builder) {
        String key = "pack.enderscape." + pack.replace("enderscape:", "");
        builder.addSwitch(
                Component.translatable(key),
                () -> dataPacks.getBoolean(pack),
                newSelected -> dataPacks.put(pack, newSelected.booleanValue())
        ).withInfo(Component.translatable(key + ".description"));
    }

    private void save() throws Exception {
        LevelSettings settings = level.fixAndGetSummary().getSettings();
        DataPackConfig config = settings.dataConfiguration().dataPacks();
        WorldOpenFlows worldOpenFlows = minecraft.createWorldOpenFlows();
        PackRepository packRepository = ServerPacksSource.createPackRepository(level);

        Dynamic<?> dataTag = level.getUnfixedDataTagWithFallback();
        WorldStem worldStem = worldOpenFlows.loadWorldStem(level, dataTag, false, packRepository);
        WorldData worldData = worldStem.worldDataAndGenSettings().data();

        List<String> enabled = new ArrayList<>(config.getEnabled());
        List<String> disabled = new ArrayList<>(config.getDisabled());

        dataPacks.forEach((pack, selected) -> {
            if (selected) {
                disabled.remove(pack);
                if (!enabled.contains(pack)) enabled.add(pack);
            } else {
                enabled.remove(pack);
                if (!disabled.contains(pack)) disabled.add(pack);
            }
        });

        worldData.setDataConfiguration(new WorldDataConfiguration(
                new DataPackConfig(enabled, disabled),
                settings.dataConfiguration().enabledFeatures()
        ));

        level.saveDataTag(worldData);
        onClose();
    }

    @Override
    protected void repositionElements() {
        scrollArea.setMaxHeight(130);
        layout.arrangeElements();
        int availableExtraHeight = height - layout.getFooterHeight() - scrollArea.getRectangle().bottom();
        scrollArea.setMaxHeight(scrollArea.getHeight() + availableExtraHeight);
    }

    @Override
    public void onClose() {
        callback.accept(false);
    }
}