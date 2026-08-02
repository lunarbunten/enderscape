package net.penumbra.enderscape.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import net.penumbra.enderscape.util.BlockUtil;

import static net.minecraft.world.level.block.Blocks.END_STONE;
import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.VEILED_END_STONE;

public class RustleSilkItem extends Item {

    private static final int RADIUS = 2;

    public RustleSilkItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (isValid(level, pos)) {
            ItemStack stack = context.getItemInHand();

            if (level.isClientSide()) {
                playClientsideEffects(level, pos);
            } else {
                modifyBlocks(level, pos, context.getPlayer());
            }

            stack.shrink(1);
            context.getPlayer().awardStat(Stats.ITEM_USED.get(stack.getItem()));

            return InteractionResult.SUCCESS_SERVER;
        }

        return super.useOn(context);
    }

    private static boolean isValid(Level level, BlockPos pos) {
        return (level.getBlockState(pos).is(END_STONE) || level.getBlockState(pos).is(VEILED_END_STONE)) && BlockUtil.hasAirAbove(level, pos, Direction.UP);
    }

    private static void modifyBlocks(Level level, BlockPos pos, Player player) {
        BlockState state = VEILED_END_STONE.defaultBlockState();

        level.setBlock(pos, state, 2);
        BlockUtil.replaceSpherically(level, pos, state, RADIUS, Direction.UP, (other) -> other.is(END_STONE));

        level.playSound(null, pos, EnderscapeItemSounds.RUSTLE_SILK_USE.value(), player.getSoundSource(), 1.0F, 1.0F);
    }

    private static void playClientsideEffects(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();

        for (int i = 0; i < 4; i++) {
            Vec3 position = Vec3.atBottomCenterOf(pos.above());

            double rx = Mth.nextFloat(random, -0.5F, 0.5F);
            double ry = Mth.nextFloat(random, 0.0F, 0.15F);
            double rz = Mth.nextFloat(random, -0.5F, 0.5F);

            level.addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    true,
                    true,
                    position.x() + rx,
                    position.y() + ry,
                    position.z() + rz,
                    rx / 2,
                    ry / 2,
                    rz / 2
            );
        }
    }
}
