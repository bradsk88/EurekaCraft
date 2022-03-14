package ca.bradj.eurekacraft.entity.board;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.vehicles.StandardRefBoard;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
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
        for (PlayerEntity player : event.getWorld().players()) {
            storePlayBoard((ServerPlayerEntity) player, (ServerWorld) event.getWorld());
        }
    }

    @SubscribeEvent
    public static void PlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEntity player = event.getPlayer();
        if (!(player.level instanceof ServerWorld)) {
            return;
        }
        storePlayBoard((ServerPlayerEntity) player, (ServerWorld) player.level);
    }

    private static void storePlayBoard(ServerPlayerEntity player, ServerWorld world) {
        EntityRefBoard board = EntityRefBoard.deployedBoards.get(player.getUUID());

        EntityRefBoard.Data data = new EntityRefBoard.Data(player.getUUID(), board);
        data.setDirty();
        world.getDataStorage().set(data);
    }

    @SubscribeEvent
    public static void PlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer().level instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = (ServerWorld) event.getPlayer().level;

        EntityRefBoard board = new EntityRefBoard(event.getPlayer(), world);
        world.getDataStorage().get(
                () -> new EntityRefBoard.Data(event.getPlayer().getUUID(), board),
                EntityRefBoard.Data.ID(event.getPlayer().getUUID())
        );
        if (board.isAlive()) {
            EntityRefBoard.spawn(event.getPlayer(), world, board, StandardRefBoard.ID); // TODO: Store board type on ref board data
        }
    }

}
