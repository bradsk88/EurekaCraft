package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.vehicles.BoardType;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class PlayerDeployedBoard {

    private BoardType boardType = BoardType.NONE;

    public @NotNull BoardType getBoardType() {
        return this.boardType;
    }

    public boolean setBoardType(BoardType t) {
        if (this.boardType == t) {
            return false;
        }
        this.boardType = t;
        return true;
    }

    public void saveNBT(CompoundTag tag) {
        tag.putString("board", this.boardType.toNBT());
    }

    public void loadNBT(CompoundTag tag) {
        String boardString = tag.getString("board");
        this.boardType = BoardType.fromNBT(boardString);
    }

}