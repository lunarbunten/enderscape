package net.bunten.enderscape.blocks.properties;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class StateProperties extends BlockStateProperties {

    public static final IntegerProperty BLINKLIGHT_BODY_STAGE = IntegerProperty.create("stage", 0, 2);
    public static final IntegerProperty BLINKLIGHT_HEAD_STAGE = IntegerProperty.create("stage", 0, 5);

    public static final EnumProperty<Stage> BERRY_STAGE = EnumProperty.create("berry_stage", Stage.class);
    public static final EnumProperty<Part> GROWTH_PART = EnumProperty.create("part", Part.class);

}