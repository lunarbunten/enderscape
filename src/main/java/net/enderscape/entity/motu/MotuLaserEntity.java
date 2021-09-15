package net.enderscape.entity.motu;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MotuLaserEntity extends AbstractFireballEntity {
    public MotuLaserEntity(EntityType<? extends MotuLaserEntity> entityType, World world) {
        super(entityType, world);
    }

    public MotuLaserEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ) {
        super(EntityType.SMALL_FIREBALL, owner, velocityX, velocityY, velocityZ, world);
    }

    public MotuLaserEntity(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(EntityType.SMALL_FIREBALL, x, y, z, velocityX, velocityY, velocityZ, world);
    }

    public void tick() {
        super.tick();
        if (age > 20) {
            if (world instanceof ServerWorld) {
                Vec3d pos = getPos();
                ((ServerWorld) world).spawnParticles(ParticleTypes.POOF, pos.x, pos.y + 0.5, pos.z, 5, 0, 0, 0, 0.1);
            }
            playSound(SoundEvents.ENTITY_SHULKER_BULLET_HURT, 1, 1.2F);
            remove(RemovalReason.DISCARDED);
        }
    }

    protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        if (!world.isClient()) {
            Entity entity = result.getEntity();
            Entity owner = getOwner();
            if (entity instanceof MotuEntity) {
                // dont
            } else {
                if (owner instanceof LivingEntity) {
                    float damage = (float) ((LivingEntity) owner).getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    if (entity.damage(DamageSource.mob((LivingEntity) owner), damage)) {
                        applyDamageEffects((LivingEntity) owner, entity);
                        playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 2);
                    }
                }
            }
        }
    }

    protected void onCollision(HitResult result) {
        super.onCollision(result);
        if (result.getType() == Type.ENTITY && ((EntityHitResult) result).getEntity() instanceof MotuEntity) {

        } else if (!world.isClient()) {
            remove(RemovalReason.DISCARDED);
        }
    }

    public boolean collides() {
        return false;
    }

    public boolean damage(DamageSource source, float amount) {
        return false;
    }
}