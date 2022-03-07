package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.sql.Ref;
import java.util.Random;

public class BlueprintItem extends Item implements INBTSerializable<CompoundNBT> {

    private static final String NBT_KEY_BOARD_STATS = "board_stats";
    private static final String NBT_KEY_BOARD_STATS_WEIGHT = "weight";
    private static final String NBT_KEY_BOARD_STATS_SPEED = "speed";
    private static final String NBT_KEY_BOARD_STATS_AGILITY = "agility";
    private static final String NBT_KEY_BOARD_STATS_LIFT = "lift";
    // TODO: land resistance and surf

    public static final String ITEM_ID = "blueprint";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public BlueprintItem() {
        super(PROPS);
    }

    public static RefBoardStats getBoardStatsFromNBTOrCreate(
            ItemStack itemStack, RefBoardStats creationReference, Random rand
    ) {
        if (itemStack.getTag() == null) {
            itemStack.setTag(new CompoundNBT());
        }
        if (itemStack.getTag().contains(NBT_KEY_BOARD_STATS)) {
            return RefBoardStats.FromNBT(itemStack.getTag().getCompound(NBT_KEY_BOARD_STATS));
        }
        RefBoardStats s = RefBoardStats.FromReferenceWithRandomOffsets(creationReference, rand);
        itemStack.getTag().put(NBT_KEY_BOARD_STATS, s.serializeNBT());
        return s;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
