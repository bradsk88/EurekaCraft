package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.EurekaCraftNetwork;
import ca.bradj.eurekacraft.core.network.msg.DeployedBoardMessage;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.wheels.IWheel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Optional;

public class PlayerDeployedBoardProvider implements ICapabilitySerializable<CompoundTag> {

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static Capability<PlayerDeployedBoard> PLAYER_BOARD = CapabilityManager.get(new CapabilityToken<>() {
    });

    private PlayerDeployedBoard playerBoard = null;
    private final LazyOptional<PlayerDeployedBoard> opt = LazyOptional.of(this::getOrInitializeBoard);

    public static Optional<PlayerDeployedBoard.DeployedBoard> getBoardTypeFor(@Nonnull ICapabilityProvider player) {
        return player.getCapability(PLAYER_BOARD).map((PlayerDeployedBoard::getBoardType));
    }

    public static void setBoardTypeFor(
            @Nonnull Entity p, BoardType bt, Color c,
            Optional<? extends IWheel> wheelItem, boolean publishChanges
    ) {
        p.getCapability(PLAYER_BOARD).ifPresent(b -> {
            PlayerDeployedBoard.DeployedBoard board = new PlayerDeployedBoard.DeployedBoard(bt, c, wheelItem);
            boolean wasChanged = b.setBoardType(board);
            if (wasChanged && publishChanges) {
                logger.debug("Sending board type change packet: " + board);
                EurekaCraftNetwork.CHANNEL.send(
                        PacketDistributor.ALL.noArg(), // TODO: Consider limiting reach
                        new DeployedBoardMessage(p.getId(), board)
                );
            }
        });
    }

    public static void removeBoardFor(@Nonnull Entity player) {
        setBoardTypeFor(player, BoardType.NONE, Color.WHITE, Optional.empty(), true);
    }

    public void invalidate() {
        opt.invalidate();
    }

    public @NotNull PlayerDeployedBoard getOrInitializeBoard() {
        if (playerBoard == null) {
            playerBoard = new PlayerDeployedBoard();
        }
        return playerBoard;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == PLAYER_BOARD) {
            return opt.cast();
        }
        return LazyOptional.empty();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        getOrInitializeBoard().saveNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrInitializeBoard().loadNBT(nbt);
    }
}
