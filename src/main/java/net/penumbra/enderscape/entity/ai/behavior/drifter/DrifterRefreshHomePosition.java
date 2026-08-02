package net.penumbra.enderscape.entity.ai.behavior.drifter;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager.Occupancy;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.phys.AABB;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.drifter.Drifter;
import net.penumbra.enderscape.entity.drifter.DrifterAI;
import net.penumbra.enderscape.registry.tag.EnderscapePoiTags;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DrifterRefreshHomePosition extends Behavior<Drifter> {

    private static final UniformInt NEXT_COOLDOWN_RANGE = UniformInt.of(60, 120);

    private static final int MAX_HOME_SEARCH_DISTANCE = 128;

    private static final int HORIZONTAL_UPDATE_RANGE = 8;
    private static final int VERTICAL_UPDATE_RANGE = 64;

    public DrifterRefreshHomePosition() {
        super(ImmutableMap.of(EnderscapeMemory.DRIFTER_FIND_HOME_COOLDOWN, MemoryStatus.VALUE_ABSENT));
    }

    protected int sampleNextCooldown(Drifter mob) {
        return NEXT_COOLDOWN_RANGE.sample(mob.getRandom()) * 20;
    }

    protected void updateOtherDrifters(ServerLevel level, Drifter mob) {
        level.getEntitiesOfClass(Drifter.class, new AABB(mob.blockPosition()).inflate(HORIZONTAL_UPDATE_RANGE, VERTICAL_UPDATE_RANGE, HORIZONTAL_UPDATE_RANGE)).forEach((other -> {
            other.setHomeTo(mob.getHomePosition(), DrifterAI.HOME_RADIUS);
            other.getBrain().setMemory(EnderscapeMemory.DRIFTER_FIND_HOME_COOLDOWN, sampleNextCooldown(mob));
        }));
    }

    protected List<BlockPos> findPossibleHomes(ServerLevel level, Drifter mob) {
        Stream<PoiRecord> stream = level.getPoiManager().getInRange(holder -> holder.is(EnderscapePoiTags.DRIFTER_HOME), mob.blockPosition(), MAX_HOME_SEARCH_DISTANCE, Occupancy.ANY);
        return stream.map(PoiRecord::getPos).collect(Collectors.toList());
    }

    protected Optional<BlockPos> findNewHome(ServerLevel level, Drifter mob) {
        List<BlockPos> possibles = findPossibleHomes(level, mob);
        return possibles.isEmpty() ? Optional.empty() : possibles.stream().findAny();
    }

    @Override
    protected void start(ServerLevel level, Drifter mob, long l) {
        Optional<BlockPos> newHome = findNewHome(level, mob);
        newHome.ifPresent((pos) -> {
            mob.setHomeTo(pos, DrifterAI.HOME_RADIUS);
            updateOtherDrifters(level, mob);
        });
        mob.getBrain().setMemory(EnderscapeMemory.DRIFTER_FIND_HOME_COOLDOWN, sampleNextCooldown(mob));
    }
}