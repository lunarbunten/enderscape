package net.bunten.enderscape;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeLootInjects;
import net.bunten.enderscape.registry.EnderscapeRegistry;
import net.bunten.enderscape.registry.EnderscapeStats;
import net.bunten.enderscape.util.PlantUtil;
import net.bunten.enderscape.world.EnderscapeBiomes;
import net.bunten.enderscape.world.EnderscapeFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class Enderscape implements ModInitializer {

    public static final String MOD_ID = "enderscape";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(Enderscape.id("all"), () -> new ItemStack(EnderscapeItems.NEBULITE));

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Logger getLogger() {
        return LogManager.getLogger();
    }

    public static boolean hasBetterEnd() {
        return FabricLoader.getInstance().isModLoaded("betterend");
    }

    public static boolean isDevelopment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    private static ImmutableMap<Block, Block> getStrippedMap() {
        var builder = new Builder<Block, Block>();

        builder.put(EnderscapeBlocks.CELESTIAL_STEM, EnderscapeBlocks.STRIPPED_CELESTIAL_STEM);
        builder.put(EnderscapeBlocks.CELESTIAL_HYPHAE, EnderscapeBlocks.STRIPPED_CELESTIAL_HYPHAE);

        return builder.build();
    }

    private void registerEvents() {
        UseBlockCallback.EVENT.register((mob, world, hand, result) -> {
            ItemStack stack = mob.getStackInHand(hand);
            BlockPos pos = result.getBlockPos();

            BlockState state = world.getBlockState(pos);
            Block block = getStrippedMap().get(state.getBlock());
            
            if (block != null && stack.getItem() instanceof AxeItem) {
                world.playSound(mob, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1, 1);
                if (!world.isClient()) {
                    world.setBlockState(pos, block.getDefaultState().with(PillarBlock.AXIS, state.get(PillarBlock.AXIS)), 11);
                    if (mob != null) {
                        stack.damage(1, mob, ((p) -> (p).sendToolBreakStatus(hand)));
                    }
                }
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });

        UseBlockCallback.EVENT.register((mob, world, hand, result) -> {
            ItemStack stack = mob.getStackInHand(hand);
            BlockPos pos = result.getBlockPos();
            BlockState state = world.getBlockState(pos);

            var celestial = EnderscapeBlocks.CELESTIAL_MYCELIUM_BLOCK;
            var nearestCelestial = BlockPos.findClosest(pos, 4, 2, nearest -> world.getBlockState(nearest).isOf(celestial));
            boolean bl = nearestCelestial.isPresent();

            if ((state.isOf(Blocks.END_STONE) || state.isOf(celestial)) && stack.getItem() instanceof BoneMealItem && world.getBlockState(pos.up()).isTranslucent(world, pos) && bl) {
                if (!world.isClient()) {
                    world.playSound(null, pos, SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, 1, 1);
                    if (!mob.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                    PlantUtil.generateCelestialMycelium(world, pos, 12, 3, 0.3F);
                } else {
                    BoneMealItem.createParticles(world, pos, 15);
                }
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });
    }

    @Override
    public void onInitialize() {

        EnderscapeRegistry.init();
        EnderscapeCriteria.init();
        EnderscapeEntities.init();
        EnderscapeLootInjects.init();
        EnderscapeStats.init();

        EnderscapeFeatures.init();
        EnderscapeBiomes.init();

        registerEvents();

        getLogger().info("Enderscape initialized!");
    }
}