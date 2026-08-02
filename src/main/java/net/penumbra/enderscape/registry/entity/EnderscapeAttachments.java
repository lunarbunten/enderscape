package net.penumbra.enderscape.registry.entity;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.Enderscape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EnderscapeAttachments {

    public static final List<AttachmentType<?>> ATTACHMENT_TYPES = new ArrayList<>();
    public static final List<AttachmentType<?>> PERSIST_WHILE_DYING = new ArrayList<>();

    public static final AttachmentType<LevelData.RespawnData> END_HAVEN_RESPAWN_DATA = register(
            "end_haven_respawn_data",
            true,
            builder -> builder.initializer(
                    () -> new LevelData.RespawnData(new GlobalPos(Level.OVERWORLD, BlockPos.containing(0, 0, 0)), 0.0F, 0.0F)
            ).syncWith(LevelData.RespawnData.STREAM_CODEC, AttachmentSyncPredicate.targetOnly()).copyOnDeath().persistent(LevelData.RespawnData.CODEC)
    );

    public static final AttachmentType<Unit> PENDING_END_HAVEN_RESPAWN = register(
            "pending_end_haven_respawn",
            true,
            builder -> builder.initializer(() -> Unit.INSTANCE).persistent(Unit.CODEC)
    );

    public static final AttachmentType<Unit> PERFORMING_STUN_ATTACK = register(
            "performing_stun_attack",
            builder -> builder.initializer(() -> Unit.INSTANCE)
    );

    public static final AttachmentType<Integer> IDLE_TICKS = register(
            "idle_ticks",
            builder -> builder.initializer(() -> 0).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> VOID_DESTRUCTION_TICKS = register(
            "void_destruction_ticks",
            builder -> builder.initializer(() -> 0).persistent(Codec.INT)
    );

    public static final AttachmentType<Vec3> LAST_POSITION = register(
            "last_position",
            builder -> builder.initializer(() -> Vec3.ZERO).persistent(Vec3.CODEC)
    );

    public static final AttachmentType<Integer> MIDAIR_TICKS = register(
            "midair_ticks",
            builder -> builder.initializer(() -> 0).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> START_GLIDING_SOUND_COOLDOWN = register(
            "start_gliding_sound_cooldown",
            builder -> builder.initializer(() -> 0).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> STOP_GLIDING_SOUND_COOLDOWN = register(
            "stop_gliding_sound_cooldown",
            builder -> builder.initializer(() -> 0).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> ELYTRA_GROUND_TICKS = register(
            "elytra_ground_ticks",
            builder -> builder.initializer(() -> 0).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> MAGNIA_COOLDOWN_TIME = register(
            "magnia_cooldown_time",
            builder -> builder.initializer(() -> 0).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> DASH_JUMP_CHARGING_TICKS = register(
            "dash_jump_charging_ticks",
            builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.targetOnly()).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> DASH_JUMP_TICKS = register(
            "dash_jump_ticks",
            builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all()).persistent(Codec.INT)
    );

    public static final AttachmentType<Boolean> HAS_END_TRIAL_SPAWNER_EFFECTS = register(
            "has_end_trial_spawner_effects",
            builder -> builder.initializer(() -> false).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.allButTarget()).persistent(Codec.BOOL)
    );

    public static final AttachmentType<Float> VOIDED_HEALTH = register(
            "voided_health",
            true,
            builder -> builder.initializer(() -> 0.0F).syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all()).persistent(Codec.FLOAT)
    );

    public static final AttachmentType<Integer> VOID_IMMUNITY_TICKS = register(
            "void_immunity_ticks",
            builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.targetOnly()).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> OUTER_VOID_TICKS = register(
            "outer_void_ticks",
            builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all()).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> OUTER_VOID_TICK_REMOVAL_DELAY = register(
            "outer_void_tick_removal_delay",
            true,
            builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.targetOnly()).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> VOID_TICKS = register(
            "void_ticks",
            builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all()).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> VOID_TICK_DOWN_DELAY = register(
            "void_tick_down_delay",
            builder -> builder.initializer(() -> 0).persistent(Codec.INT).syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.targetOnly())
    );

    public static final AttachmentType<Integer> STUN_DURATION = register(
            "stun_duration",
            builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all()).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> STUN_TICKS = register(
            "stun_ticks",
            builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all()).persistent(Codec.INT)
    );

    public static final AttachmentType<Integer> ATTACK_ANIMATION_REMAINING_TICKS = register(
            "attack_animation_remaining_ticks",
            builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all()).persistent(Codec.INT)
    );

    private static <A> AttachmentType<A> register(String name, Consumer<AttachmentRegistry.Builder<A>> consumer) {
        return register(name, false, consumer);
    }

    private static <A> AttachmentType<A> register(String name, boolean persistWhileDying, Consumer<AttachmentRegistry.Builder<A>> consumer) {
        AttachmentType<A> type = AttachmentRegistry.create(Enderscape.id(name), consumer);
        ATTACHMENT_TYPES.add(type);
        if (persistWhileDying) PERSIST_WHILE_DYING.add(type);
        return type;
    }

    public static void afterLivingEntityDeath(LivingEntity entity, DamageSource source) {
        ATTACHMENT_TYPES.forEach(type -> {
            if (!PERSIST_WHILE_DYING.contains(type)) entity.removeAttached(type);
        });
    }
}