package net.bunten.enderscape.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MirrorContext extends NebuliteToolContext {

    public MirrorContext(ItemStack stack, Level level, LivingEntity user) {
        super(stack, level, user);
    }

    public static MirrorContext of(NebuliteToolContext context) {
        return new MirrorContext(context.stack(), context.level(), context.user());
    }
    
    @Nullable
    public BlockPos linkedPos() {
        if (stack().has(DataComponents.LODESTONE_TRACKER)) {
            LodestoneTracker tracker = stack().get(DataComponents.LODESTONE_TRACKER);
            if (tracker.target().isPresent()) return tracker.target().get().pos();
        }

        return null;
    }

    public ResourceKey<Level> dimension() {
        return level().dimension();
    }

    @Nullable
    public ResourceKey<Level> linkedDimension() {
        if (stack().has(DataComponents.LODESTONE_TRACKER)) {
            LodestoneTracker tracker = stack().get(DataComponents.LODESTONE_TRACKER);
            if (tracker.target().isPresent()) return tracker.target().get().dimension();
        }

        return null;
    }

    @Nullable
    public ServerLevel linkedLevel() {
        return level().getServer().getLevel(linkedDimension());
    }
}