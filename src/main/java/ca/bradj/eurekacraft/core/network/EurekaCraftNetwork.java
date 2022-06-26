package ca.bradj.eurekacraft.core.network;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.msg.BoardControlMessage;
import ca.bradj.eurekacraft.core.network.msg.DeployedBoardMessage;
import ca.bradj.eurekacraft.core.network.msg.TraparStormMessage;
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
        registerMessage(BoardControlMessage.class, NetworkDirection.PLAY_TO_CLIENT).
                encoder(BoardControlMessage::encode).
                decoder(BoardControlMessage::decode).
                consumer(BoardControlMessage::handle).
                add();
    }

    public static <T> SimpleChannel.MessageBuilder<T> registerMessage(Class<T> msgClass, NetworkDirection dir) {
        return CHANNEL.messageBuilder(msgClass, messageIndex++, dir);
    }
}
