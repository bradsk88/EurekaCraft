package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.vehicles.BoardType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class DeploymentCapability {

    @CapabilityInject(IPlayerEntityBoardDeployed.class)
    public static Capability<IPlayerEntityBoardDeployed> PLAYER_BOARD_DEPLOYED_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.injectCapabilities(IPlayerEntityBoardDeployed.class, new Storage(), DefaultPlayerBoardDeployed::new);
    }

    public static class Storage implements Capability.IStorage<IPlayerEntityBoardDeployed> {

        @Nullable
        @Override
        public Tag writeNBT(Capability<IPlayerEntityBoardDeployed> capability, IPlayerEntityBoardDeployed instance, Direction side) {
            CompoundTag tag = new CompoundTag();
            tag.putString("board_type", instance.getBoardType().getPath());
            return tag;
        }

        @Override
        public void readNBT(Capability<IPlayerEntityBoardDeployed> capability, IPlayerEntityBoardDeployed instance, Direction side, INBT nbt) {
            String boardType = ((CompoundTag) nbt).getString("board_type");
            instance.setBoardType(BoardType.fromNBT(boardType));
        }
    }

}
