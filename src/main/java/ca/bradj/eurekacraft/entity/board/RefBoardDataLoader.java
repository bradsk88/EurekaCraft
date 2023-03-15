package ca.bradj.eurekacraft.entity.board;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
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

import java.util.UUID;

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
        if (!(event.getPlayer().level instanceof ServerLevel)) {
            return;
        }
        ServerLevel world = (ServerLevel) event.getPlayer().level;

        EntityRefBoard board = new EntityRefBoard(event.getPlayer(), world);
        world.getDataStorage().get(
                (CompoundTag t) ->  new EntityRefBoard.Data(event.getPlayer().getUUID(), board, t),
                EntityRefBoard.Data.ID(event.getPlayer().getUUID())
        );
        if (board.isAlive()) {
            ItemStack mainHandItem = event.getPlayer().getMainHandItem();
            if (mainHandItem.isEmpty()) {
                return;
            }
            CompoundTag tag = mainHandItem.getOrCreateTag();
            if (!tag.hasUUID(EntityRefBoard.NBT_KEY_BOARD_UUID)) {
                return;
            }
            UUID handBoardUUID = tag.getUUID(EntityRefBoard.NBT_KEY_BOARD_UUID);
            UUID entityBoardUUID = EntityRefBoard.getEntityBoardUUID(board);
            if (handBoardUUID.equals(entityBoardUUID)) {
                EntityRefBoard.spawnFromInventory(
                        event.getPlayer(),
                        world,
                        mainHandItem,
                        ((RefBoardItem) mainHandItem.getItem()).getBoardType()
                );
            }
        }
    }

}
