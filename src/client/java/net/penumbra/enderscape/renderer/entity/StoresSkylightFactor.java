package net.penumbra.enderscape.renderer.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;

public interface StoresSkylightFactor {
    static <E extends Entity, S extends EntityRenderState> void extract(E entity, S state) {
        if (state instanceof StoresSkylightFactor improved) {
            float skyLightFactor = entity.level().environmentAttributes().getValue(EnvironmentAttributes.SKY_LIGHT_FACTOR, entity.position());
            improved.setSkyLightFactor(skyLightFactor);
        }
    }

    float skyLightFactor();
    void setSkyLightFactor(float value);
}