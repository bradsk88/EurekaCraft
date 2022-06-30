package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.interfaces.IIDHaver;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.wheels.BoardWheels;
import ca.bradj.eurekacraft.vehicles.wheels.IWheel;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Optional;

public class PlayerDeployedBoard {

    private DeployedBoard board = DeployedBoard.plain(BoardType.NONE);

    public @NotNull PlayerDeployedBoard.DeployedBoard getBoardType() {
        return this.board;
    }

    public boolean setBoardType(DeployedBoard board) {
        if (this.board.boardType == board.boardType) {
            return false;
        }
        this.board = board;
        return true;
    }

    public void saveNBT(CompoundTag tag) {
        tag.putString("board", this.board.boardType.toNBT());
        CompoundTag color = new CompoundTag();
        float[] colors = this.board.getColor().getRGBColorComponents(null);
        color.putFloat("r", colors[0]);
        color.putFloat("g", colors[1]);
        color.putFloat("b", colors[2]);
        tag.put("color", color);
        this.board.wheel.ifPresent(eurekaCraftItem -> tag.putString(
                "wheel", eurekaCraftItem.getItemId())
        );
    }

    public void loadNBT(CompoundTag tag) {
        String boardString = tag.getString("board");
        CompoundTag color = tag.getCompound("color");
        float r = color.getFloat("r");
        float g = color.getFloat("g");
        float b = color.getFloat("b");
        String wheel = tag.getString("wheel");
        this.board = new DeployedBoard(
                BoardType.fromNBT(boardString),
                new Color(r, g, b),
                BoardWheels.getItem(wheel).map(v -> v)
        );
    }

    public static class DeployedBoard {
        public final BoardType boardType;
        public final Color color;
        public final Optional<? extends IWheel> wheel;

        public DeployedBoard(
                BoardType boardType,
                Color color,
                Optional<? extends IWheel> wheel
        ) {
            this.boardType = boardType;
            this.color = color;
            this.wheel = wheel;
        }

        public static DeployedBoard plain(BoardType bt) {
            return new DeployedBoard(bt, Color.WHITE, Optional.empty());
        }

        public static DeployedBoard NONE = DeployedBoard.plain(BoardType.NONE);

        public Color getColor() {
            return this.color;
        }

        @Override
        public String toString() {
            return "DeployedBoard{" +
                    "boardType=" + boardType +
                    ", color=" + color +
                    ", wheel=" + wheel.map(IIDHaver::getItemId).orElse("None") +
                    '}';
        }
    }
}