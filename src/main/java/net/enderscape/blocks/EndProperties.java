package net.enderscape.blocks;

import net.minecraft.state.property.EnumProperty;

public class EndProperties {
    public static final EnumProperty<Part> PART = EnumProperty.of("part", Part.class);
    public static final EnumProperty<BerryStage> BERRY_STAGE = EnumProperty.of("berry_stage", BerryStage.class);
}