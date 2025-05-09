package net.bunten.enderscape.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class NebuliteToolContext {

    private final ItemStack stack;
    private final Level level;
    private final LivingEntity user;

    public NebuliteToolContext(ItemStack stack, Level level, LivingEntity user) {
        this.stack = stack;
        this.level = level;
        this.user = user;
    }

    public ItemStack stack() {
        return stack;
    }

    @Nullable
    public NebuliteToolItem item() {
        if (stack.getItem() instanceof NebuliteToolItem nebuliteToolItem) {
            return nebuliteToolItem;
        }
        return null;
    }

    public Level level() {
        return level;
    }

    public ServerLevel serverLevel() {
        return (ServerLevel) level;
    }

    public LivingEntity user() {
        return user;
    }
}