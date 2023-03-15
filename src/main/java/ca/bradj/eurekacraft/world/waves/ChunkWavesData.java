package ca.bradj.eurekacraft.world.waves;

import ca.bradj.eurekacraft.core.config.EurekaConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.*;

public class ChunkWavesData {

    private Map<BlockPos, Boolean> waveBlocks;
    private boolean generatedRavineWaves;

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

    public static ChunkWavesData generate(ChunkAccess ca, Random rand) {
        // TODO: Get upper and lower bound from config;
        ChunkPos cp = ca.getPos();
        int upperBound = EurekaConfig.wave_blobs_per_chunk_upper_bound.get();
        int lowerBound = EurekaConfig.wave_blobs_per_chunk_lower_bound.get();
        int numWaves = (int) ((Math.random() * (upperBound - lowerBound)) + lowerBound);
        int xRange = cp.getMaxBlockX() - cp.getMinBlockX();
        int zRange = cp.getMaxBlockZ() - cp.getMinBlockZ();
        Map<BlockPos, Boolean> wavesMap = new HashMap<>(numWaves);
        addLowWaves(cp, rand, numWaves, xRange, zRange, wavesMap);
        addHighWaves(cp, rand, numWaves, xRange, zRange, wavesMap);
        return new ChunkWavesData(wavesMap);
    }

    private static void addLowWaves(ChunkPos cp, Random rand, int numWaves, int xRange, int zRange, Map<BlockPos, Boolean> wavesMap) {
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
    }

    private static void addHighWaves(ChunkPos cp, Random rand, int numWaves, int xRange, int zRange, Map<BlockPos, Boolean> wavesMap) {
        for (int i = 0; i < numWaves / 2; i++) {
            wavesMap.put(new BlockPos(
                    cp.getMinBlockX() + rand.nextInt(xRange),
                    100 + rand.nextInt(100), // TODO: Get max height from world
                    cp.getMinBlockZ() + rand.nextInt(zRange)
            ), true);
        }
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

    public boolean generateRavineWaves(
            ChunkAccess ca, Random rand
    ) {
        ChunkStatus[] statusesToCheck = new ChunkStatus[]{
                ChunkStatus.HEIGHTMAPS, ChunkStatus.FULL
        };
        if (Arrays.stream(statusesToCheck).allMatch(v -> v != ca.getStatus())) {
            return false;
        }
        if (this.generatedRavineWaves) {
            return false;
        }

        ChunkPos cp = ca.getPos();

        int maxX = cp.getMaxBlockX();
        int minX = cp.getMinBlockX();
        int maxZ = cp.getMaxBlockZ();
        int minZ = cp.getMinBlockZ();
        int[][] ys = new int[maxX - minX][maxZ - minZ];

        int heightSum = 0;
        int blocks = 0;
        int i = 0; int j = 0;
        for (int x = minX; x < maxX; x++) {
            j = 0;
            for (int z = minZ; z < maxZ; z++) {
                blocks++;
                int y = ca.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                heightSum += y;
                ys[i][j] = y;
                j++;
            }
            i++;
        }

        int avgHeight = heightSum / blocks;

        i = 0;
        for (int x = minX; x < maxX; x++) {
            j = 0;
            for (int z = minZ; z < maxZ; z++) {
                int y = ys[i][j];
                if (y < avgHeight - 10) {
                    if (rand.nextBoolean()) {
                        this.waveBlocks.put(new BlockPos(x, y + 1, z), true);
                    }
                    if (rand.nextBoolean()) {
                        this.waveBlocks.put(new BlockPos(x, y + 5, z), true);
                    }
                    if (rand.nextBoolean()) {
                        this.waveBlocks.put(new BlockPos(x, y + 10, z), true);
                    }
                }
                j++;
            }
            i++;
        }
        this.generatedRavineWaves = true;
        return true;
    }
}
