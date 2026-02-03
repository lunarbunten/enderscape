package net.bunten.enderscape.registry;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.sound.SoundTypeOverride;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Predicate;

public class EnderscapeSoundTypeOverrides {

	private static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

	public static final List<SoundTypeOverride> SOUND_TYPE_OVERRIDES = new ArrayList<>();
	private static final List<BlockState> IGNORED_STATES = new ArrayList<>();
	private static final Map<BlockState, SoundType> SOUND_TYPE_CACHE = new IdentityHashMap<>();

	public static Optional<SoundType> getSoundType(BlockState state) {
		if (IGNORED_STATES.contains(state)) return Optional.empty();

		return Optional.ofNullable(SOUND_TYPE_CACHE.computeIfAbsent(state, s -> {
			for (SoundTypeOverride override : SOUND_TYPE_OVERRIDES) if (override.applies(s)) return override.getSoundType();
			IGNORED_STATES.add(s);
			return null;
		}));
	}

	public static void register(SoundTypeOverride override) {
		Objects.requireNonNull(override);
		SOUND_TYPE_OVERRIDES.add(override);
	}

	public static void register(SoundTypeOverride... overrides) {
		for (SoundTypeOverride override : overrides) register(override);
	}

	private static String getNameOf(BlockState state) {
		return BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
	}

	static {
		Map<SoundType, Predicate<BlockState>> overrides = Map.of(
				EnderscapeSoundTypes.CHORUS_PLANT, (state) -> canApplyOverride(
						state,
						CONFIG.blocksSoundUpdateChorus,
						List.of("chorus_plant"),
						List.of(),
						true
				),
				EnderscapeSoundTypes.CHORUS_FLOWER, (state) -> canApplyOverride(
						state,
						CONFIG.blocksSoundUpdateChorus,
						List.of("chorus_flower"),
						List.of(),
						true
				),
				EnderscapeSoundTypes.PURPUR, (state) -> canApplyOverride(
						state,
						CONFIG.blocksSoundUpdatePurpur,
						List.of("purpur"),
						List.of("dusk")
				),
				EnderscapeSoundTypes.END_PORTAL_FRAME, (state) -> canApplyOverride(
						state,
						CONFIG.blockSoundUpdateEndPortalFrame,
						List.of("end_portal_frame"),
						List.of(),
						true
				),
				EnderscapeSoundTypes.END_PORTAL, (state) -> canApplyOverride(
						state,
						CONFIG.blockSoundUpdateEndPortals,
						List.of("end_portal"),
						List.of("frame"),
						true
				),
				EnderscapeSoundTypes.END_GATEWAY, (state) -> canApplyOverride(
						state,
						CONFIG.blockSoundUpdateEndPortals,
						List.of("end_gateway"),
						List.of(),
						true
				),
				EnderscapeSoundTypes.END_ROD, (state) -> canApplyOverride(
						state,
						CONFIG.blockSoundsUpdateEndRods,
						List.of("end_rod"),
						List.of(),
						true
				),
				EnderscapeSoundTypes.END_STONE, (state) -> canApplyOverride(
						state,
						CONFIG.blockSoundUpdateEndStone,
						List.of("end_stone"),
						List.of("brick", "veiled")
				),
				EnderscapeSoundTypes.END_STONE_BRICKS, (state) -> canApplyOverride(
						state,
						CONFIG.blockSoundUpdateEndStoneBricks,
						List.of("end_stone", "brick"),
						List.of("chiseled"),
						true
				),
				EnderscapeSoundTypes.SHULKER_BOX, (state) -> canApplyOverride(
						state,
						CONFIG.blockSoundUpdateShulkerBoxes,
						List.of("shulker", "box"),
						List.of(),
						true
				)
		);

		overrides.forEach((sound, condition) -> register(new SoundTypeOverride(sound, condition)));
	}

	private static boolean canApplyOverride(BlockState state, boolean condition, List<String> allowed, List<String> disallowed) {
		return canApplyOverride(state, condition, allowed, disallowed, false);
	}

	private static boolean canApplyOverride(BlockState state, boolean condition, List<String> allowed, List<String> disallowed, boolean requireAllAlloweds) {
		if (!condition) return false;
		String name = getNameOf(state);

		for (String string : disallowed) if (name.contains(string)) return false;

		if (requireAllAlloweds) {
			for (String string : allowed) if (!name.contains(string)) return false;
			return true;
		}

		for (String string : allowed) if (name.contains(string)) return true;

		return false;
	}
}