package ca.bradj.eurekacraft.core.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroup {

    public static final CreativeModeTab EUREKACRAFT_GROUP = new CreativeModeTab("eurekaCraftTab") {
        @Override
        public ItemStack makeIcon() {
            return ItemsInit.STANDARD_REF_BOARD.get().getDefaultInstance();
        }
    };
}
