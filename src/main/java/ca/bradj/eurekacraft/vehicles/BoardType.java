package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.util.ResourceLocation;

public class BoardType extends ResourceLocation {
    public static final BoardType NONE = new BoardType("--none--");

    public BoardType(String boardID) {
        super(EurekaCraft.MODID, boardID);
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof BoardType)) {
            return false;
        }
        BoardType bt = (BoardType) p_equals_1_;

        return this.namespace.equals(bt.namespace) && this.path.equals(bt.path);
    }
}
