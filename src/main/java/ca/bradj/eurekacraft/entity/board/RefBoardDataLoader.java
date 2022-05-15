package ca.bradj.eurekacraft.entity.board;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.vehicles.StandardRefBoard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class RefBoardDataLoader {

    public static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    @SubscribeEvent
    public static void PauseOrCloseServer(WorldEvent.Save event) {
        for (Player player : event.getWorld().players()) {
            storePlayBoard((ServerPlayer) player, (ServerLevel) event.getWorld());
        }
    }

    @SubscribeEvent
    public static void PlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getPlayer();
        if (!(player.level instanceof ServerLevel)) {
            return;
        }
        storePlayBoard((ServerPlayer) player, (ServerLevel) player.level);
    }

    private static void storePlayBoard(ServerPlayer player, ServerLevel world) {
        EntityRefBoard board = EntityRefBoard.deployedBoards.get(player.getUUID());

        EntityRefBoard.Data data = new EntityRefBoard.Data(player.getUUID(), board);
        data.setDirty();
        world.getDataStorage().set(data);
    }

    @SubscribeEvent
    public static void PlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer().level instanceof ServerLevel)) {
            return;
        }
        ServerLevel world = (ServerLevel) event.getPlayer().level;

        EntityRefBoard board = new EntityRefBoard(event.getPlayer(), world);
        world.getDataStorage().get(
                (CompoundTag t) -> new EntityRefBoard.Data(event.getPlayer().getUUID(), board),
                EntityRefBoard.Data.ID(event.getPlayer().getUUID())
        );
        if (board.isAlive()) {
            EntityRefBoard.spawn(event.getPlayer(), world, board, StandardRefBoard.ID); // TODO: Store board type on ref board data
        }
    }

}
