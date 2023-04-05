package ca.bradj.eurekacraft.core.init.items;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.RedResinBlock;
import ca.bradj.eurekacraft.blocks.ResinBlock;
import ca.bradj.eurekacraft.blocks.machines.RefTableBlock;
import ca.bradj.eurekacraft.blocks.machines.SandingMachineBlock;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.crop.FreshSeeds;
import ca.bradj.eurekacraft.crop.FreshSmellingLeaves;
import ca.bradj.eurekacraft.materials.*;
import ca.bradj.eurekacraft.materials.paint.PaintItem;
import ca.bradj.eurekacraft.vehicles.*;
import ca.bradj.eurekacraft.wearables.ScubGoggles;
import ca.bradj.eurekacraft.world.PosterBlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemsInit {
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EurekaCraft.MODID);

	static {
		WheelItemsInit.register(ITEMS);
	}

	public static final RegistryObject<Item> FRESH_SEEDS_ITEM = ITEMS.register(
			FreshSeeds.ITEM_ID, FreshSeeds::new
	);

	public static final RegistryObject<Item> FRESH_LEAVES_ITEM = ITEMS.register(
			FreshSmellingLeaves.ITEM_ID, FreshSmellingLeaves::new
	);

//	public static final RegistryObject<Item> BROKEN_REF_BOARD_BLOCK = ITEMS.register(
//			BrokenRefBoardBlock.ITEM_ID,
//			() -> new BlockItem(
//					BlocksInit.BROKEN_REF_BOARD.get(),
//					BrokenRefBoardBlock.ITEM_PROPS
//			)
//	);

	public static final RegistryObject<Item> RESIN_BLOCK = ITEMS.register(
			ResinBlock.ITEM_ID,
			() -> new BlockItem(
					BlocksInit.RESIN.get(),
					ResinBlock.ITEM_PROPS
			) {
				@Override
				public void appendHoverText(
						ItemStack p_40572_,
						@Nullable Level p_40573_,
						List<Component> p_40574_,
						TooltipFlag p_40575_
				) {
					p_40574_.add(
							new TranslatableComponent("item.eurekacraft.resins.subtitle").
									withStyle(ChatFormatting.GRAY)
					);
				}
			}
	);
	public static final RegistryObject<Item> RED_RESIN_BLOCK = ITEMS.register(
			RedResinBlock.ITEM_ID,
			() -> new BlockItem(
					BlocksInit.RED_RESIN.get(),
					RedResinBlock.ITEM_PROPS
			) {
				@Override
				public void appendHoverText(
						ItemStack p_40572_,
						@Nullable Level p_40573_,
						List<Component> p_40574_,
						TooltipFlag p_40575_
				) {
					p_40574_.add(
							new TranslatableComponent("item.eurekacraft.resins.subtitle").
									withStyle(ChatFormatting.GRAY)
					);
					p_40574_.add(
							new TranslatableComponent("item.eurekacraft.red_resins.subtitle").
									withStyle(ChatFormatting.GRAY)
					);
				}
			}
	);
	public static final RegistryObject<Item> POSTER_SPAWN_BLACK = ITEMS.register(
			"poster_spawn_block",
			PosterBlockItem::new
	);
	public static final RegistryObject<Item> TRAPAR_WOOD_BLOCK = ITEMS.register(
			"trapar_wood", // TODO: Add const
			() -> new BlockItem(
					BlocksInit.TRAPAR_WOOD_BLOCK.get(),
					new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP)
			)
	);
	public static final RegistryObject<Item> TRAPAR_LOG_BLOCK = ITEMS.register(
			"trapar_log", // TODO: Add const
			() -> new BlockItem(
					BlocksInit.TRAPAR_LOG_BLOCK.get(),
					new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP)
			)
	);
	public static final RegistryObject<Item> TRAPAR_SAPLING_BLOCK = ITEMS.register(
			"fresh_sapling", // TODO: Add const
			() -> new BlockItem(
					BlocksInit.TRAPAR_SAPLING.get(),
					new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP)
			)
	);

	public static final RegistryObject<Item> REF_TABLE_BLOCK = ITEMS.register(
			RefTableBlock.ITEM_ID,
			() -> new BlockItem(
					BlocksInit.REF_TABLE_BLOCK.get(),
					RefTableBlock.ITEM_PROPS
			)
	);
	public static final RegistryObject<Item> SANDING_MACHINE_BLOCK = ITEMS.register(
			SandingMachineBlock.ITEM_ID,
			() -> new BlockItem(
					BlocksInit.SANDING_MACHINE.get(),
					SandingMachineBlock.ITEM_PROPS
			)
	);

	public static final RegistryObject<Item> GLIDE_BOARD = ITEMS.register(
			RefBoardItem.ItemIDs.GLIDE_BOARD, GlideBoard::new
	);

	public static final RegistryObject<StandardRefBoard> STANDARD_REF_BOARD = ITEMS.register(
			RefBoardItem.ItemIDs.REF_BOARD, StandardRefBoard::new
	);

	public static final RegistryObject<Item> ELITE_BOARD = ITEMS.register(
			EliteRefBoard.ITEM_ID, EliteRefBoard::new
	);

	public static final RegistryObject<Item> BROKEN_BOARD = ITEMS.register(
			BrokenRefBoard.ITEM_ID, BrokenRefBoard::new
	);

	public static final RegistryObject<Item> REFLECTION_FILM_MOLD = ITEMS.register(
			ReflectionFilmMoldItem.ITEM_ID, ReflectionFilmMoldItem::new
	);

	public static final RegistryObject<Item> REFLECTION_FILM = ITEMS.register(
			ReflectionFilm.ITEM_ID, ReflectionFilm::new
	);

	public static final RegistryObject<Item> DIAMOND_REFLECTION_FILM = ITEMS.register(
			DiamondReflectionFilm.ITEM_ID, DiamondReflectionFilm::new
	);

	public static final RegistryObject<Item> REFLECTION_FILM_DUST = ITEMS.register(
			ReflectionFilmDust.ITEM_ID, ReflectionFilmDust::new
	);

	public static final RegistryObject<Item> CLAY_STICKY_DISC = ITEMS.register(
			ClayStickyDiscItem.ITEM_ID, ClayStickyDiscItem::new
	);

	public static final RegistryObject<Item> FLINT_STICKY_DISC = ITEMS.register(
			FlintStickyDiscItem.ITEM_ID, FlintStickyDiscItem::new
	);

	public static final RegistryObject<Item> FLINT_SANDING_DISC = ITEMS.register(
			FlintSandingDiscItem.ITEM_ID, FlintSandingDiscItem::new
	);

	public static final RegistryObject<Item> FLINT_SANDING_DISC_STACK = ITEMS.register(
			FlintSandingDiscStackItem.ITEM_ID, FlintSandingDiscStackItem::new
	);

	public static final RegistryObject<Item> SOFT_CHISEL = ITEMS.register(
			SoftChiselItem.ITEM_ID, SoftChiselItem::new
	);

	public static final RegistryObject<Item> PRECISION_WOOD = ITEMS.register(
			PrecisionWoodItem.ITEM_ID, PrecisionWoodItem::new
	);

	public static final RegistryObject<Item> PRECISION_WOOD_STICK = ITEMS.register(
			PrecisionWoodStickItem.ITEM_ID, PrecisionWoodStickItem::new
	);

	public static final RegistryObject<Item> REF_BOARD_CORE = ITEMS.register(
			RefBoardCoreItem.ITEM_ID, RefBoardCoreItem::new
	);

	public static final RegistryObject<Item> BLUEPRINT_POOR = ITEMS.register(
			BlueprintPoorItem.ITEM_ID, BlueprintPoorItem::new
	);


	public static final RegistryObject<Item> BLUEPRINT = ITEMS.register(
			BlueprintItem.ITEM_ID, BlueprintItem::new
	);

	public static final RegistryObject<Item> BLUEPRINT_ADVANCED = ITEMS.register(
			BlueprintAdvancedItem.ITEM_ID, BlueprintAdvancedItem::new
	);

	public static final RegistryObject<Item> BLUEPRINT_FOLDER = ITEMS.register(
			BlueprintFolderItem.ITEM_ID, BlueprintFolderItem::new
	);

	public static final RegistryObject<Item> POLISHED_OAK_SLAB = ITEMS.register(
			PolishedOakSlab.ITEM_ID, PolishedOakSlab::new
	);

	public static final RegistryObject<Item> RESINOUS_DUST = ITEMS.register(
			ResinousDust.ITEM_ID, ResinousDust::new
	);

	public static final RegistryObject<Item> RESIN = ITEMS.register(
			Resin.ITEM_ID, Resin::new
	);

	public static final RegistryObject<Item> RED_RESIN = ITEMS.register(
			RedResin.ITEM_ID, RedResin::new
	);

	public static final RegistryObject<Item> SCUB_GLASS_LENS = ITEMS.register(
			ScubGlassLens.ITEM_ID, ScubGlassLens::new
	);

	public static final RegistryObject<Item> SCUB_GOGGLES = ITEMS.register(
			ScubGoggles.ITEM_ID, ScubGoggles::new
	);

	public static final RegistryObject<PaintItem> PAINT_BUCKET_BLACK = ITEMS.register(
			PaintItem.BLACK_ITEM_ID, PaintItem::black
	);

	public static final RegistryObject<Item> PAINT_BUCKET_BLUE = ITEMS.register(
			PaintItem.BLUE_ITEM_ID, PaintItem::blue
	);

	public static final RegistryObject<Item> PAINT_BUCKET_BROWN = ITEMS.register(
			PaintItem.BROWN_ITEM_ID, PaintItem::brown
	);

	public static final RegistryObject<Item> PAINT_BUCKET_CYAN = ITEMS.register(
			PaintItem.CYAN_ITEM_ID, PaintItem::cyan
	);

	public static final RegistryObject<Item> PAINT_BUCKET_GRAY = ITEMS.register(
			PaintItem.GRAY_ITEM_ID, PaintItem::gray
	);

	public static final RegistryObject<Item> PAINT_BUCKET_GREEN = ITEMS.register(
			PaintItem.GREEN_ITEM_ID, PaintItem::green
	);

	public static final RegistryObject<Item> PAINT_BUCKET_LIGHT_BLUE = ITEMS.register(
			PaintItem.LIGHT_BLUE_ITEM_ID, PaintItem::lightblue
	);

	public static final RegistryObject<Item> PAINT_BUCKET_LIGHT_GRAY = ITEMS.register(
			PaintItem.LIGHT_GRAY_ITEM_ID, PaintItem::lightgray
	);

	public static final RegistryObject<Item> PAINT_BUCKET_LIME = ITEMS.register(
			PaintItem.LIME_ITEM_ID, PaintItem::lime
	);

	public static final RegistryObject<Item> PAINT_BUCKET_MAGENTA = ITEMS.register(
			PaintItem.MAGENTA_ITEM_ID, PaintItem::magenta
	);

	public static final RegistryObject<Item> PAINT_BUCKET_ORANGE = ITEMS.register(
			PaintItem.ORANGE_ITEM_ID, PaintItem::orange
	);

	public static final RegistryObject<Item> PAINT_BUCKET_PINK = ITEMS.register(
			PaintItem.PINK_ITEM_ID, PaintItem::pink
	);

	public static final RegistryObject<Item> PAINT_BUCKET_PURPLE = ITEMS.register(
			PaintItem.PURPLE_ITEM_ID, PaintItem::purple
	);

	public static final RegistryObject<Item> PAINT_BUCKET_RED = ITEMS.register(
			PaintItem.RED_ITEM_ID, PaintItem::red
	);

	public static final RegistryObject<Item> PAINT_BUCKET_WHITE = ITEMS.register(
			PaintItem.WHITE_ITEM_ID, PaintItem::white
	);

	public static final RegistryObject<Item> PAINT_BUCKET_YELLOW = ITEMS.register(
			PaintItem.YELLOW_ITEM_ID, PaintItem::yellow
	);

	public static final RegistryObject<Item> PHOTO = ITEMS.register(
			Photo.ITEM_ID, Photo::new
	);

	public static void register(IEventBus bus) {
		ITEMS.register(bus);
	}
}
