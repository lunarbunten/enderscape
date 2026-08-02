package net.penumbra.enderscape.block.state;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class StateProperties extends BlockStateProperties {

    public static final int MAX_BLINKLAMP_LUMINANCE = 7;
    public static final int MAX_BLINKLIGHT_BODY_STAGES = 2;
    public static final int MAX_BLINKLIGHT_HEAD_STAGES = 5;
    public static final int MAX_VEILED_LEAVES_DISTANCE = 14;
    public static final int MAX_VOID_SHALE_ITERATIONS = 48;
    public static final int MAX_VOID_SHALE_STRESS = 3;

    public static final BooleanProperty MAGNIA_SPROUT_OVERHEATED = BooleanProperty.create("overheated");
    public static final BooleanProperty RADIO_IS_PLAYING = BooleanProperty.create("is_playing");

    public static final EnumProperty<PurifyingPhase> PURIFYING_PHASE = EnumProperty.create("phase", PurifyingPhase.class);
    public static final EnumProperty<EndHavenCoreState> END_HAVEN_CORE_STATE = EnumProperty.create("state", EndHavenCoreState.class);
    public static final EnumProperty<PartProperty> GROWTH_PART = EnumProperty.create("part", PartProperty.class);
    public static final EnumProperty<MagniaPolarityProperty> MAGNIA_POLARITY = EnumProperty.create("polarity", MagniaPolarityProperty.class);
    public static final EnumProperty<OptionalMagniaPolarityProperty> OPTIONAL_MAGNIA_POLARITY = EnumProperty.create("polarity", OptionalMagniaPolarityProperty.class);

    public static final IntegerProperty BLINKLAMP_LUMINANCE = IntegerProperty.create("luminance", 0, MAX_BLINKLAMP_LUMINANCE);
    public static final IntegerProperty BLINKLIGHT_BODY_STAGE = IntegerProperty.create("stage", 0, MAX_BLINKLIGHT_BODY_STAGES);
    public static final IntegerProperty BLINKLIGHT_HEAD_STAGE = IntegerProperty.create("stage", 0, MAX_BLINKLIGHT_HEAD_STAGES);
    public static final IntegerProperty VEILED_LEAVES_DISTANCE = IntegerProperty.create("distance", 1, MAX_VEILED_LEAVES_DISTANCE);
    public static final IntegerProperty VOID_SHALE_ITERATION = IntegerProperty.create("iteration", 0, MAX_VOID_SHALE_ITERATIONS);
    public static final IntegerProperty VOID_SHALE_STRESS = IntegerProperty.create("stress", 0, MAX_VOID_SHALE_STRESS);
}