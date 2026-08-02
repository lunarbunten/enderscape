package net.penumbra.enderscape.registry.server;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.*;
import net.penumbra.enderscape.Enderscape;

import java.util.function.ToIntFunction;

public class EnderscapeGameRules {

    public static final GameRule<Boolean> VOID_DAMAGE_OVERRIDES_HEALTH = registerBoolean("void_damage_overrides_health", GameRuleCategory.PLAYER, true);
    public static final GameRule<Boolean> VOID_LACHRYMA_SOURCE_CONVERSION = registerBoolean("void_lachryma_source_conversion", GameRuleCategory.UPDATES, false);

    private static GameRule<Boolean> registerBoolean(final String id, final GameRuleCategory category, final boolean defaultValue) {
        return register(
                id, category, GameRuleType.BOOL, BoolArgumentType.bool(), Codec.BOOL, defaultValue, FeatureFlagSet.of(), GameRuleTypeVisitor::visitBoolean, b -> b ? 1 : 0
        );
    }

    private static <T> GameRule<T> register(
            final String name,
            final GameRuleCategory category,
            final GameRuleType typeHint,
            final ArgumentType<T> argumentType,
            final Codec<T> codec,
            final T defaultValue,
            final FeatureFlagSet requiredFeatures,
            final GameRules.VisitorCaller<T> visitorCaller,
            final ToIntFunction<T> commandResultFunction
    ) {
        return Registry.register(
                BuiltInRegistries.GAME_RULE,
                Enderscape.id(name),
                new GameRule<>(category, typeHint, argumentType, visitorCaller, codec, commandResultFunction, defaultValue, requiredFeatures)
        );
    }
}
