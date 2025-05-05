package net.bunten.enderscape.entity.rubblemite;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record RubblemiteVariantPredicate(Optional<RubblemiteVariant> variant) implements EntitySubPredicate {

    public static final MapCodec<RubblemiteVariantPredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(RubblemiteVariant.CODEC.optionalFieldOf("variant").forGetter(RubblemiteVariantPredicate::variant)).apply(instance, RubblemiteVariantPredicate::new)
    );

    @Override
    public MapCodec<RubblemiteVariantPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel serverLevel, @Nullable Vec3 vec3) {
        if (entity instanceof Rubblemite rubblemite) {
            return variant.isPresent() && RubblemiteVariant.get(rubblemite) == variant.get();
        }

        return false;
    }
}
