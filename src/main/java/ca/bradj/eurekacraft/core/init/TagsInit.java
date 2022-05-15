package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagsInit {

    public static class Items {

        public static final TagKey<Item> SANDING_DISCS = createTag("sanding_disc");


        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(new ResourceLocation(EurekaCraft.MODID, name));
        }
    }
}
