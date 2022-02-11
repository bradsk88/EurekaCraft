package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.EurekaCraftNetwork;
import ca.bradj.eurekacraft.core.network.msg.DeployedBoardMessage;
import ca.bradj.eurekacraft.vehicles.BoardType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class PlayerDeployedBoard implements ICapabilitySerializable<CompoundNBT> {

    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    private final DefaultPlayerBoardDeployed board = new DefaultPlayerBoardDeployed();
    private final LazyOptional<IPlayerEntityBoardDeployed> boardOptional = LazyOptional.of(() -> board);

    public static Optional<BoardType> get(PlayerEntity p) {
        LazyOptional<IPlayerEntityBoardDeployed> c = p.getCapability(DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY);
        if (c.isPresent() && c.resolve().isPresent()) {
            return c.resolve().map(IPlayerEntityBoardDeployed::getBoardType);
        }
        return Optional.empty();
    }

    public static void set(PlayerEntity p, BoardType t) {
        if (BoardType.NONE.equals(t)) {
            remove(p);
        }
        LazyOptional<IPlayerEntityBoardDeployed> c = p.getCapability(DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY);
        c.ifPresent(v -> {
            if (t.equals(v.getBoardType())) {
                return;
            }
            v.setBoardType(t);
            EurekaCraftNetwork.CHANNEL.send(
                    PacketDistributor.ALL.noArg(), // TODO: Consider limiting reach
                    new DeployedBoardMessage(p.getId(), t)
            );
        });
    }

    public static void remove(PlayerEntity p) {
        LazyOptional<IPlayerEntityBoardDeployed> c = p.getCapability(DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY);
        c.ifPresent(v -> {
            if (BoardType.NONE.equals(v.getBoardType())) {
                return;
            }
            v.setBoardType(BoardType.NONE);
            EurekaCraftNetwork.CHANNEL.send(
                    PacketDistributor.ALL.noArg(), // TODO: Consider limiting reach
                    new DeployedBoardMessage(p.getId(), BoardType.NONE)
            );
        });
    }

    public void invalidate() {
        boardOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return boardOptional.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY == null) {
            return new CompoundNBT();
        }
        return (CompoundNBT) DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY.writeNBT(board, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY != null) {
            DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY.readNBT(board, null, nbt);
        }
    }
}
