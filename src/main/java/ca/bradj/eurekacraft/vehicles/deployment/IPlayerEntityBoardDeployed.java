package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.vehicles.BoardType;

public interface IPlayerEntityBoardDeployed {
    BoardType getBoardType();

    void setBoardType(BoardType boardType);
}
