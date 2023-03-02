package ca.bradj.eurekacraft.world.waves;

import ca.bradj.eurekacraft.core.network.EurekaCraftNetwork;
import ca.bradj.eurekacraft.core.network.msg.ChunkWavesMessage;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChunkWavesDataManager extends SavedData {

    private static Map<ChunkPos, ChunkWavesData> chunkData = new HashMap<>();

    private static int tickCounter = 10;

    @Nonnull
    public static ChunkWavesDataManager get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("Client side access is not allowed");
        }
        DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
        return storage.computeIfAbsent(ChunkWavesDataManager::new, ChunkWavesDataManager::new, "chunk_wave_data_manager");
    }

    public static void tick(ServerLevel world) {
        tickCounter--;
        if (tickCounter <= 0) {
            tickCounter = 10;
            world.players().forEach(p -> {
                ChunkPos cp = p.chunkPosition();
                ChunkWavesData data = ChunkWavesDataManager.get(world).getData(
                        world.getChunk(cp.x, cp.z), world.getRandom()
                );
//                EurekaCraft.LOGGER.trace("Waves at " + cp + ": " + data.getWaves());
                EurekaCraftNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> p),
                        new ChunkWavesMessage(cp, data.getWaves())
                );
            });
        }
    }

    public ChunkWavesData getData(ChunkAccess ca, Random rand) {
        ChunkWavesData chunkWavesData = chunkData.computeIfAbsent(ca.getPos(), cp -> {
            ChunkWavesData data = ChunkWavesData.generate(ca, rand);
            setDirty();
            return data;
        });
        if (chunkWavesData.generateRavineWaves(ca, rand)) {
            setDirty();
        }
        return chunkWavesData;
    }

    public void initData(ChunkAccess ca, Random rand) {
        getData(ca, rand);
    }

    public ChunkWavesDataManager() {
    }

    public ChunkWavesDataManager(CompoundTag tag) {
        ListTag chunks = tag.getList("chunk_wave_data", Tag.TAG_COMPOUND);
        for (Tag t : chunks) {
            CompoundTag chunkWaves = (CompoundTag) t;
            int chunkX = chunkWaves.getInt("chunk_x");
            int chunkZ = chunkWaves.getInt("chunk_y");
            ChunkPos pos = new ChunkPos(chunkX, chunkZ);
            ListTag waves = chunkWaves.getList("waves", Tag.TAG_COMPOUND);
            Map<BlockPos, Boolean> wavesMap = new HashMap<>(waves.size());
            waves.forEach(t2 -> {
                CompoundTag waveTag = (CompoundTag) t2;
                int x = waveTag.getInt("wave_block_x");
                int y = waveTag.getInt("wave_block_y");
                int z = waveTag.getInt("wave_block_z");
                wavesMap.put(new BlockPos(x, y, z), true);
            });
            chunkData.put(pos, new ChunkWavesData(wavesMap));
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag chunks = new ListTag();
        chunkData.forEach((key, value) -> {
            CompoundTag chunkWaves = new CompoundTag();
            chunkWaves.putInt("chunk_x", key.x);
            chunkWaves.putInt("chunk_z", key.z);
            ListTag waves = new ListTag();
            value.getWaves().forEach(wave -> {
                CompoundTag waveTag = new CompoundTag();
                waveTag.putInt("wave_block_x", wave.getX());
                waveTag.putInt("wave_block_y", wave.getY());
                waveTag.putInt("wave_block_z", wave.getZ());
                waves.add(waveTag);
            });
            chunkWaves.put("waves", waves);
        });
        tag.put("chunk_wave_data", chunks);
        return tag;
    }

    public static ChunkWavesData getForClient(ChunkPos cp) {
        return chunkData.getOrDefault(cp, ChunkWavesData.fromCollection(ImmutableList.of()));
    }

    public static void updateFromMessage(ChunkWavesMessage msg) {
        chunkData.put(msg.chunkPos, ChunkWavesData.fromCollection(msg.waveBlocks));
    }
}
