package net.bunten.enderscape.block.state;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class StateProperties extends BlockStateProperties {

    public static final BooleanProperty IS_PLAYING = BooleanProperty.create("is_playing");
    public static final BooleanProperty OVERHEATED = BooleanProperty.create("overheated");

    public static final EnumProperty<PartProperty> GROWTH_PART = EnumProperty.create("part", PartProperty.class);
    
    public static final EnumProperty<MagniaPolarityProperty> MAGNIA_POLARITY = EnumProperty.create("polarity", MagniaPolarityProperty.class);
    public static final EnumProperty<OptionalMagniaPolarityProperty> OPTIONAL_MAGNIA_POLARITY = EnumProperty.create("polarity", OptionalMagniaPolarityProperty.class);

    public static final IntegerProperty BLINKLIGHT_BODY_STAGE = IntegerProperty.create("stage", 0, 2);
    public static final IntegerProperty BLINKLIGHT_HEAD_STAGE = IntegerProperty.create("stage", 0, 5);
}