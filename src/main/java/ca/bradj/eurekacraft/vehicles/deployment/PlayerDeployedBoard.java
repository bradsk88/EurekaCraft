package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.vehicles.BoardType;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PlayerDeployedBoard {

    private ColoredBoard board = new ColoredBoard(BoardType.NONE, 1, 1, 1);

    public @NotNull ColoredBoard getBoardType() {
        return this.board;
    }

    public boolean setBoardType(ColoredBoard board) {
        if (this.board.boardType == board.boardType) {
            return false;
        }
        this.board = board;
        return true;
    }

    // TODO: Serialize color
    public void saveNBT(CompoundTag tag) {
        tag.putString("board", this.board.boardType.toNBT());
    }

    public void loadNBT(CompoundTag tag) {
        String boardString = tag.getString("board");
        this.board = new ColoredBoard(
                BoardType.fromNBT(boardString), 1, 1, 1 // TODO: load color
        );
    }

    public static class ColoredBoard {
        public final BoardType boardType;
        public final float r;
        public final float g;
        public final float b;

        public ColoredBoard(BoardType boardType, float r, float g, float b) {
            this.boardType = boardType;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public static ColoredBoard plain(BoardType bt) {
            return new ColoredBoard(bt, 1, 1, 1);
        }

        public static ColoredBoard NONE = new ColoredBoard(BoardType.NONE, 1, 1,1);

        public ColoredBoard withColor(Color color) {
            return new ColoredBoard(
                    this.boardType, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f
            );
        }

        @Override
        public String toString() {
            return "ColoredBoard{" +
                    "boardType=" + boardType +
                    ", r=" + r +
                    ", g=" + g +
                    ", b=" + b +
                    '}';
        }
    }
}