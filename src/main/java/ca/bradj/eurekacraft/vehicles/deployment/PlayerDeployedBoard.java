package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.EurekaCraftNetwork;
import ca.bradj.eurekacraft.core.network.msg.DeployedBoardMessage;
import ca.bradj.eurekacraft.vehicles.BoardType;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class PlayerDeployedBoard implements ICapabilitySerializable<CompoundTag> {

    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    private final DefaultPlayerBoardDeployed board = new DefaultPlayerBoardDeployed();
    private final LazyOptional<IPlayerEntityBoardDeployed> boardOptional = LazyOptional.of(() -> board);

    public static Optional<BoardType> get(Entity p) {
        LazyOptional<IPlayerEntityBoardDeployed> c = p.getCapability(DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY);
        if (c.isPresent() && c.resolve().isPresent()) {
            return c.resolve().map(IPlayerEntityBoardDeployed::getBoardType);
        }
        return Optional.empty();
    }

    public static void set(Entity p, BoardType t) {
        LazyOptional<IPlayerEntityBoardDeployed> c = p.getCapability(DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY);
        c.ifPresent(v -> {
            if (t.equals(v.getBoardType())) {
                return;
            }
            if (BoardType.NONE.equals(t)) {
                remove(p);
            }
            v.setBoardType(t);
            EurekaCraftNetwork.CHANNEL.send(
                    PacketDistributor.ALL.noArg(), // TODO: Consider limiting reach
                    new DeployedBoardMessage(p.getId(), t)
            );
        });
    }

    public static void remove(Entity p) {
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
        if (cap == null) {
            return LazyOptional.empty();
        }
        String t = cap.getName();
        if ("ca.bradj.eurekacraft.vehicles.deployment.IPlayerEntityBoardDeployed".equals(t)) {
            return boardOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        if (DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY == null) {
            return new CompoundTag();
        }
        // TODO: Reimplement
//        return (CompoundTag) DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY.writeNBT(board, null);
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY != null) {
            // TODO: Reimplement
//            DeploymentCapability.PLAYER_BOARD_DEPLOYED_CAPABILITY.readNBT(board, null, nbt);
        }
    }
}
