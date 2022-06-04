package net.bunten.enderscape.world;

import java.util.List;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRule;
import ru.bclib.api.surface.SurfaceRuleBuilder;
import ru.bclib.api.surface.rules.SwitchRuleSource;
import ru.bclib.interfaces.NumericProvider;
import ru.bclib.interfaces.SurfaceMaterialProvider;
import ru.bclib.mixin.common.SurfaceRulesContextAccessor;
import ru.bclib.util.MHelper;

public class EnderscapeSurfaces {
    
    public static final MaterialRule CELESTIAL_SURFACE = new CelestialProvider().surface().build();

    protected static class CelestialSurface implements NumericProvider {
        public static final CelestialSurface DEFAULT = new CelestialSurface();
        public static final Codec<CelestialSurface> CODEC = Codec.BYTE.fieldOf("celestial").xmap((obj)-> DEFAULT, obj -> 0).codec();
    
        private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(1512);
    
        @Override
        public int getNumber(SurfaceRulesContextAccessor context) {
            final int x = context.getBlockX();
            final int z = context.getBlockZ();
            return getDepth(x, z);
        }
        
        public static int getDepth(int x, int z) {
            double value =  NOISE.eval(x * 0.086, z * 0.086) + (MHelper.RANDOM.nextFloat() * 0.12F) * 4;
            if (value > 0.42F) {
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
            Registry.register(NumericProvider.NUMERIC_PROVIDER, Enderscape.id("celestial"), CelestialSurface.CODEC);
        }
    }

    protected static class CelestialProvider implements SurfaceMaterialProvider {
        private final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
        private final BlockState CELESTIAL = EnderscapeBlocks.CELESTIAL_MYCELIUM_BLOCK.getDefaultState();
        
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
            
            if (generateFloorRule() && getTopMaterial()!=getUnderMaterial()){
                if (getTopMaterial()!=getAltTopMaterial()){
                    builder.floor(getTopMaterial());
                } else {
                    builder.chancedFloor(getTopMaterial(), getAltTopMaterial());
                }
            }

            builder = builder.filler(getUnderMaterial());

            return builder.rule(2, MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, new SwitchRuleSource(new CelestialSurface(), List.of(rule(END_STONE), rule(CELESTIAL)))));
        }
    }
}