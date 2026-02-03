package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EnderscapeStats {
    private static final Map<ResourceLocation, StatFormatter> FORMATTERS = new HashMap<>();


    public static final ResourceLocation DRIFTER_BOUNCE = register("drifter_bounce", StatFormatter.DEFAULT);
    public static final ResourceLocation ITEMS_ATTRACTED = register("items_attracted", StatFormatter.DEFAULT);
    public static final ResourceLocation MIRROR_TELEPORT = register("mirror_teleport", StatFormatter.DEFAULT);
    public static final ResourceLocation MIRROR_ONE_CM = register("mirror_one_cm", StatFormatter.DISTANCE);
    public static final ResourceLocation RUBBLE_SHIELD_DASH_ONE_CM = register("rubble_shield_dash_one_cm", StatFormatter.DISTANCE);

    private static ResourceLocation register(String name, StatFormatter formatter) {
        ResourceLocation id = Enderscape.id(name);
        RegistryHelper.register(BuiltInRegistries.CUSTOM_STAT, id, () -> id);
        FORMATTERS.put(id, formatter);
        return id;
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (var entry : FORMATTERS.entrySet()) {
                Stats.CUSTOM.get(entry.getKey(), entry.getValue());
            }
        });
    }
}