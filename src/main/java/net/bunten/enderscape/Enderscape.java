package net.bunten.enderscape;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.reflect.Reflection;

import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.criteria.EnderscapeCriteria;
import net.bunten.enderscape.enchantments.EnderscapeEnchantments;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeModifications;
import net.bunten.enderscape.registry.EnderscapeRegistry;
import net.bunten.enderscape.registry.EnderscapeStats;
import net.bunten.enderscape.world.EnderscapeBiomes;
import net.bunten.enderscape.world.EnderscapeFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class Enderscape implements ModInitializer {

    public static final String MOD_ID = "enderscape";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final boolean IS_DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();
    public static final CreativeModeTab TAB = FabricItemGroupBuilder.build(Enderscape.id("all"), () -> new ItemStack(EnderscapeItems.NEBULITE));

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        Reflection.initialize(
            EnderscapeRegistry.class,
            EnderscapeEnchantments.class,
            EnderscapeCriteria.class,
            EnderscapeEntities.class,
            EnderscapeStats.class,
            EnderscapeFeatures.class,
            EnderscapeBiomes.class,
            EnderscapeModifications.class
        );

        Config.save();
        
        LOGGER.info("Enderscape initialized!");
    }
}