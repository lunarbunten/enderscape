package net.bunten.enderscape.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChargedUsageContext {

    private final ItemStack stack;
    private final Level level;
    private final LivingEntity user;

    public ChargedUsageContext(ItemStack stack, Level level, LivingEntity user) {
        this.stack = stack;
        this.level = level;
        this.user = user;
    }

    public ItemStack getStack() {
        return stack;
    }

    public NebuliteChargedItem getChargedItem() {
        return (NebuliteChargedItem) stack.getItem();
    }

    public Level getUsageLevel() {
        return level;
    }

    public ServerLevel getUsageServerLevel() {
        return (ServerLevel) level;
    }

    public LivingEntity getUser() {
        return user;
    }

    public Player getPlayer() {
        return (Player) getUser();
    }

    public ServerPlayer getServerPlayer() {
        return (ServerPlayer) getPlayer();
    }
}