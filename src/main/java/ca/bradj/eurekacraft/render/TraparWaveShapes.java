package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TraparWaveShapes {

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private static int[][][] SHAPE_1_ARRAY = {
                    {
                            {1, 1, 1},
                            {1, 1, 1},
                            {1, 1, 1},
                            {1, 1, 0},
                            {1, 1, 0},
                    },{
                    {0, 1, 1},
                    {0, 1, 1},
                    {0, 1, 1},
                    {0, 0, 0},
                    {0, 0, 0},
            }
        };

    public static TraparWaveShapes SHAPE_1 = new TraparWaveShapes(SHAPE_1_ARRAY, new RangeFunction() {
        @Override
        public boolean isInRange(BlockPos center, int[][][] shape, BlockPos other) {
            int zRange = shape[0].length / 2;
            int xRange = shape[0][0].length / 2; // TODO: Swap if "facing" deems it so
            int yRange = shape.length / 2;
            int xDist = Math.abs(center.getX() - other.getX());
            int yDist = Math.abs(center.getY() - other.getY());
            int zDist = Math.abs(center.getZ() - other.getZ());
            return xDist <= xRange && yDist <= yRange && zDist <= zRange;
        }
    });

    protected final int[][][] shape;
    protected Direction facing = Direction.NORTH;
    protected BlockPos center = BlockPos.ZERO;
    private List<BlockPos> asRelativeBlockPositions = new ArrayList<>();
    private final RangeFunction rangeFunction;

    private TraparWaveShapes(
            int[][][] shape, RangeFunction rangeFn
    ) {
        this.shape = shape;
        this.rangeFunction = rangeFn;
    }

    public TraparWaveShapes WithCenterAndDirection(BlockPos center, Direction facing) {
        TraparWaveShapes traparWaveShapes = new TraparWaveShapes(this.shape, this.rangeFunction);
        traparWaveShapes.center = center;
        traparWaveShapes.facing = facing;
        return traparWaveShapes;
    }

    public boolean isInAffectedRange(BlockPos other) {
        return this.rangeFunction.isInRange(this.center, this.shape, other);
    }

    public Iterable<? extends BlockPos> getAsRelativeBlockPositions() {
        if (this.asRelativeBlockPositions.isEmpty()) {
            this.computeRelativeBlockPositions();
        }
        return this.asRelativeBlockPositions;
    }

    private void computeRelativeBlockPositions() {
        int halfY = shape.length / 2;
        int halfZ = shape[0].length / 2;
        int halfX = shape[0][0].length / 2;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                for (int k = 0; k < shape[i][j].length; k++) {
                    if (shape[i][j][k] == 1) {
                        this.asRelativeBlockPositions.add(new BlockPos(k - halfX, i - halfY, j - halfZ));
                    }
                }
            }
        }
    }

    public static class RangeFunction {

        public boolean isInRange(BlockPos center, int[][][] shape, BlockPos queryPos) {
            return false;
        }
    }
}
