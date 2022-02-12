package ca.bradj.eurekacraft.core.network;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.msg.DeployedBoardMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class EurekaCraftNetwork {

    public static final String NETWORK_VERSION = "0.0.1";

    private static int messageIndex = 0;

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(EurekaCraft.MODID, "network"),
            () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION),
            version -> version.equals(NETWORK_VERSION)
    );

    public static void initClientMessages() {
        registerMessage(DeployedBoardMessage.class, NetworkDirection.PLAY_TO_CLIENT).
                encoder(DeployedBoardMessage::encode).
                decoder(DeployedBoardMessage::decode).
                consumer(DeployedBoardMessage::handle).
                add();
    }

    public static <T> SimpleChannel.MessageBuilder<T> registerMessage(Class<T> msgClass, NetworkDirection dir) {
        return CHANNEL.messageBuilder(msgClass, messageIndex++, dir);
    }
}
