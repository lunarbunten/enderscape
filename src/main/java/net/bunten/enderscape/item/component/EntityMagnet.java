package net.bunten.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.network.EnderscapeCodecs;
import net.bunten.enderscape.registry.EnderscapeDataComponents;
import net.bunten.enderscape.registry.EnderscapeEnchantmentEffectComponents;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.bunten.enderscape.registry.tag.EnderscapeEntityTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.ENTITY_MAGNET;

public record EntityMagnet(
        TagKey<EntityType<?>> entitiesToPull,
        TagKey<EntityType<?>> exemptFromAbuseCost,
        boolean canDepositIntoBundlesByDefault,
        Vec2 pullRange,
        Holder<SoundEvent> pullEntitySound
) {

    public static final BiPredicate<Entity, EntityMagnet> CAN_PULL_ENTITY = (entity, magnet) -> {
        if (entity.getType().is(magnet.entitiesToPull())) {
            if (entity instanceof ItemEntity item) return !item.hasPickUpDelay();
            return entity.tickCount >= 20;
        } else {
            return false;
        }
    };

    public static final TagKey<EntityType<?>> DEFAULT_ENTITIES_TO_PULL = EnderscapeEntityTags.PULLED_BY_MAGNIA_ATTRACTOR;
    public static final TagKey<EntityType<?>> DEFAULT_EXEMPT_FROM_ABUSE_COST = EnderscapeEntityTags.EXEMPT_FROM_MAGNIA_ATTRACTOR_ABUSE_COST;

    public static final boolean DEFAULT_CAN_DEPOSIT_INTO_BUNDLES_BY_DEFAULT = false;

    public static final Vec2 DEFAULT_PULL_RANGE = new Vec2(10.0F, 4.0F);

    public static final Holder.Reference<SoundEvent> DEFAULT_USE_FUEL_SOUND = EnderscapeItemSounds.MAGNIA_ATTRACTOR_USE_FUEL;
    public static final Holder.Reference<SoundEvent> DEFAULT_PULL_ENTITY_SOUND = EnderscapeItemSounds.MAGNIA_ATTRACTOR_MOVE;

    public static final Codec<EntityMagnet> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    TagKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("entities_to_pull", DEFAULT_ENTITIES_TO_PULL).forGetter(EntityMagnet::entitiesToPull),
                    TagKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("exempt_from_abuse_cost", DEFAULT_EXEMPT_FROM_ABUSE_COST).forGetter(EntityMagnet::exemptFromAbuseCost),
                    Codec.BOOL.optionalFieldOf("can_deposit_into_bundles_by_default", DEFAULT_CAN_DEPOSIT_INTO_BUNDLES_BY_DEFAULT).forGetter(EntityMagnet::canDepositIntoBundlesByDefault),
                    EnderscapeCodecs.VEC2.optionalFieldOf("pull_range", DEFAULT_PULL_RANGE).forGetter(EntityMagnet::pullRange),
                    SoundEvent.CODEC.optionalFieldOf("pull_entity_sound", DEFAULT_PULL_ENTITY_SOUND).forGetter(EntityMagnet::pullEntitySound)
            ).apply(instance, EntityMagnet::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, EntityMagnet> STREAM_CODEC = StreamCodec.composite(
            streamCodec(Registries.ENTITY_TYPE),
            EntityMagnet::entitiesToPull,
            streamCodec(Registries.ENTITY_TYPE),
            EntityMagnet::exemptFromAbuseCost,
            ByteBufCodecs.BOOL,
            EntityMagnet::canDepositIntoBundlesByDefault,
            EnderscapeCodecs.VEC2_STREAM,
            EntityMagnet::pullRange,
            SoundEvent.STREAM_CODEC,
            EntityMagnet::pullEntitySound,
            EntityMagnet::new
    );

    public static final EntityMagnet DEFAULT = new EntityMagnet(
            DEFAULT_ENTITIES_TO_PULL,
            DEFAULT_EXEMPT_FROM_ABUSE_COST,
            DEFAULT_CAN_DEPOSIT_INTO_BUNDLES_BY_DEFAULT,
            DEFAULT_PULL_RANGE,
            DEFAULT_PULL_ENTITY_SOUND
    );

    public static <T> StreamCodec<ByteBuf, TagKey<T>> streamCodec(ResourceKey<? extends Registry<T>> key) {
        return ResourceLocation.STREAM_CODEC.map((location) -> TagKey.create(key, location), TagKey::location);
    }

    public static boolean is(ItemStack stack) {
        return stack.has(EnderscapeDataComponents.ENTITY_MAGNET);
    }

    public static EntityMagnet get(ItemStack stack) {
        return stack.get(EnderscapeDataComponents.ENTITY_MAGNET);
    }

    public static boolean tryAddToBundle(Inventory inventory, ItemStack toAdd) {
        ItemStack magnet = getFirstUsableMagnet(inventory);

        if (magnet.isEmpty() || !Enabled.get(magnet) || !EntityMagnet.canDepositIntoBundles(magnet, inventory.player)) {
            return false;
        }

        for (ItemStack stack : inventory.items) {
            if (stack.getItem() instanceof BundleItem && stack.has(DataComponents.BUNDLE_CONTENTS)) {
                BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
                for (ItemStack bundle : contents.items()) {
                    if (ItemStack.isSameItemSameComponents(bundle, toAdd)) {
                        BundleContents.Mutable mutableContents = new BundleContents.Mutable(contents);
                        if (mutableContents.tryInsert(toAdd) > 0) {
                            stack.set(DataComponents.BUNDLE_CONTENTS, mutableContents.toImmutable());
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static ItemStack getFirstUsableMagnet(Inventory inventory) {
        Player player = inventory.player;
        Level level = player.level();

        Predicate<ItemStack> valid = stack -> EntityMagnet.is(stack) && Enabled.get(stack) && (!FueledTool.is(stack) || FueledTool.fuelExceedsCost(new ItemStackContext(stack, level, player)));

        if (valid.test(player.getMainHandItem())) return player.getMainHandItem();
        if (valid.test(player.getOffhandItem())) return player.getOffhandItem();

        return inventory.items.stream().filter(valid).findFirst().orElse(ItemStack.EMPTY);
    }

    public static int abuseCost(Entity entity, EntityMagnet entityMagnet) {
        return entity.getType().is(entityMagnet.exemptFromAbuseCost()) ? 0 : 1;
    }

    public static boolean canDepositIntoBundles(ItemStack stack, LivingEntity user) {
        if (EntityMagnet.is(stack)) {
            EntityMagnet entityMagnet = get(stack);
            return entityMagnet.canDepositIntoBundlesByDefault() || canAdditionallyDepositIntoBundles(stack, user);
        } else throw new IllegalStateException(stack.getItem() + " missing component of " + ENTITY_MAGNET);
    }

    public static boolean canAdditionallyDepositIntoBundles(ItemStack stack, LivingEntity user) {
        MutableFloat mutable = new MutableFloat(0);
        EnchantmentHelper.runIterationOnItem(stack, (holder, i) -> holder.value().modifyUnfilteredValue(EnderscapeEnchantmentEffectComponents.MAGNET_ENABLE_DEPOSIT_INTO_BUNDLES, user.getRandom(), i, mutable));
        return mutable.floatValue() > 0;
    }
}