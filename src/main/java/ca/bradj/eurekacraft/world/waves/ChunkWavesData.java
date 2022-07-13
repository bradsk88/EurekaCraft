package ca.bradj.eurekacraft.world.waves;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.msg.ChunkWavesMessage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class ChunkWavesData {

    private Map<BlockPos, Boolean> waveBlocks = ImmutableMap.of();

    public ChunkWavesData(Map<BlockPos, Boolean> waveBlocks) {
        super();
        this.waveBlocks = waveBlocks;
    }

    public boolean isWavePresentAt(BlockPos bp) {
        return waveBlocks.containsKey(bp);
    }

    public static ChunkWavesData generate(Random rand, ChunkPos cp) {
        // TODO: Randomize number? Get from config;
        int numWaves = 20;
        int xRange = cp.getMaxBlockX() - cp.getMinBlockX();
        int zRange = cp.getMaxBlockZ() - cp.getMinBlockZ();
        Map<BlockPos, Boolean> wavesMap = new HashMap<>(numWaves);
        for (int i = 0; i < numWaves; i++) {
            wavesMap.put(new BlockPos(
                cp.getMinBlockX() + rand.nextInt(xRange),
                    rand.nextInt(256), // TODO: Get max height from world
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
