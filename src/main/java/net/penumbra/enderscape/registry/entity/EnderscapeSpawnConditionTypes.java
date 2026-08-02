package net.penumbra.enderscape.registry.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.level.block.Block;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeSpawnConditionTypes {

    public record FloorCheck(TagKey<Block> requiredBlocks) implements SpawnCondition {
        public static final MapCodec<FloorCheck> DATA_CODEC = RecordCodecBuilder.mapCodec(
                instance ->
                        instance.group(
                                        TagKey.codec(Registries.BLOCK)
                                                .fieldOf("blocks")
                                                .forGetter(FloorCheck::requiredBlocks)
                                )
                                .apply(instance, FloorCheck::new)
        );

        public static final KeyDispatchDataCodec<FloorCheck> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

        public boolean test(SpawnContext context) {
            return context.level().getBlockState(context.pos().below()).is(requiredBlocks);
        }

        @Override
        public MapCodec<FloorCheck> codec() {
            return DATA_CODEC;
        }
    }

    private static MapCodec<? extends SpawnCondition> register(String string, KeyDispatchDataCodec<? extends SpawnCondition> dispatch) {
        return Registry.register(BuiltInRegistries.SPAWN_CONDITION_TYPE, Enderscape.id(string), dispatch.codec());
    }

    static {
        register("floor_check", FloorCheck.CODEC);
    }
}