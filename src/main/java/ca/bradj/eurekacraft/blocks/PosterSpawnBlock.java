package ca.bradj.eurekacraft.blocks;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.wrappers.EntityBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PosterSpawnBlock extends EntityBlock {

    private static String NBT_RECIPE_CATEGORY = "recipe_category";

    private static Map<String, ItemProvider[][][]> recipes = new HashMap<>();

    private interface ItemProvider {
        Item get();
    }

    public static void initRecipes() {
        recipes.put("crafting_table_recipes", new ItemProvider[][][]{
                {
                        new ItemProvider[]{
                                () -> Items.COBBLESTONE, () -> Items.COBBLESTONE, () -> Items.COBBLESTONE,
                        },
                        new ItemProvider[]{
                                () -> Items.OAK_PLANKS, ItemsInit.BLUEPRINT::get, () -> Items.OAK_PLANKS,
                        },
                        new ItemProvider[]{
                                () -> Items.OAK_PLANKS, () -> Items.OAK_PLANKS, () -> Items.OAK_PLANKS,
                        }
                },
                {
                        new ItemProvider[]{
                                ItemsInit.POLISHED_OAK_SLAB::get, ItemsInit.POLISHED_OAK_SLAB::get, ItemsInit.POLISHED_OAK_SLAB::get,
                        },
                        new ItemProvider[]{
                                ItemsInit.RESIN::get, ItemsInit.RESIN::get, ItemsInit.RESIN::get,
                        },
                        new ItemProvider[]{
                                () -> Items.AIR, () -> Items.IRON_BLOCK, () -> Items.AIR,
                        }
                },
                {
                        new ItemProvider[]{
                                () -> Items.FLINT, () -> Items.FLINT, () -> Items.FLINT,
                        },
                        new ItemProvider[]{
                                () -> Items.FLINT, ItemsInit.CLAY_STICKY_DISC::get, () -> Items.FLINT,
                        },
                        new ItemProvider[]{
                                () -> Items.FLINT, () -> Items.FLINT, () -> Items.FLINT,
                        }
                },
                {
                        new ItemProvider[]{
                                () -> Items.CLAY_BALL, () -> Items.PAPER, () -> Items.CLAY_BALL,
                        },
                        new ItemProvider[]{
                                () -> Items.PAPER, () -> Items.CLAY_BALL, () -> Items.PAPER,
                        },
                        new ItemProvider[]{
                                () -> Items.CLAY_BALL, () -> Items.PAPER, () -> Items.CLAY_BALL,
                        }
                },
                {
                        new ItemProvider[]{
                                ItemsInit.PRECISION_WOOD::get, ItemsInit.PRECISION_WOOD::get, ItemsInit.PRECISION_WOOD::get,
                        },
                        new ItemProvider[]{
                                ItemsInit.PRECISION_WOOD::get, ItemsInit.RESIN::get, ItemsInit.PRECISION_WOOD::get,
                        },
                        new ItemProvider[]{
                                () -> Items.AIR, () -> Items.LAVA_BUCKET, () -> Items.AIR,
                        }
                },
                {
                        new ItemProvider[]{
                                () -> Items.IRON_INGOT, () -> Items.AIR, () -> Items.AIR,
                        },
                        new ItemProvider[]{
                                () -> Items.AIR, ItemsInit.PRECISION_WOOD_STICK::get, () -> Items.AIR,
                        },
                        new ItemProvider[]{
                                () -> Items.AIR, () -> Items.AIR, () -> Items.AIR,
                        }
                },
                {
                        new ItemProvider[]{
                                ItemsInit.PRECISION_WOOD::get, () -> Items.AIR, () -> Items.AIR,
                        },
                        new ItemProvider[]{
                                () -> Items.AIR, ItemsInit.PRECISION_WOOD::get, () -> Items.AIR,
                        },
                        new ItemProvider[]{
                                () -> Items.AIR, () -> Items.AIR, () -> Items.AIR,
                        }
                },
        });
    }

    private static final String[] supportedRecipes = new String[]{
            "crafting_table_recipes",
            "ref_table_recipes",
    };

    public static final String ITEM_ID = "poster_spawn_block";
    private Entity entity;

    public PosterSpawnBlock() {
        super(
                BlockBehaviour.Properties.
                        of(Material.WOOD).
                        strength(1f)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        this.entity = TilesInit.POSTER_BLOCK.get().create(pos, state);
        return this.entity;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState blockState,
            BlockEntityType<T> entityType
    ) {
        return level.isClientSide ? null : createTickerHelper(
                entityType, TilesInit.POSTER_BLOCK.get(), Entity::tick
        );
    }

    @Override
    public InteractionResult use(
            BlockState p_60503_,
            Level level,
            BlockPos p_60505_,
            Player p_60506_,
            InteractionHand p_60507_,
            BlockHitResult p_60508_
    ) {
        int catIdx = entity.getNextRecipeCategoryIndex(level);
        if (level.isClientSide()) {
            Minecraft.getInstance().gui.setOverlayMessage(
                    Component.literal("setting recipe category to " + supportedRecipes[catIdx]),
                    false
            );
            return InteractionResult.sidedSuccess(true);
        } else {
            entity.setRecipeCategoryIndex(level, catIdx);
            return InteractionResult.sidedSuccess(false);
        }
    }

    private static class RecipeBlockPos extends BlockPos {

        private final int i;
        private final int j;

        public RecipeBlockPos(
                int x,
                int y,
                int z,
                int i,
                int j
        ) {
            super(x, y, z);
            this.i = i;
            this.j = j;
        }
    }

    private static class DetectResult {
        private final Collection<RecipeBlockPos> pos;
        private final Direction facing;

        public DetectResult(
                Collection<RecipeBlockPos> pos,
                Direction facing
        ) {
            this.pos = pos;
            this.facing = facing;
        }
    }

    public static class Entity extends BlockEntity {

        public static final String ID = "poster_spawn_block_entity";
        private Entity mainBlock;

        public Entity(
                BlockPos p_155229_,
                BlockState p_155230_
        ) {
            super(TilesInit.POSTER_BLOCK.get(), p_155229_, p_155230_);
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            super.deserializeNBT(nbt);
        }

        public static void tick(
                Level level,
                BlockPos blockPos,
                BlockState state,
                Entity e
        ) {
            for (Player p : level.players()) {
                if (p.isCreative()) {
                    return;
                }
            }
            // Begin converting blocks immediately if no one is in creative
            int idx = e.getRecipeCategoryIndex(level);
            if (idx != 0) {
                EurekaCraft.LOGGER.debug("Skipping poster conversion for index " + idx);
                // TODO: Support ref table recipes?
                return;
            }
            String cat = supportedRecipes[idx];
            ItemProvider[][][] rz = recipes.get(cat);
            ItemProvider[][] ipz = rz[level.getRandom().nextInt(rz.length)];

            Optional<DetectResult> shapeParts = Entity.detectCompleteShape(level, blockPos, state, e);
            if (shapeParts.isPresent()) {
                for (RecipeBlockPos bp : shapeParts.get().pos) {
                    ItemProvider ip = ipz[bp.j][bp.i];
                    replaceWithPartBlock(level, bp, ip.get(), shapeParts.get().facing);
                }
            }
        }

        int getRecipeCategoryIndex(Level level) {
            this.determineMainBlock(level);
            if (this.mainBlock.getTileData().contains(NBT_RECIPE_CATEGORY)) {
                return this.getTileData().getInt(NBT_RECIPE_CATEGORY);
            }
            this.mainBlock.getTileData().putInt(NBT_RECIPE_CATEGORY, 0);
            return 0;
        }

        private void determineMainBlock(Level level) {
            if (this.mainBlock != null) {
                return;
            }
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    BlockPos bp = this.getBlockPos().relative(Direction.Axis.X, i).relative(Direction.Axis.Y, j);
                    for (Direction d : Direction.Plane.VERTICAL) {
                        BlockEntity be = level.getBlockEntity(bp.relative(d));
                        if (be instanceof Entity) {
                            if (((Entity) be).mainBlock != null) {
                                this.mainBlock = ((Entity) be).mainBlock;
                                return;
                            }
                        }
                    }
                }
            }
            this.mainBlock = this;
        }

        private static void replaceWithPartBlock(
                Level level,
                BlockPos bp,
                Item i,
                Direction frameDir
        ) {
            EurekaCraft.LOGGER.debug("Setting spawn block to part: " + bp);
            level.removeBlockEntity(bp);
            level.removeBlock(bp, true);
            ItemFrame iF = new ItemFrame(level, bp, frameDir);
            iF.setItem(new ItemStack(i));
            level.addFreshEntity(iF);
        }

        private static Optional<DetectResult> detectCompleteShape(
                Level level,
                BlockPos blockPos,
                BlockState state,
                Entity e
        ) {
            DetectResult shapeEntities = detectCompleteShapeOnAxis(
                    level,
                    blockPos,
                    Direction.Axis.X,
                    Direction.SOUTH,
                    Direction.NORTH
            );
            if (shapeEntities.pos.size() == 9) {
                return Optional.of(shapeEntities);
            }
            shapeEntities = detectCompleteShapeOnAxis(
                    level,
                    blockPos,
                    Direction.Axis.Z,
                    Direction.EAST,
                    Direction.WEST
            );
            if (shapeEntities.pos.size() == 9) {
                return Optional.of(shapeEntities);
            }
            return Optional.empty();
        }

        @NotNull
        private static PosterSpawnBlock.DetectResult detectCompleteShapeOnAxis(
                Level level,
                BlockPos blockPos,
                Direction.Axis axis,
                Direction positive,
                Direction negative
        ) {
            List<RecipeBlockPos> shapeEntities = new ArrayList<>();

            int airPlus = 0;
            int airMinus = 0;
            for (int i = -1; i < 2; i++) {
                BlockPos lrBP = blockPos.relative(axis, i);
                for (int j = -1; j < 2; j++) {
                    BlockPos bp = lrBP.relative(Direction.Axis.Y, j);
                    if (level.getBlockState(bp.relative(positive)).isAir()) {
                        airPlus++;
                    }
                    if (level.getBlockState(bp.relative(negative)).isAir()) {
                        airMinus++;
                    }
                    if (level.getBlockState(bp).getBlock() instanceof PosterSpawnBlock) {
                        shapeEntities.add(new RecipeBlockPos(bp.getX(), bp.getY(), bp.getZ(), i + 1, 1 - j));
                    }
                }
            }
            if (airPlus > airMinus) {
                return new DetectResult(shapeEntities, positive);
            }
            return new DetectResult(shapeEntities, negative);
        }

        public int getNextRecipeCategoryIndex(Level level) {
            this.determineMainBlock(level);
            int newValue;
            if (mainBlock.getTileData().contains(NBT_RECIPE_CATEGORY)) {
                newValue = mainBlock.getTileData().getInt(NBT_RECIPE_CATEGORY) + 1;
                int maxValue = supportedRecipes.length;
                newValue = newValue % maxValue;
            } else {
                newValue = 0;
            }
            return newValue;
        }

        public void setRecipeCategoryIndex(
                Level level,
                int catIdx
        ) {
            this.determineMainBlock(level);
            mainBlock.getTileData().putInt(NBT_RECIPE_CATEGORY, catIdx);
        }
    }
}
