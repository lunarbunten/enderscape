package net.bunten.enderscape.block.properties;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class StateProperties extends BlockStateProperties {

    public static final BooleanProperty OVERHEATED = BooleanProperty.create("overheated");

    public static final IntegerProperty BLINKLIGHT_BODY_STAGE = IntegerProperty.create("stage", 0, 2);
    public static final IntegerProperty BLINKLIGHT_HEAD_STAGE = IntegerProperty.create("stage", 0, 5);

    public static final EnumProperty<Part> GROWTH_PART = EnumProperty.create("part", Part.class);

}