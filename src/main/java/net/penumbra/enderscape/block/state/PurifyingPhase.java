package net.penumbra.enderscape.block.state;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.block.RandomlyPurifiesEntities;

public enum PurifyingPhase implements StringRepresentable {
    POWERLESS("powerless"),
    INACTIVE("inactive"),
    CHARGING("charging"),
    ACTIVE("active"),
    COOLDOWN("cooldown");

    private final String name;

    PurifyingPhase(String name) {
        this.name = name;
    }

    public static BlockState set(BlockState state, PurifyingPhase dormantPhase) {
        return state.setValue(RandomlyPurifiesEntities.PHASE, dormantPhase);
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public static BlockState with(Block block, PurifyingPhase phase) {
        return block.defaultBlockState().setValue(StateProperties.PURIFYING_PHASE, phase);
    }

    public static PurifyingPhase get(BlockState state) {
        return state.getValue(RandomlyPurifiesEntities.PHASE);
    }

    public static boolean powerless(BlockState state) {
        return get(state).equals(POWERLESS);
    }

    public static boolean inactive(BlockState state) {
        return get(state).equals(INACTIVE);
    }

    public static boolean charging(BlockState state) {
        return get(state).equals(CHARGING);
    }

    public static boolean active(BlockState state) {
        return get(state).equals(ACTIVE);
    }

    public static boolean cooldown(BlockState state) {
        return get(state).equals(COOLDOWN);
    }

    public static boolean powerlessOrInactive(BlockState state) {
        return powerless(state) || inactive(state);
    }
}