package ca.bradj.eurekacraft.core.network;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.msg.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class EurekaCraftNetwork {

    public static final String NETWORK_VERSION = "0.0.1";

    private static int messageIndex = 0;

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(EurekaCraft.MODID, "network"),
            () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION),
            version -> version.equals(NETWORK_VERSION)
    );

    public static void init() {
        registerMessage(DeployedBoardMessage.class, NetworkDirection.PLAY_TO_CLIENT).
                encoder(DeployedBoardMessage::encode).
                decoder(DeployedBoardMessage::decode).
                consumer(DeployedBoardMessage::handle).
                add();
        registerMessage(TraparStormMessage.class, NetworkDirection.PLAY_TO_CLIENT).
                encoder(TraparStormMessage::encode).
                decoder(TraparStormMessage::decode).
                consumer(TraparStormMessage::handle).
                add();
        registerMessage(BoardControlMessage.class, NetworkDirection.PLAY_TO_SERVER).
                encoder(BoardControlMessage::encode).
                decoder(BoardControlMessage::decode).
                consumer(BoardControlMessage::handle).
                add();
        registerMessage(ChunkWavesMessage.class, NetworkDirection.PLAY_TO_CLIENT).
                encoder(ChunkWavesMessage::encode).
                decoder(ChunkWavesMessage::decode).
                consumer(ChunkWavesMessage::handle).
                add();
        registerMessage(OnGroundMessage.class, NetworkDirection.PLAY_TO_CLIENT).
                encoder(OnGroundMessage::encode).
                decoder(OnGroundMessage::decode).
                consumer(OnGroundMessage::handle).
                add();
    }

    public static <T> SimpleChannel.MessageBuilder<T> registerMessage(Class<T> msgClass, NetworkDirection dir) {
        return CHANNEL.messageBuilder(msgClass, messageIndex++, dir);
    }
}
