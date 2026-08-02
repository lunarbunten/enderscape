package net.penumbra.enderscape.manager;

import com.google.common.base.Stopwatch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.item.crafting.VoidLachrymaRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class VoidLachrymaItemConversionManager {

    private static final Map<Ingredient, List<VoidLachrymaRecipe>> RECIPE_CACHE = new IdentityHashMap<>();
    private static final Logger LOGGER = LogManager.getLogger(VoidLachrymaItemConversionManager.class);

    public static void onServerStarting(MinecraftServer server) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        RECIPE_CACHE.clear();

        Map<Ingredient, List<VoidLachrymaRecipe>> map = new IdentityHashMap<>();
        Collection<RecipeHolder<VoidLachrymaRecipe>> recipes = server.getRecipeManager().getAllOfType(VoidLachrymaRecipe.TYPE);

        recipes.forEach(conversion -> map.computeIfAbsent(conversion.value().input(), _ -> new ArrayList<>()).add(conversion.value()));
        map.forEach((item, list) -> RECIPE_CACHE.put(item, Collections.unmodifiableList(list)));

        LOGGER.info("Cached {} void lachryma item recipes in {}", RECIPE_CACHE.size(), stopwatch);
    }

    public static void onServerStopping(MinecraftServer server) {
        RECIPE_CACHE.clear();
        LOGGER.info("Cleared item recipe cache");
    }

    public static void tickConversion(Entity entity, ServerLevel server) {
        if (entity instanceof ItemEntity item && VoidManager.inVoidLachryma(item)) {
            for (Map.Entry<Ingredient, List<VoidLachrymaRecipe>> entry : RECIPE_CACHE.entrySet()) {
                if (entry.getKey().test(item.getItem())) {
                    List<VoidLachrymaRecipe> recipes = entry.getValue();

                    for (VoidLachrymaRecipe conversion : recipes) {
                        if (canConvert(item, conversion)) {
                            convertItem(item, conversion, server);
                            return;
                        }
                    }
                }
            }
        }
    }

    public static boolean hasRecipe(ItemStack stack) {
        return RECIPE_CACHE.keySet().stream().anyMatch(ingredient -> ingredient.test(stack));
    }

    private static void convertItem(ItemEntity item, VoidLachrymaRecipe conversion, ServerLevel server) {
        Vec3 position = item.position();
        ItemStack original = item.getItem();

        server.sendParticles(conversion.particle(), position.x, position.y + 0.5, position.z, 4, 0, 0, 0, 0);

        item.setItem(conversion.result().withCount(original.count()).apply(original.getComponentsPatch()));
        item.playSound(conversion.sound().value(), 1.0F, 1.0F);
    }

    private static boolean canConvert(ItemEntity item, VoidLachrymaRecipe recipe) {
        boolean passedMinimumVoidTicks = VoidManager.getVoidTicksPercentage(item) >= recipe.minimumVoidedPercentage();
        boolean passedRandomChance = recipe.averageChance() >= item.getRandom().nextFloat();

        return passedMinimumVoidTicks && passedRandomChance;
    }
}