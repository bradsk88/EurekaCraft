package ca.bradj.eurekacraft.world.waves;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;

import java.util.*;

public class ChunkWavesData {

    private Map<BlockPos, Boolean> waveBlocks = ImmutableMap.of();

    public ChunkWavesData(Map<BlockPos, Boolean> waveBlocks) {
        super();
        this.waveBlocks = waveBlocks;
    }

    public boolean isWavePresentAt(BlockPos bp) {
        BlockPos up = bp.relative(Direction.UP);
        BlockPos down = bp.relative(Direction.DOWN);
        return waveBlocks.containsKey(bp) ||
                waveBlocks.containsKey(up) ||
                waveBlocks.containsKey(down);
    }

    public static ChunkWavesData generate(Random rand, ChunkPos cp) {
        // TODO: Randomize number? Get from config;
        int numWaves = 10;
        int xRange = cp.getMaxBlockX() - cp.getMinBlockX();
        int zRange = cp.getMaxBlockZ() - cp.getMinBlockZ();
        Map<BlockPos, Boolean> wavesMap = new HashMap<>(numWaves);
        // Low waves
        for (int i = 0; i < numWaves; i++) {
            BlockPos bp = new BlockPos(
                    cp.getMinBlockX() + rand.nextInt(xRange),
                    rand.nextInt(100),
                    cp.getMinBlockZ() + rand.nextInt(zRange)
            );
            wavesMap.put(bp, true);
            for (Direction d : Direction.Plane.HORIZONTAL) {
                if (rand.nextBoolean()) {
                    wavesMap.put(bp.relative(d), true);
                    for (Direction d2 : Direction.Plane.HORIZONTAL) {
                        wavesMap.put(bp.relative(d).relative(d2), true);
                    }
                }
            }
        }
        // High waves
        for (int i = 0; i < numWaves; i++) {
            wavesMap.put(new BlockPos(
                    cp.getMinBlockX() + rand.nextInt(xRange),
                    100 + rand.nextInt(100), // TODO: Get max height from world
                    cp.getMinBlockZ() + rand.nextInt(zRange)
            ), true);
        }
        return new ChunkWavesData(wavesMap);
    }

    public static ChunkWavesData fromCollection(Collection<BlockPos> waveBlocks) {
        Map<BlockPos, Boolean> m = new HashMap<>(waveBlocks.size());
        for (BlockPos bp : waveBlocks) {
            m.put(bp, true);
        }
        return new ChunkWavesData(m);
    }

    public Set<BlockPos> getWaves() {
        return waveBlocks.keySet();
    }
}
