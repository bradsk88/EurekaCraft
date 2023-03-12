package ca.bradj.eurekacraft.villager;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.core.init.items.WheelItemsInit;
import ca.bradj.eurekacraft.materials.BlueprintItem;
import ca.bradj.eurekacraft.vehicles.EliteRefBoard;
import ca.bradj.eurekacraft.vehicles.StandardRefBoard;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class VillagerEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {

        // Level 1: Built board construction tools
        final List<VillagerTrades.ItemListing> refDealerTrades1 = ImmutableList.of(
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.GLIDE_BOARD.get(), 1),
                        1, 4, 0.25F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(ItemsInit.BROKEN_BOARD.get(), 1),
                        new ItemStack(Items.EMERALD, 5),
                        new ItemStack(ItemsInit.REF_BOARD_CORE.get(), 1),
                        1, 4, 0.2F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(ItemsInit.BROKEN_BOARD.get(), 1),
                        new ItemStack(Items.EMERALD, 1),
                        4, 4, 0.0F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.PAPER, 24),
                        new ItemStack(Items.EMERALD, 2),
                        BlueprintItem.getRandom(rand),
                        1, 4, 0.125F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.PAPER, 24),
                        new ItemStack(Items.EMERALD, 1),
                        4, 4, 0.25F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.CLAY_BALL, 24),
                        new ItemStack(Items.EMERALD, 1),
                        4, 4, 0.25F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(ItemsInit.FLINT_SANDING_DISC.get(), 1),
                        4, 4, 0.0F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(ItemsInit.FLINT_STICKY_DISC.get(), 2),
                        4, 4, 0.0F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        BlueprintItem.getRandom(rand),
                        4, 4, 0.25F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(Items.CLAY_BALL, 8),
                        4, 4, 0.0F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(Items.PAPER, 8),
                        4, 4, 0.0F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.CLAY_BALL, 10),
                        new ItemStack(Items.PAPER, 10),
                        new ItemStack(ItemsInit.CLAY_STICKY_DISC.get(), 5),
                        4, 4, 0.1F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(Items.IRON_INGOT, 1),
                        4, 4, 0.25F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(Items.OAK_SLAB, 8),
                        4, 4, 0.5F
                )
        );

        // Level 2: Build a board
        final List<VillagerTrades.ItemListing> refDealerTrades2 = ImmutableList.of(
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(ItemsInit.REF_BOARD_CORE.get(), 1),
                        new ItemStack(Items.EMERALD, 4),
                        StandardRefBoard.getWithRandomBadStats(rand),
                        1, 8, 0.0F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.IRON_INGOT, 8),
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.REF_TABLE_BLOCK.get(), 1),
                        1, 8, 0.125F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.FLINT_SANDING_DISC_STACK.get(), 1),
                        1, 8, 0.25F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.WHEAT_SEEDS, 1),
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.FRESH_SEEDS_ITEM.get(), 1),
                        1, 8, 0.0F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.REFLECTION_FILM.get(), 5),
                        4, 8, 0.25F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        new ItemStack(ItemsInit.REFLECTION_FILM_DUST.get(), 6),
                        4, 8, 0.0F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(ItemsInit.PRECISION_WOOD.get(), 8),
                        new ItemStack(Items.EMERALD, 2),
                        4, 8, 0.25F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(ItemsInit.POLISHED_OAK_SLAB.get(), 16),
                        new ItemStack(Items.EMERALD, 1),
                        4, 8, 0.125F
                ),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.OAK_LOG, 8),
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.RESIN.get(), 16),
                        4, 8, 0.125F
                ),
                (trader, rand) -> {
                    Item[] dyes = new Item[]{
                            Items.BLACK_DYE,
                            Items.WHITE_DYE,
                            Items.BLUE_DYE,
                            Items.BROWN_DYE,
                            Items.CYAN_DYE,
                            Items.GRAY_DYE,
                            Items.GREEN_DYE,
                            Items.LIGHT_BLUE_DYE,
                            Items.LIGHT_GRAY_DYE,
                            Items.LIME_DYE,
                            Items.MAGENTA_DYE,
                            Items.ORANGE_DYE,
                            Items.PINK_DYE,
                            Items.PURPLE_DYE,
                            Items.RED_DYE,
                            Items.YELLOW_DYE
                    };
                    Item dye = dyes[rand.nextInt(dyes.length - 1)];
                    return new MerchantOffer(
                            new ItemStack(dye, 16),
                            new ItemStack(Items.EMERALD, 1),
                            4, 8, 0.125F
                    );
                }

        );

        // Level 3: Improve a standard ref board
        final List<VillagerTrades.ItemListing> refDealerTrades3 = ImmutableList.of(
                (trader, random) -> {
                    Item[] dyes = new Item[]{
                            ItemsInit.PAINT_BUCKET_BLACK.get(),
                            ItemsInit.PAINT_BUCKET_WHITE.get(),
                            ItemsInit.PAINT_BUCKET_BLUE.get(),
                            ItemsInit.PAINT_BUCKET_BROWN.get(),
                            ItemsInit.PAINT_BUCKET_CYAN.get(),
                            ItemsInit.PAINT_BUCKET_GRAY.get(),
                            ItemsInit.PAINT_BUCKET_GREEN.get(),
                            ItemsInit.PAINT_BUCKET_LIGHT_BLUE.get(),
                            ItemsInit.PAINT_BUCKET_LIGHT_GRAY.get(),
                            ItemsInit.PAINT_BUCKET_LIME.get(),
                            ItemsInit.PAINT_BUCKET_MAGENTA.get(),
                            ItemsInit.PAINT_BUCKET_ORANGE.get(),
                            ItemsInit.PAINT_BUCKET_PINK.get(),
                            ItemsInit.PAINT_BUCKET_PURPLE.get(),
                            ItemsInit.PAINT_BUCKET_RED.get(),
                            ItemsInit.PAINT_BUCKET_YELLOW.get()
                    };
                    Item paint = dyes[random.nextInt(dyes.length - 1)];
                    return new MerchantOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(paint, 1),
                            4, 8, 0.5F
                    );
                },
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
                        4, 8, 0.125F
                ) {
                    @Override
                    public boolean satisfiedBy(ItemStack stack1, ItemStack stack2) {
                        return stack2.sameItem(ItemsInit.BLUEPRINT.get().getDefaultInstance());
                    }
                },
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 4),
                        new ItemStack(ItemsInit.SCUB_GLASS_LENS.get(), 1),
                        4, 8, 0.25F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 6),
                        StandardRefBoard.getWithRandomBadStats(random),
                        1, 8, 0.125F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 6),
                        StandardRefBoard.getWithRandomBadStats(random),
                        1, 8, 0.125F
                )
        );

        // Level 4: Goggles, Mid-Tier Wheels, Boards with Stats
        final List<VillagerTrades.ItemListing> refDealerTrades4 = ImmutableList.of(
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 6),
                            new ItemStack(ItemsInit.REFLECTION_FILM.get(), 8),
                            new ItemStack(ItemsInit.SCUB_GOGGLES.get(), 1),
                            1, 8, 0.125F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 32),
                            new ItemStack(ItemsInit.SCUB_GOGGLES.get(), 1),
                            1, 8, 0.125F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(WheelItemsInit.SOCKET_WRENCH.get(), 1),
                            1, 8, 0.0F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(WheelItemsInit.WHEEL_BEARING_ITEM.get(), 1),
                            1, 8, 0.5F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(WheelItemsInit.WHEEL_BEARING_MOLD_ITEM.get(), 1),
                            1, 8, 0.0F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(WheelItemsInit.OAK_WOOD_WHEEL_ITEM.get(), 1),
                            1, 8, 0.0F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(WheelItemsInit.STONE_WHEEL_ITEM.get(), 1),
                            1, 8, 0.0F),
                (trader, random) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(WheelItemsInit.IRON_WHEEL_ITEM.get(), 1),
                            1, 8, 0.0F),
                // Random stats board 1
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 16),
                        StandardRefBoard.getWithRandomStats(random),
                        1, 8, 0.125F
                ),
                // Random stats board 2
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 16),
                        StandardRefBoard.getWithRandomStats(random),
                        1, 8, 0.125F
                )
        );

        // Level 5: Elite board, High-Tier Wheels
        final List<VillagerTrades.ItemListing> refDealerTrades5 = ImmutableList.of(
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD_BLOCK, 4),
                        EliteRefBoard.getWithRandomStats(random),
                        1, 8, 0.25F
                ),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 16),
                        new ItemStack(WheelItemsInit.GOLD_WHEEL_ITEM.get(), 1),
                        1, 8, 0.125F),
                (trader, random) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 32),
                        new ItemStack(WheelItemsInit.DIAMOND_WHEEL_ITEM.get(), 1),
                        1, 8, 0.125F)
        );

        if (event.getType() == VillagersInit.REF_DEALER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            refDealerTrades1.forEach(o -> trades.get(1).add(o));

            refDealerTrades2.forEach(o -> trades.get(2).add(o));

            refDealerTrades3.forEach(o -> trades.get(3).add(o));

            refDealerTrades4.forEach(o -> trades.get(4).add(o));

            refDealerTrades5.forEach(o -> trades.get(5).add(o));
        }
    }
}
