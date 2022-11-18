package net.bunten.enderscape.blocks;

import org.betterx.ui.ColorUtil;

import net.bunten.enderscape.util.MathUtil;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class BlinklightColorProvider implements BlockColor {

    public static final Vec3i[] COLORS = new Vec3i[] {
            new Vec3i(209, 225, 255),
            new Vec3i(255, 209, 250)
    };

    @Override
    public int getColor(BlockState state, BlockAndTintGetter view, BlockPos pos, int tint) {
        if (pos == null) pos = BlockPos.ZERO;

        int size = COLORS.length - 1;
        long i = (long) pos.getX() + (long) pos.getY() + (long) pos.getZ();
        double delta = i * 0.1;
        int index = MathUtil.floor(delta);
        int index2 = (index + 1) & size;
        delta -= index;
        index &= size;

        Vec3i color1 = COLORS[index];
        Vec3i color2 = COLORS[index2];

        int r = MathUtil.floor(MathUtil.lerp(delta, color1.getX(), color2.getX()));
        int g = MathUtil.floor(MathUtil.lerp(delta, color1.getY(), color2.getY()));
        int b = MathUtil.floor(MathUtil.lerp(delta, color1.getZ(), color2.getZ()));

        return ColorUtil.color(r, g, b);
    }
}