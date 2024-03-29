package ca.bradj.eurekacraft.world.storm;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.EurekaCraftNetwork;
import ca.bradj.eurekacraft.core.network.msg.TraparStormMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StormSavedData extends SavedData {

    public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "storm_saved_data");
    private static final StormSavedData NOT_STORMING = new StormSavedData(ID.toString());
    private static Map<ChunkPos, StormSavedData> chunkData = new HashMap<>();

    public static final float DEFAULT_TRAPAR_PER_100_TICK = 0.005f;
    private static final float MAX_TRAPAR_GAIN_PER_100_TICK = 0.01f;

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private static int ticks = 0;
    private final ChunkPos pos;
    private final float gainPer100Tick;
    private final float lossPer100Tick;

    public boolean storming = false;
    private float traparLevel;

    private StormSavedData(String id, ChunkPos pos, float gainPerTick) {
        super();
        this.gainPer100Tick = gainPerTick;
        this.lossPer100Tick = 40 * gainPerTick;
        this.pos = pos;
        this.traparLevel = new Random().nextFloat();
    }


    public StormSavedData(String p_i2141_1_) {
        this(p_i2141_1_, new ChunkPos(0), DEFAULT_TRAPAR_PER_100_TICK);
    }

    public StormSavedData(long seed, ChunkPos cp) {
        this(ID.toString(), cp, gainFromSeedAndChunk(seed, cp));
    }

    private static float gainFromSeedAndChunk(long seed, ChunkPos cp) {
        long posSeed = (100L * cp.x) + cp.z;
        return new Random(seed + posSeed).nextFloat() * MAX_TRAPAR_GAIN_PER_100_TICK;
    }

    public static StormSavedData forChunk(ChunkPos cp) {
        return chunkData.getOrDefault(cp, StormSavedData.NOT_STORMING);
    }

    public static void initChunk(long seed, ChunkPos pos) {
        if (chunkData.containsKey(pos)) {
            return;
        }
        chunkData.put(pos, new StormSavedData(seed, pos));
    }

    public static void tick(Level world) {
        if (world.isClientSide()) {
            return;
        }
        if (chunkData.isEmpty()) {
            return;
        }
        if (ticks < 100) {
            ticks++;
            return;
        }
        ticks = 0;
        for (Map.Entry<ChunkPos, StormSavedData> e : chunkData.entrySet()) {
            if (e.getValue().storming) {
                e.getValue().peterOut();
            } else {
                e.getValue().buildUp();
            }
        }
        logTraparForPlayers(world);
    }

    private static void logTraparForPlayers(Level world) {
        for (Player p : world.players()) {
            BlockPos bp = p.blockPosition();
            StormSavedData d = chunkData.get(new ChunkPos(bp));
            if (d == null) {
                logger.trace("Near " + p.getName().getContents() + ": null");
            } else {
                logger.trace("Near " + p.getName().getContents() + ": storming[" + d.storming + "], level[" + d.traparLevel + "], rate["+ d.gainPer100Tick +"], p["+ d.pos+"]");
            }
        }
    }

    public static void removeChunk(ChunkPos pos) {
        chunkData.remove(pos);
    }

    public static void updateFromMessage(TraparStormMessage traparStormMessage) {
        ChunkPos cp = traparStormMessage.chunkPos;
        StormSavedData defaultValue = new StormSavedData(0, cp);
        StormSavedData current = chunkData.getOrDefault(cp, defaultValue);
        current.storming = traparStormMessage.isStorming();
        chunkData.put(cp, current);
    }

    public static StormSavedData forBlockPosition(BlockPos blockPosition) {
        return forChunk(new ChunkPos(blockPosition));
    }

    public static void triggerTraparExplosion(BlockPos blockPosition, int blockRadius, float intensity) {
        ChunkPos cp = new ChunkPos(blockPosition);
        forChunk(cp).traparLevel = intensity;
        // TODO: Implement true radius
        for (Direction d : Direction.Plane.HORIZONTAL) {
            for (int i = 1; i < blockRadius; i++) {
                int stepX = d.getStepX() * blockRadius;
                int stepZ = d.getStepZ() * blockRadius;
                forChunk(new ChunkPos(cp.x + stepX, cp.z + stepZ)).traparLevel = intensity;
            }
        }
    }

    private void buildUp() {
        this.traparLevel += this.gainPer100Tick;
        if (traparLevel >= 1.0f && !this.storming) {
            this.storming = true;
            EurekaCraftNetwork.CHANNEL.send(
                    PacketDistributor.ALL.noArg(), // TODO: Consider limiting reach
                    new TraparStormMessage(this.pos, true)
            );
        }
        setDirty();
    }

    private void peterOut() {
        this.traparLevel -= this.lossPer100Tick;
        if (traparLevel <= 0.0f && this.storming) {
            this.storming = false;
            EurekaCraftNetwork.CHANNEL.send(
                    PacketDistributor.ALL.noArg(),
                    new TraparStormMessage(this.pos, false)
            );
        }
        setDirty();
    }

    // TODO: Reimplement?
//    @Override
//    public void load(CompoundTag nbt) {
//        this.storming = nbt.getBoolean("storming");
//    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean("storming", this.storming);
        return nbt;
    }
}
