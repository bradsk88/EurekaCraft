package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagsInit {

    public static class Items {

        public static final TagKey<Item> SANDING_DISCS = createTag("sanding_disc");
        public static final TagKey<Item> ITEMS_THAT_BURN = createTag("items_that_burn");


        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(new ResourceLocation(EurekaCraft.MODID, name));
        }
    }
}
