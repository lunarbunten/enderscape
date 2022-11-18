package net.bunten.enderscape.world.surface;

import java.util.List;

import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import org.betterx.bclib.api.v2.levelgen.surface.rules.SwitchRuleSource;
import org.betterx.bclib.interfaces.NumericProvider;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.bclib.mixin.common.SurfaceRulesContextAccessor;
import org.betterx.bclib.noise.OpenSimplexNoise;
import org.betterx.bclib.util.MHelper;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.blocks.AbstractMyceliumBlock;
import net.bunten.enderscape.util.States;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;

public class CorruptSurfaceRule implements SurfaceMaterialProvider {

    private final BlockState END_STONE = States.END_STONE;
    private final BlockState CORRUPT = States.CORRUPT_MYCELIUM.setValue(AbstractMyceliumBlock.FACING, Direction.DOWN);
    
    protected RuleSource rule(BlockState state) {
        return SurfaceRules.state(state);
    }

    @Override
    public BlockState getTopMaterial() {
        return CORRUPT;
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
        var ruleSource = new SwitchRuleSource(new SurfaceProvider(), List.of(rule(END_STONE), rule(CORRUPT)));

        builder = builder.filler(getUnderMaterial());
        builder.rule(2, SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, ruleSource));

        return builder;
    }
    
    private static class SurfaceProvider implements NumericProvider {
        public static final String NAME = "corrupt_depths";
        
        public static final SurfaceProvider DEFAULT = new SurfaceProvider();
        public static final Codec<SurfaceProvider> CODEC = Codec.BYTE.fieldOf(NAME).xmap((obj)-> DEFAULT, obj -> 0).codec();
    
        private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(1512);
    
        @Override
        public int getNumber(SurfaceRulesContextAccessor context) {
            final int x = context.getBlockX();
            final int z = context.getBlockZ();

            var scale = 0.034;
            double value = NOISE.eval(x * scale, z * scale) + (MHelper.RANDOM_SOURCE.nextFloat() * 0.12F) * 2;

            return value > 0.3F || value < -0.3F ? 1 : 0;
        }
    
        @Override
        public Codec<? extends NumericProvider> pcodec() {
            return CODEC;
        }
    
        static {
            Registry.register(NumericProvider.NUMERIC_PROVIDER, Enderscape.id(NAME), SurfaceProvider.CODEC);
        }
    }
}