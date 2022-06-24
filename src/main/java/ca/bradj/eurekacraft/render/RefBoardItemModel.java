package ca.bradj.eurekacraft.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import java.util.ArrayList;

public class RefBoardItemModel extends BakedItemModel {
    public RefBoardItemModel() {
        super(RefBoardItemModel.buildQuads(), null, ImmutableMap.of(), ItemOverrides.EMPTY, true, true);
    }

    private static ImmutableList<BakedQuad> buildQuads() {
        ArrayList<BakedQuad> quads = new ArrayList<BakedQuad>();
        BakedQuadBuilder b = new BakedQuadBuilder();
        b.put(0, 2, 2, 2, 2);
        b.put(1, 2, 2, 2, 2);
        b.put(2, 2, 2, 2, 2);
        b.put(3, 2, 2, 2, 2);
        quads.add(b.build());
        return ImmutableList.copyOf(quads);
    }
}
