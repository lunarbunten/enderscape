package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.drifter.AbstractDrifter;
import net.bunten.enderscape.entity.drifter.DrifterAI;
import net.bunten.enderscape.registry.tag.EnderscapePoiTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager.Occupancy;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DrifterRefreshHomePosition extends Behavior<AbstractDrifter> {

    private static final UniformInt NEXT_COOLDOWN_RANGE = UniformInt.of(60, 120);

    private static final int MAX_HOME_SEARCH_DISTANCE = 128;

    private static final int HORIZONTAL_UPDATE_RANGE = 8;
    private static final int VERTICAL_UPDATE_RANGE = 64;

    public DrifterRefreshHomePosition() {
        super(ImmutableMap.of(EnderscapeMemory.DRIFTER_FIND_HOME_COOLDOWN, MemoryStatus.VALUE_ABSENT));
    }

    protected int sampleNextCooldown(AbstractDrifter mob) {
        return NEXT_COOLDOWN_RANGE.sample(mob.getRandom()) * 20;
    }

    protected void updateOtherDrifters(ServerLevel level, AbstractDrifter mob) {
        level.getEntitiesOfClass(AbstractDrifter.class, new AABB(mob.blockPosition()).inflate(HORIZONTAL_UPDATE_RANGE, VERTICAL_UPDATE_RANGE, HORIZONTAL_UPDATE_RANGE)).forEach((other -> {
            DrifterAI.setHome(other, DrifterAI.getHome(mob));
            other.getBrain().setMemory(EnderscapeMemory.DRIFTER_FIND_HOME_COOLDOWN, sampleNextCooldown(mob));
        }));
    }

    protected List<BlockPos> findPossibleHomes(ServerLevel level, AbstractDrifter mob) {
        Stream<PoiRecord> stream = level.getPoiManager().getInRange(holder -> holder.is(EnderscapePoiTags.DRIFTER_HOME), mob.blockPosition(), MAX_HOME_SEARCH_DISTANCE, Occupancy.ANY);
        return stream.map(PoiRecord::getPos).collect(Collectors.toList());
    }

    protected Optional<BlockPos> findNewHome(ServerLevel level, AbstractDrifter mob) {
        List<BlockPos> possibles = findPossibleHomes(level, mob);
        return possibles.isEmpty() ? Optional.empty() : possibles.stream().findAny();
    }

    @Override
    protected void start(ServerLevel level, AbstractDrifter mob, long l) {
        Optional<BlockPos> newHome = findNewHome(level, mob);
        if (newHome.isPresent()) {
            DrifterAI.setHome(mob, GlobalPos.of(level.dimension(), newHome.get()));
            updateOtherDrifters(level, mob);
        }
        mob.getBrain().setMemory(EnderscapeMemory.DRIFTER_FIND_HOME_COOLDOWN, sampleNextCooldown(mob));
    }
}