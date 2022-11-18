package net.bunten.enderscape.world.surface;

import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;

import net.bunten.enderscape.util.States;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;

public class DefaultEndSurfaceRule implements SurfaceMaterialProvider {

    private final BlockState END_STONE = States.END_STONE;
    
    protected RuleSource rule(BlockState state) {
        return SurfaceRules.state(state);
    }

    @Override
    public BlockState getTopMaterial() {
        return END_STONE;
    }

    @Override
    public BlockState getUnderMaterial() {
        return END_STONE;
    }

    @Override
    public BlockState getAltTopMaterial() {
        return END_STONE;
    }

    @Override
    public boolean generateFloorRule() {
        return false;
    }

    @Override
    public SurfaceRuleBuilder surface() {
        SurfaceRuleBuilder builder = SurfaceRuleBuilder.start();

        builder = builder.filler(getUnderMaterial());

        return builder;
    }
}