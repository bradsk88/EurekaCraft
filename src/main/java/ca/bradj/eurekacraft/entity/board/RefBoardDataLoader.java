package ca.bradj.eurekacraft.entity.board;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class RefBoardDataLoader {

    public static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    @SubscribeEvent
    public static void PauseOrCloseServer(LevelEvent.Save event) {
        for (Player player : event.getLevel().players()) {
            storePlayBoard((ServerPlayer) player, (ServerLevel) event.getLevel());
        }
    }

    @SubscribeEvent
    public static void PlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
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
        Entity player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerLevel world = (ServerLevel) player.level;

        EntityRefBoard board = new EntityRefBoard(player, world);
        world.getDataStorage().get(
                (CompoundTag t) ->  new EntityRefBoard.Data(player.getUUID(), board, t),
                EntityRefBoard.Data.ID(player.getUUID())
        );
        if (board.isAlive()) {
            ItemStack mainHandItem = ((ServerPlayer) player).getMainHandItem();
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
                        player,
                        world,
                        mainHandItem,
                        ((RefBoardItem) mainHandItem.getItem()).getBoardType()
                );
            }
        }
    }

}
