package ca.bradj.eurekacraft.vehicles.deployment;

import net.minecraftforge.common.capabilities.Capability;

public class DeploymentCapability {
    // TODO: Reimplement
//
//    @CapabilityInject(IPlayerEntityBoardDeployed.class)
    public static Capability<IPlayerEntityBoardDeployed> PLAYER_BOARD_DEPLOYED_CAPABILITY = null;
//
//    public static void register() {
//        CapabilityManager.INSTANCE.injectCapabilities(IPlayerEntityBoardDeployed.class, new Storage(), DefaultPlayerBoardDeployed::new);
//    }
//
//    public static class Storage implements Capability.IStorage<IPlayerEntityBoardDeployed> {
//
//        @Nullable
//        @Override
//        public Tag writeNBT(Capability<IPlayerEntityBoardDeployed> capability, IPlayerEntityBoardDeployed instance, Direction side) {
//            CompoundTag tag = new CompoundTag();
//            tag.putString("board_type", instance.getBoardType().getPath());
//            return tag;
//        }
//
//        @Override
//        public void readNBT(Capability<IPlayerEntityBoardDeployed> capability, IPlayerEntityBoardDeployed instance, Direction side, INBT nbt) {
//            String boardType = ((CompoundTag) nbt).getString("board_type");
//            instance.setBoardType(BoardType.fromNBT(boardType));
//        }
//    }

}
