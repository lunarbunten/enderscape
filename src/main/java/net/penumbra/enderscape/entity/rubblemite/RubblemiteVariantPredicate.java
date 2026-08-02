package net.penumbra.enderscape.entity.rubblemite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record RubblemiteVariantPredicate(Optional<Holder<RubblemiteVariant>> variant) implements EntitySubPredicate {

    public static final Codec<RubblemiteVariantPredicate> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(RubblemiteVariant.CODEC.optionalFieldOf("variant").forGetter(RubblemiteVariantPredicate::variant)).apply(instance, RubblemiteVariantPredicate::new)
    );

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 vec3) {
        if (entity instanceof Rubblemite rubblemite) return variant.isPresent() && rubblemite.getVariant() == variant.get();

        return false;
    }
}