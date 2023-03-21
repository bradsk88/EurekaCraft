package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.blocks.machines.RefTableConsts;
import ca.bradj.eurekacraft.core.init.TagsInit;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collection;
import java.util.Optional;

public class RefTableSlotPreferences {
    public static Optional<Integer> getIdealSlot(
            Item item,
            Collection<Item> inputItems,
            Item fuelItem,
            Item techItem
    ) {
        if (inputItems.stream().anyMatch(i -> i.equals(item))) {
            return Optional.of(RefTableConsts.inputSlotIndex);
        }

        if (Ingredient.of(ItemTags.COALS)
                .test(new ItemStack(item))) {
            boolean emptyOrSame = fuelItem.equals(Items.AIR) || fuelItem.equals(item);
            if (emptyOrSame) {
                return Optional.of(RefTableConsts.fuelSlot);
            }
            return Optional.of(RefTableConsts.inputSlotIndex);
        }

        if (inputItems.size() == 0 || inputItems.stream().allMatch(Items.AIR::equals)) {
            return Optional.of(RefTableConsts.inputSlotIndex);
        }

        if (Ingredient.of(TagsInit.Items.TECH_ITEMS)
                .test(new ItemStack(item))) {
            return Optional.of(RefTableConsts.techSlot);
        }

        return Optional.empty();
    }
}
