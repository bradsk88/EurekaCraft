package ca.bradj.eurekacraft.world.storm;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.EurekaCraftNetwork;
import ca.bradj.eurekacraft.core.network.msg.TraparStormMessage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StormSavedData extends WorldSavedData {

    private static final float TRAPAR_PER_TICK = 0.01f;

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public boolean storming = false;
    private float traparLevel = 0.0f;

    public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "storm_saved_data");

    public StormSavedData(String p_i2141_1_) {
        super(p_i2141_1_);
    }

    public StormSavedData() {
        super(ID.toString());
    }

    public void tick(World world) {
        if (world.isClientSide()) {
            return;
        }
        if (storming) {
            this.peterOut();
        } else {
            this.buildUp();
        }
        logger.debug("Trapar level is " + this.traparLevel);
    }

    private void buildUp() {
        this.traparLevel += TRAPAR_PER_TICK;
        if (traparLevel >= 1.0f) {
            this.storming = true;
            EurekaCraftNetwork.CHANNEL.send(
                    PacketDistributor.ALL.noArg(), // TODO: Consider limiting reach
                    new TraparStormMessage(true)
            );
        }
    }

    private void peterOut() {
        this.traparLevel -= TRAPAR_PER_TICK;
        if (traparLevel <= 0.0f) {
            this.storming = false;
            EurekaCraftNetwork.CHANNEL.send(
                    PacketDistributor.ALL.noArg(), // TODO: Consider limiting reach
                    new TraparStormMessage(false)
            );
        }
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.storming = nbt.getBoolean("storming");
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putBoolean("storming", this.storming);
        return nbt;
    }
}
