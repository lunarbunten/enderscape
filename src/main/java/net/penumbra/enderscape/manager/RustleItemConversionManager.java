package net.penumbra.enderscape.manager;

import com.google.common.base.Stopwatch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.penumbra.enderscape.item.crafting.RustleRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class RustleItemConversionManager {

    private static final Map<Ingredient, List<RustleRecipe>> RECIPE_CACHE = new IdentityHashMap<>();
    private static final Logger LOGGER = LogManager.getLogger(RustleItemConversionManager.class);

    public static void onServerStarting(MinecraftServer server) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        RECIPE_CACHE.clear();

        Map<Ingredient, List<RustleRecipe>> map = new IdentityHashMap<>();
        Collection<RecipeHolder<RustleRecipe>> recipes = server.getRecipeManager().getAllOfType(RustleRecipe.TYPE);

        recipes.forEach(conversion -> map.computeIfAbsent(conversion.value().input(), _ -> new ArrayList<>()).add(conversion.value()));
        map.forEach((item, list) -> RECIPE_CACHE.put(item, Collections.unmodifiableList(list)));

        LOGGER.info("Cached {} Rustle item recipes in {}", RECIPE_CACHE.size(), stopwatch);
    }

    public static void onServerStopping(MinecraftServer server) {
        RECIPE_CACHE.clear();
        LOGGER.info("Cleared item recipe cache");
    }

    public static Optional<RustleRecipe> getRecipeFor(ItemStack stack) {
        for (Map.Entry<Ingredient, List<RustleRecipe>> entry : RECIPE_CACHE.entrySet()) {
            if (entry.getKey().test(stack)) {
                return Optional.of(entry.getValue().getFirst());
            }
        }

        return Optional.empty();
    }
}