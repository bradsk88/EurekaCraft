package ca.bradj.eurekacraft.entity.board;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

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
        world.getDataStorage().set(EntityRefBoard.Data.ID(player.getUUID()), data);
    }

    @SubscribeEvent
    public static void PlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer().level instanceof ServerLevel world)) {
            return;
        }

        EntityRefBoard board = new EntityRefBoard(event.getPlayer(), world);
        AtomicBoolean loaded = new AtomicBoolean(false);
        world.getDataStorage().get(
                (CompoundTag t) ->  {
                    loaded.set(true);
                    return new EntityRefBoard.Data(event.getPlayer().getUUID(), board, t);
                },
                EntityRefBoard.Data.ID(event.getPlayer().getUUID())
        );
        if (!loaded.get()) {
            return;
        }
        if (!board.isAlive()) {
            return;
        }
        ItemStack mainHandItem = event.getPlayer().getMainHandItem();
        if (!(mainHandItem.getItem() instanceof RefBoardItem mainHandBoardItem)) {
            EurekaCraft.LOGGER.error("Found deployed board, but hand does not contain board.");
            EurekaCraft.LOGGER.info("Resolving discrepancy by killing board");
            board.kill();
            return;
        }

        if (event.getPlayer().isOnGround()) {
            PlayerDeployedBoard.DeployedBoard.RemoveFromStack(mainHandItem);
            PlayerDeployedBoardProvider.removeBoardFor(event.getPlayer());
        }

        Optional<UUID> handBoardUUID = EntityRefBoard.getItemStackBoardUUID(mainHandItem);
        if (handBoardUUID.isEmpty()) {
            return;
        }
        Optional<UUID> entityBoardUUID = EntityRefBoard.getEntityBoardUUID(board);
        if (entityBoardUUID.isEmpty()) {
            return;
        }
        if (handBoardUUID.get().equals(entityBoardUUID.get())) {
            BoardType boardType = mainHandBoardItem.getBoardType();
            EntityRefBoard.toggleFromInventory(
                    event.getPlayer(), world, mainHandItem, boardType
            );
        }
    }

}
