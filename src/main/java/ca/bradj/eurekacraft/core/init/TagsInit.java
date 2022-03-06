package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class TagsInit {

    public static class Items {

        public static final Tags.IOptionalNamedTag<Item> SANDING_DISCS = createTag("sanding_disc");


        private static Tags.IOptionalNamedTag<Item> createTag(String name) {
            return ItemTags.createOptional(new ResourceLocation(EurekaCraft.MODID, name));
        }
    }
}
