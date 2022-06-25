package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.vehicles.BoardType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PlayerDeployedBoard {

    private ColoredBoard board = ColoredBoard.plain(BoardType.NONE);

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
        CompoundTag color = tag.getCompound("color");
        float r = color.getFloat("r");
        float g = color.getFloat("g");
        float b = color.getFloat("b");
        this.board = new ColoredBoard(
                BoardType.fromNBT(boardString), new Color(r, g, b)
        );
    }

    public static class ColoredBoard {
        public final BoardType boardType;
        public final Color color;

        public ColoredBoard(BoardType boardType, Color color) {
            this.boardType = boardType;
            this.color = color;
        }

        public static ColoredBoard plain(BoardType bt) {
            return new ColoredBoard(bt, Color.WHITE);
        }

        public static ColoredBoard NONE = ColoredBoard.plain(BoardType.NONE);


        public Color getColor() {
            return this.color;
        }

        @Override
        public String toString() {
            return "ColoredBoard{" +
                    "boardType=" + boardType +
                    ", color=" + color +
                    '}';
        }
    }
}