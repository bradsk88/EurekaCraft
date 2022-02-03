package ca.bradj.eurekacraft.core.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup EUREKACRAFT_GROUP = new ItemGroup("eurekaCraftTab") {
        @Override
        public ItemStack makeIcon() {
            return ItemsInit.STANDARD_BOARD.get().getDefaultInstance();
        }
    };
}
