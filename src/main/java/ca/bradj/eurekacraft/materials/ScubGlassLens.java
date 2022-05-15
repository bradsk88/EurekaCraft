package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class ScubGlassLens extends Item {

    public static final String ITEM_ID = "scub_glass_lens";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public ScubGlassLens() {
        super(PROPS);
    }
}
