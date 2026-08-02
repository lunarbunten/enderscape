package net.penumbra.enderscape.entity;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LocalMobCapCalculator;
import net.minecraft.world.level.NaturalSpawner;
import net.penumbra.enderscape.registry.level.EnderscapeEnvironmentAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnderscapeMobCapCalculator extends LocalMobCapCalculator {

    private static final int MAGIC_NUMBER = (int)Math.pow(17.0, 2.0);

    private final ServerLevel level;
    private final ChunkMap chunkMap;

    private final Long2ObjectMap<List<ServerPlayer>> playersNearChunk = new Long2ObjectOpenHashMap<>();
    private final Map<ServerPlayer, Object2IntMap<MobCategory>> playerMobCounts = new HashMap<>();

    public EnderscapeMobCapCalculator(ChunkMap chunkMap, ServerLevel level) {
        super(chunkMap);

        this.chunkMap = chunkMap;
        this.level = level;
    }

    public static boolean isEnd(ServerLevel level) {
        return level.dimension() == Level.END;
    }

    public static int getMonsterSpawnCap(ServerLevel level) {
        return level.environmentAttributes().getDimensionValue(EnderscapeEnvironmentAttributes.MONSTER_SPAWN_CAP);
    }

    public static boolean shouldSpawnMoreMonsters(ServerLevel level, NaturalSpawner.SpawnState state) {
        int maxCount = getMonsterSpawnCap(level) * state.getSpawnableChunkCount() / MAGIC_NUMBER;
        return state.getMobCategoryCounts().getInt(MobCategory.MONSTER) < maxCount;
    }

    private List<ServerPlayer> getPlayersNear(final ChunkPos pos) {
        return this.playersNearChunk.computeIfAbsent(pos.pack(), _ -> this.chunkMap.getPlayersCloseForSpawning(pos));
    }

    private int chooseMobCap(MobCategory category) {
        if (category == MobCategory.MONSTER) {
            return getMonsterSpawnCap(level);
        } else {
            return category.getMaxInstancesPerChunk();
        }
    }

    @Override
    public void addMob(ChunkPos pos, MobCategory category) {
        for (ServerPlayer player : getPlayersNear(pos)) {
            playerMobCounts.computeIfAbsent(player, _ -> new Object2IntOpenHashMap<>()).mergeInt(category, 1, Integer::sum);
        }
    }

    @Override
    public boolean canSpawn(MobCategory category, ChunkPos pos) {
        for (ServerPlayer player : getPlayersNear(pos)) {
            Object2IntMap<MobCategory> counts = playerMobCounts.get(player);

            if (counts == null || counts.getInt(category) < chooseMobCap(category)) return true;
        }

        return false;
    }
}