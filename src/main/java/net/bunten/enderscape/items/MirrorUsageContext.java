package net.bunten.enderscape.items;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MirrorUsageContext extends ChargedUsageContext {

    public MirrorUsageContext(ItemStack stack, Level level, Player player) {
        super(stack, level, player);
    }

    public MirrorItem getMirrorItem() {
        return (MirrorItem) getChargedItem();
    }
    
    @Nullable
    public BlockPos getLinkedPos() {
        return MirrorData.pos(getStack());
    }

    public ResourceKey<Level> getUsageDimension() {
        return getUsageLevel().dimension();
    }

    @Nullable
    public ResourceKey<Level> getLinkedDimension() {
        return MirrorData.dimension(getStack());
    }

    public ServerLevel getLinkedLevel() {
        return getUsageLevel().getServer().getLevel(getLinkedDimension());
    }
}