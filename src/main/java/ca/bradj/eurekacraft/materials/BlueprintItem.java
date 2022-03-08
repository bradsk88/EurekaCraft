package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.IBoardStatsFactory;
import ca.bradj.eurekacraft.interfaces.IBoardStatsFactoryProvider;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.Random;

public class BlueprintItem extends Item implements IBoardStatsFactoryProvider {

    private static final String NBT_KEY_BOARD_STATS = "board_stats";
    private static final IBoardStatsFactory FACTORY_INSTANCE = new BoardStatsFactory();

    public static final String ITEM_ID = "blueprint";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public BlueprintItem() {
        super(PROPS);
    }

    @Override
    public IBoardStatsFactory getBoardStatsFactory() {
        return FACTORY_INSTANCE;
    }

    public static class BoardStatsFactory implements IBoardStatsFactory {
        @Override
        public RefBoardStats getBoardStatsFromNBTOrCreate(
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
    }
}
