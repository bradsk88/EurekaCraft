package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.IBoardStatsFactory;
import ca.bradj.eurekacraft.interfaces.IBoardStatsFactoryProvider;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class BlueprintAdvancedItem extends Item implements IBoardStatsFactoryProvider {

    private static final IBoardStatsFactory FACTORY_INSTANCE = new BoardStatsFactory();

    public static final String ITEM_ID = "blueprint_advanced";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public BlueprintAdvancedItem() {
        super(PROPS);
    }

    @Override
    public IBoardStatsFactory getBoardStatsFactory() {
        return FACTORY_INSTANCE;
    }

    public static class BoardStatsFactory extends BlueprintItem.BoardStatsFactory {
        @Override
        public RefBoardStats getBoardStatsFromNBTOrCreate(ItemStack itemStack, RefBoardStats creationReference, Random rand) {
            RefBoardStats boostedReference = creationReference.WithAllIncreased(0.25);
            return super.getBoardStatsFromNBTOrCreate(itemStack, boostedReference, rand);
        }
    }
}
