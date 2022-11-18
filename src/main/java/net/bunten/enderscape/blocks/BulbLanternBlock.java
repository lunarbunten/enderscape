package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.HasRenderType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BulbLanternBlock extends LanternBlock implements HasRenderType {
    public BulbLanternBlock(Properties settings) {
        super(settings);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(HANGING)) {
            return Shapes.join(box(5, 2, 5, 11, 8, 11), box(2, 6, 2, 14, 10, 14), BooleanOp.OR);
        } else {
            return Shapes.join(box(2, 4, 2, 14, 8, 14), box(5, 0, 5, 11, 6, 11), BooleanOp.OR);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}