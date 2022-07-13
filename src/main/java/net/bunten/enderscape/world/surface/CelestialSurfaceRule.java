package net.bunten.enderscape.world.surface;

import java.util.List;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.util.OpenSimplexNoise;
import net.bunten.enderscape.util.States;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRule;
import ru.bclib.api.surface.SurfaceRuleBuilder;
import ru.bclib.api.surface.rules.SwitchRuleSource;
import ru.bclib.interfaces.NumericProvider;
import ru.bclib.interfaces.SurfaceMaterialProvider;
import ru.bclib.mixin.common.SurfaceRulesContextAccessor;
import ru.bclib.util.MHelper;

public class CelestialSurfaceRule implements SurfaceMaterialProvider {

    public static MaterialRule register() {
        return new CelestialSurfaceRule().surface().build();
    }

    private final BlockState END_STONE = States.END_STONE;
    private final BlockState CELESTIAL = States.CELESTIAL_MYCELIUM;
    
    protected MaterialRule rule(BlockState state) {
        return MaterialRules.block(state);
    }

    @Override
    public BlockState getTopMaterial() {
        return CELESTIAL;
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
        builder.rule(2, MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, new SwitchRuleSource(new SurfaceProvider(), List.of(rule(END_STONE), rule(CELESTIAL)))));

        return builder;
    }
    
    private static class SurfaceProvider implements NumericProvider {
        public static final String NAME = "celestial_plains";
        
        public static final SurfaceProvider DEFAULT = new SurfaceProvider();
        public static final Codec<SurfaceProvider> CODEC = Codec.BYTE.fieldOf(NAME).xmap((obj)-> DEFAULT, obj -> 0).codec();
    
        private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(1512);
    
        @Override
        public int getNumber(SurfaceRulesContextAccessor context) {
            final int x = context.getBlockX();
            final int z = context.getBlockZ();

            double noise = NOISE.eval(x * 0.086, z * 0.086) + (MHelper.RANDOM.nextFloat() * 0.12F) * 4;

            if (noise > 0.42F) {
                return 0;
            } else {
                return 1;
            }
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