package ca.bradj.eurekacraft.villager;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.core.init.items.WheelItemsInit;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static ca.bradj.eurekacraft.materials.BlueprintItem.NBT_KEY_BOARD_STATS;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class VillagerEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {

        // Level 1: Built board construction tools
        final List<MerchantOffer> refDealerTrades1 = ImmutableList.of(
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.GLIDE_BOARD.get(), 1),
                        1, 8, 0.8F
                ),
                new MerchantOffer(
                        new ItemStack(ItemsInit.BROKEN_BOARD.get(), 1),
                        new ItemStack(Items.EMERALD, 5),
                        new ItemStack(ItemsInit.REF_BOARD_CORE.get(), 1),
                        1, 8, 0.8F
                ),
                new MerchantOffer(
                        new ItemStack(ItemsInit.BROKEN_BOARD.get(), 1),
                        new ItemStack(Items.EMERALD, 1),
                        4, 8, 1.0F
                ),
                // TODO: Randomize blueprint stats
                new MerchantOffer(
                        new ItemStack(Items.PAPER, 32),
                        new ItemStack(ItemsInit.BLUEPRINT.get(), 1),
                        1, 8, 0.75F
                ),
                new MerchantOffer(
                        new ItemStack(Items.PAPER, 16),
                        new ItemStack(Items.EMERALD, 1),
                        4, 8, 0.5F
                ),
                new MerchantOffer(
                        new ItemStack(Items.CLAY_BALL, 16),
                        new ItemStack(Items.EMERALD, 1),
                        4, 8, 0.5F
                ),
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(ItemsInit.FLINT_SANDING_DISC.get(), 2),
                        4, 8, 1.0F
                ),
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(ItemsInit.FLINT_STICKY_DISC.get(), 4),
                        4, 8, 1.0F
                ),
                // TODO: Randomize blueprint stats
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.BLUEPRINT.get(), 1),
                        4, 8, 0.75F
                ),
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(Items.CLAY_BALL, 16),
                        4, 8, 0.75F
                ),
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(Items.PAPER, 16),
                        4, 8, 0.75F
                ),
                new MerchantOffer(
                        new ItemStack(Items.CLAY_BALL, 10),
                        new ItemStack(Items.PAPER, 10),
                        new ItemStack(ItemsInit.CLAY_STICKY_DISC.get(), 5),
                        4, 8, 0.75F
                ),
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(Items.IRON_BLOCK, 1),
                        4, 8, 0.75F
                ),
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(Items.OAK_SLAB, 16),
                        4, 8, 0.75F
                )
        );

        // Level 2: Build a board
        final List<MerchantOffer> refDealerTrades2 = ImmutableList.of(
                new MerchantOffer(
                        new ItemStack(ItemsInit.REF_BOARD_CORE.get(), 1),
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.STANDARD_REF_BOARD.get(), 1),
                        1, 8, 0.75F
                ),
                new MerchantOffer(
                        new ItemStack(Items.IRON_INGOT, 8),
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.REF_TABLE_BLOCK.get(), 1),
                        1, 8, 0.75F
                ),
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.FLINT_SANDING_DISC_STACK.get(), 1),
                        1, 8, 0.5F
                ),
                new MerchantOffer(
                        new ItemStack(Items.WHEAT_SEEDS, 1),
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.FRESH_SEEDS_ITEM.get(), 1),
                        1, 8, 0.5F
                ),
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.REFLECTION_FILM.get(), 5),
                        4, 8, 0.5F
                ),
                new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.REFLECTION_FILM_DUST.get(), 6),
                        4, 8, 0.5F
                ),
                new MerchantOffer(
                        new ItemStack(ItemsInit.PRECISION_WOOD.get(), 4),
                        new ItemStack(Items.EMERALD, 2),
                        4, 8, 0.5F
                ),
                new MerchantOffer(
                        new ItemStack(ItemsInit.POLISHED_OAK_SLAB.get(), 8),
                        new ItemStack(Items.EMERALD, 1),
                        4, 8, 0.5F
                ),
                new MerchantOffer(
                        new ItemStack(Items.OAK_WOOD, 8),
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.RESIN.get(), 16),
                        4, 8, 0.5F
                )
        );

        // Level 3: Improve a standard ref board
        final List<VillagerTrades.ItemListing> refDealerTrades3 = ImmutableList.of(
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_BLACK.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_BLUE.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_BROWN.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_CYAN.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_GRAY.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_GREEN.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_LIGHT_BLUE.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_LIGHT_GRAY.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_LIME.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_MAGENTA.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_ORANGE.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_PINK.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_PURPLE.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_RED.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_WHITE.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.PAINT_BUCKET_YELLOW.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.SOFT_CHISEL.get(), 1),
                        4, 8, 0.5F
                ),
                // TODO: Randomize blueprint stats
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 8),
                        new ItemStack(ItemsInit.BLUEPRINT.get(), 1),
                        new ItemStack(ItemsInit.BLUEPRINT_ADVANCED.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.SCUB_GLASS_LENS.get(), 1),
                        4, 8, 0.5F
                ),
                (trader, random) -> {
                    ItemStack boardStack = new ItemStack(ItemsInit.STANDARD_REF_BOARD.get(), 1);
                    RefBoardStats s = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.BadBoard, random);
                    boardStack.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(s));
                    return new MerchantOffer(
                            new ItemStack(Items.EMERALD, 6),
                            boardStack,
                            1, 8, 0.5F
                    );
                },
                (trader, random) -> {
                    ItemStack boardStack = new ItemStack(ItemsInit.STANDARD_REF_BOARD.get(), 1);
                    RefBoardStats s = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.BadBoard, random);
                    boardStack.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(s));
                    return new MerchantOffer(
                            new ItemStack(Items.EMERALD, 6),
                            boardStack,
                            1, 8, 0.5F
                    );
                }
        );

        // Level 4: Goggles, Mid-Tier Wheels, Boards with Stats
        final List<VillagerTrades.ItemListing> refDealerTrades4 = ImmutableList.of(
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 6),
                            new ItemStack(ItemsInit.REFLECTION_FILM.get(), 8),
                            new ItemStack(ItemsInit.SCUB_GOGGLES.get(), 8),
                            1, 8, 0.5F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(WheelItemsInit.SOCKET_WRENCH.get(), 1),
                            1, 8, 1.0F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(WheelItemsInit.WHEEL_BEARING_ITEM.get(), 1),
                            1, 8, 0.5F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(WheelItemsInit.WHEEL_BEARING_MOLD_ITEM.get(), 1),
                            1, 8, 1.0F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(WheelItemsInit.OAK_WOOD_WHEEL_ITEM.get(), 1),
                            1, 8, 1.0F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(WheelItemsInit.STONE_WHEEL_ITEM.get(), 1),
                            1, 8, 1.0F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(WheelItemsInit.IRON_WHEEL_ITEM.get(), 1),
                            1, 8, 1.0F),
                // Random stats board 1
                (trader, random) -> {
                    ItemStack boardStack = new ItemStack(ItemsInit.STANDARD_REF_BOARD.get(), 1);
                    RefBoardStats s = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.StandardBoard, random);
                    boardStack.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(s));
                    return new MerchantOffer(
                            new ItemStack(Items.EMERALD, 16),
                            boardStack,
                            1, 8, 0.5F
                    );
                },
                // Random stats board 2
                (trader, random) -> {
                    ItemStack boardStack = new ItemStack(ItemsInit.STANDARD_REF_BOARD.get(), 1);
                    RefBoardStats s = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.StandardBoard, random);
                    boardStack.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(s));
                    return new MerchantOffer(
                            new ItemStack(Items.EMERALD, 16),
                            boardStack,
                            1, 8, 0.5F
                    );
                }
        );

        // Level 5: Elite board, High-Tier Wheels
        final List<VillagerTrades.ItemListing> refDealerTrades5 = ImmutableList.of(
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD_BLOCK, 4),
                        new ItemStack(ItemsInit.ELITE_BOARD.get(), 1),
                        1, 8, 0.75F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 16),
                        new ItemStack(WheelItemsInit.GOLD_WHEEL_ITEM.get(), 1),
                        1, 8, 0.75F),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 32),
                        new ItemStack(WheelItemsInit.DIAMOND_WHEEL_ITEM.get(), 1),
                        1, 8, 0.9F)
        );

        if (event.getType() == VillagersInit.REF_DEALER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            refDealerTrades1.forEach(o -> trades.get(1).add((trader, rand) -> o));

            refDealerTrades2.forEach(o -> trades.get(2).add((trader, rand) -> o));

            refDealerTrades3.forEach(o -> trades.get(3).add(o));

            refDealerTrades4.forEach(o -> trades.get(4).add(o));

            refDealerTrades5.forEach(o -> trades.get(5).add(o));
        }
    }
}
