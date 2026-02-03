package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.block.dispenser.LodestoneTeleportationDispenserBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.HashMap;
import java.util.Map;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.world.level.block.Blocks.CHORUS_FLOWER;
import static net.minecraft.world.level.block.Blocks.CHORUS_PLANT;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EnderscapeModifications {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ((FireBlock) Blocks.FIRE).setFlammable(DRY_END_GROWTH.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(CHORUS_SPROUTS.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(WISP_GROWTH.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(WISP_SPROUTS.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(WISP_FLOWER.get(), 60, 100);

            ((FireBlock) Blocks.FIRE).setFlammable(CHORUS_PLANT, 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(CHORUS_FLOWER, 5, 5);

            ((FireBlock) Blocks.FIRE).setFlammable(VEILED_LOG.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(VEILED_WOOD.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(STRIPPED_VEILED_LOG.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(STRIPPED_VEILED_WOOD.get(), 5, 5);

            ((FireBlock) Blocks.FIRE).setFlammable(VEILED_PLANKS.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(VEILED_SLAB.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(VEILED_FENCE_GATE.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(VEILED_FENCE.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(VEILED_STAIRS.get(), 5, 5);

            ((FireBlock) Blocks.FIRE).setFlammable(VEILED_SAPLING.get(), 15, 60);
            ((FireBlock) Blocks.FIRE).setFlammable(VEILED_LEAVES.get(), 30, 60);
            ((FireBlock) Blocks.FIRE).setFlammable(VEILED_LEAF_PILE.get(), 60, 20);

            ((FireBlock) Blocks.FIRE).setFlammable(CELESTIAL_STEM.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(CELESTIAL_HYPHAE.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(STRIPPED_CELESTIAL_STEM.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(STRIPPED_CELESTIAL_HYPHAE.get(), 5, 5);

            ((FireBlock) Blocks.FIRE).setFlammable(CELESTIAL_PLANKS.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(CELESTIAL_SLAB.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(CELESTIAL_FENCE_GATE.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(CELESTIAL_FENCE.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(CELESTIAL_STAIRS.get(), 5, 5);

            ((FireBlock) Blocks.FIRE).setFlammable(CELESTIAL_CAP.get(), 30, 60);

            ((FireBlock) Blocks.FIRE).setFlammable(MURUBLIGHT_STEM.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(MURUBLIGHT_HYPHAE.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(STRIPPED_MURUBLIGHT_STEM.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(STRIPPED_MURUBLIGHT_HYPHAE.get(), 5, 5);

            ((FireBlock) Blocks.FIRE).setFlammable(MURUBLIGHT_PLANKS.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(MURUBLIGHT_SLAB.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(MURUBLIGHT_FENCE_GATE.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(MURUBLIGHT_FENCE.get(), 5, 5);
            ((FireBlock) Blocks.FIRE).setFlammable(MURUBLIGHT_STAIRS.get(), 5, 5);

            ((FireBlock) Blocks.FIRE).setFlammable(MURUBLIGHT_CAP.get(), 30, 60);

            ((FireBlock) Blocks.FIRE).setFlammable(CELESTIAL_GROWTH.get(), 60, 60);
            ((FireBlock) Blocks.FIRE).setFlammable(BULB_FLOWER.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(FLANGER_BERRY_VINE.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(FLANGER_BERRY_FLOWER.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(UNRIPE_FLANGER_BERRY_BLOCK.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(RIPE_FLANGER_BERRY_BLOCK.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(CELESTIAL_CHANTERELLE.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(MURUBLIGHT_CHANTERELLE.get(), 60, 100);

            ((FireBlock) Blocks.FIRE).setFlammable(CORRUPT_GROWTH.get(), 60, 100);
            ((FireBlock) Blocks.FIRE).setFlammable(BLINKLIGHT_VINES_BODY.get(), 15, 60);
            ((FireBlock) Blocks.FIRE).setFlammable(BLINKLIGHT_VINES_HEAD.get(), 15, 60);
            ((FireBlock) Blocks.FIRE).setFlammable(MURUBLIGHT_BRACKET.get(), 60, 100);
            
            GameBusHandlers.STATES.put(VEILED_LOG.get(), STRIPPED_VEILED_LOG.get());
            GameBusHandlers.STATES.put(VEILED_WOOD.get(), STRIPPED_VEILED_WOOD.get());
            GameBusHandlers.STATES.put(CELESTIAL_STEM.get(), STRIPPED_CELESTIAL_STEM.get());
            GameBusHandlers.STATES.put(CELESTIAL_HYPHAE.get(), STRIPPED_CELESTIAL_HYPHAE.get());
            GameBusHandlers.STATES.put(MURUBLIGHT_STEM.get(), STRIPPED_MURUBLIGHT_STEM.get());
            GameBusHandlers.STATES.put(MURUBLIGHT_HYPHAE.get(), STRIPPED_MURUBLIGHT_HYPHAE.get());

            DispenserBlock.registerBehavior(RUSTLE_BUCKET.get(), new DefaultDispenseItemBehavior() {
                private final DefaultDispenseItemBehavior defaultBehavior = new DefaultDispenseItemBehavior();

                @Override
                public ItemStack execute(BlockSource source, ItemStack stack) {
                    DispensibleContainerItem item = (DispensibleContainerItem) stack.getItem();
                    BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
                    Level level = source.level();

                    if (item.emptyContents(null, level, pos, null)) {
                        item.checkExtraContent(null, level, stack, pos);
                        return consumeWithRemainder(source, stack, new ItemStack(Items.BUCKET));
                    } else {
                        return defaultBehavior.dispense(source, stack);
                    }
                }
            });

            DispenserBlock.registerBehavior(MIRROR.get(), new LodestoneTeleportationDispenserBehavior());
        });
    }

    @EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class GameBusHandlers {
        private static final Map<Block, Block> STATES = new HashMap<>();
        
        @SubscribeEvent
        public static void onToolUse(BlockEvent.BlockToolModificationEvent event) {
            if (event.getItemAbility() == ItemAbilities.AXE_STRIP) {
                var originalState = event.getFinalState();
                var block = STATES.get(originalState.getBlock());
                if (block != null) {
                    var state = block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS));
                    event.setFinalState(state);
                }
            }
        }
    }
}