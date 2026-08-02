package net.penumbra.enderscape.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemStackContext {

    private final ItemStack stack;
    private final Level level;
    private final LivingEntity user;

    public ItemStackContext(ItemStack stack, Level level, LivingEntity user) {
        this.stack = stack;
        this.level = level;
        this.user = user;
    }

    public ItemStack stack() {
        return stack;
    }

    public Level level() {
        return level;
    }

    public ServerLevel serverLevel() {
        if (level instanceof ServerLevel server) {
            return server;
        } else throw new IllegalStateException(stack.getItem() + " requested server level from client-sided context");
    }

    public LivingEntity user() {
        return user;
    }
}