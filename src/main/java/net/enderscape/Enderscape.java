package net.enderscape;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.apache.logging.log4j.LogManager;

import net.enderscape.entity.drifter.DrifterEntity;
import net.enderscape.registry.EndBlocks;
import net.enderscape.registry.EndCriteria;
import net.enderscape.registry.EndEntities;
import net.enderscape.registry.EndItems;
import net.enderscape.registry.EndLootTables;
import net.enderscape.registry.EndSounds;
import net.enderscape.util.EndMath;
import net.enderscape.world.EndBiomes;
import net.enderscape.world.EndConfiguredFeatures;
import net.enderscape.world.EndFeatures;
import net.enderscape.world.EndSurfaces;
import net.enderscape.world.biomes.EnderscapeBiome;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class Enderscape implements ModInitializer {

    public static final String MOD_ID = "enderscape";
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(Enderscape.id("all"), () -> new ItemStack(EndItems.NEBULITE));

    public static final SimpleRegistry<EnderscapeBiome> ENDERSCAPE_BIOME = FabricRegistryBuilder.createSimple(EnderscapeBiome.class, id("enderscape_biome")).buildAndRegister();

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static void registerCompostableItem(float chance, ItemConvertible item) {
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(item.asItem(), chance);
    }

    public static boolean isDevelopment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    private static ImmutableMap<Block, Block> getStrippedMap() {
        return new Builder<Block, Block>()
        .put(EndBlocks.CELESTIAL_STEM, EndBlocks.STRIPPED_CELESTIAL_STEM)
        .put(EndBlocks.CELESTIAL_HYPHAE, EndBlocks.STRIPPED_CELESTIAL_HYPHAE)
        .build();
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

            if (state.isOf(Blocks.END_STONE) && stack.getItem() instanceof BoneMealItem) {
                if (!world.isClient()) {
                    world.playSound(null, pos, SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, 1, 1);
                    world.setBlockState(pos, EndBlocks.CELESTIAL_MYCELIUM_BLOCK.getDefaultState(), 3);
                    if (!mob.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                } else {
                    BoneMealItem.createParticles(world, pos, 15);
                }
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, result) -> {
            if (player.isAlive()) {
                if (entity instanceof DrifterEntity mob) {
                    ItemStack stack = player.getStackInHand(hand);
                    Item item = stack.getItem();
                    if (stack.isIn(EndItems.DRIFTER_FOOD) && !mob.isDrippingJelly()) {
                        mob.heal(4);
                        mob.playSound(mob.getEatSound(stack), EndMath.nextFloat(mob.getRandom(), 0.3F, 0.6F), 1);
                        if (!world.isClient()) {
                            if (!player.getAbilities().creativeMode) {
                                stack.decrement(1);
                            }
                            if (mob.getRandom().nextInt(5) == 0) {
                                if (!mob.isDrippingJelly()) {
                                    mob.setDrippingJelly(true);
                                }
                            }
                        }
                        if (world.isClient()) {
                            for (int i = 0; i < 5; i++) {
                                world.addParticle(ParticleTypes.HEART, mob.getParticleX(0.6), mob.getRandomBodyY() + 0.5D, mob.getParticleZ(0.6), 0, 0, 0);
                            }
                        }
                        return ActionResult.success(world.isClient());
                    } else if (item == Items.GLASS_BOTTLE && mob.isDrippingJelly()) {
                        player.playSound(EndSounds.ENTITY_DRIFTER_MILK, 0.5F, 1);
                        if (!world.isClient()) {
                            if (!player.getAbilities().creativeMode) {
                                stack.decrement(1);
                            }
                            ItemStack jelly = new ItemStack(EndItems.DRIFT_JELLY_BOTTLE);
                            if (!player.getInventory().insertStack(jelly)) {
                                player.dropItem(jelly, false);
                            }
                            mob.setDrippingJelly(false);
                        }
                        return ActionResult.success(world.isClient());
                    }
                }
            }
            return ActionResult.PASS;
        });
    }

    @Override
    public void onInitialize() {

        Registry.register(Registry.PAINTING_MOTIVE, "big_guy", new PaintingMotive(64, 64));
        Registry.register(Registry.PAINTING_MOTIVE, "cat", new PaintingMotive(32, 32));
        Registry.register(Registry.PAINTING_MOTIVE, "thirsty_guy", new PaintingMotive(64, 64));

        EndItems.init();
        EndBlocks.init();
        EndCriteria.init();
        EndEntities.init();
        EndLootTables.init();

        EndBiomes.init();
        EndConfiguredFeatures.init();
        EndFeatures.init();
        EndSurfaces.init();

        registerEvents();

        registerCompostableItem(0.3F, EndBlocks.CELESTIAL_GROWTH);
        registerCompostableItem(0.5F, EndBlocks.BULB_FLOWER);
        registerCompostableItem(0.65F, EndBlocks.CELESTIAL_FUNGUS);
        registerCompostableItem(1, EndBlocks.FLANGER_BERRY_BLOCK);
        registerCompostableItem(0.65F, EndItems.FLANGER_BERRY);
        registerCompostableItem(0.65F, EndItems.MURUSHROOMS);

        LogManager.getLogger().info("Enderscape initialized!");
    }
}