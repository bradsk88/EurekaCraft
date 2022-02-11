package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.vehicles.BoardType;

public class DefaultPlayerBoardDeployed implements IPlayerEntityBoardDeployed {
    private BoardType boardType = BoardType.NONE;

    @Override
    public BoardType getBoardType() {
        return this.boardType;
    }

    @Override
    public void setBoardType(BoardType boardType) {
        this.boardType = boardType;
    }
}
