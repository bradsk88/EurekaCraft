package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import ca.bradj.eurekacraft.interfaces.*;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import ca.bradj.eurekacraft.vehicles.wheels.BoardWheels;
import ca.bradj.eurekacraft.vehicles.wheels.Wheel;
import com.google.common.collect.MapMaker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class RefBoardItem extends Item implements ITechAffected, IPaintable, IWrenchable, IBoardStatsGetterProvider {

    private static final String NBT_KEY_STATS = "stats";

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private static Map<Player, EntityRefBoard> spawnedGlidersMap = new MapMaker().weakKeys().weakValues().makeMap();

    private static final Item.Properties PROPS = new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);
    private final RefBoardStats baseStats;
    private BoardType board;
    private final StatsGetter statsGetter;
    protected boolean canFly = true;

    protected RefBoardItem(RefBoardStats stats, BoardType boardId) {
        super(PROPS);
        this.baseStats = stats;
        this.board = boardId;
        this.statsGetter = new StatsGetter(stats);
    }

    public BoardType getBoardType() {
        return board;
    }

    public static RefBoardStats GetStatsFromNBT(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof RefBoardItem)) {
            throw new IllegalArgumentException("Expected ItemStack of RefBoardItem");
        }
        if (itemStack.getTag() != null && itemStack.getTag().contains(NBT_KEY_STATS)) {
            return RefBoardStats.deserializeNBT(itemStack.getTag().getCompound(NBT_KEY_STATS));
        }
        return ((RefBoardItem) itemStack.getItem()).baseStats;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack defaultInstance = super.getDefaultInstance();
        storeStatsOnStack(defaultInstance, baseStats);
        return defaultInstance;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack s = player.getItemInHand(hand);

        if (player.isOnGround()) {
            return InteractionResultHolder.pass(s);
        }

        boolean serverSide = !world.isClientSide;
        if (serverSide) {
            EntityRefBoard glider = spawnedGlidersMap.get(player);
            if (glider != null && !glider.isAlive()) {
                despawnGlider(player, world, glider);
            }
            if (glider != null && glider.isAlive()) {
                despawnGlider(player, world, glider);
            } else {
                ItemStack boardItem = player.getItemInHand(hand);
                EntityRefBoard spawned = EntityRefBoard.spawnFromInventory(
                        player, (ServerLevel) player.level, boardItem, this.board
                );
                if (spawned == null) {
                    spawnedGlidersMap.remove(player);
                } else {
                    spawnedGlidersMap.put(player, spawned);
                }
            }
        }

        return InteractionResultHolder.success(s);
    }

    // Server Side only
    private static void despawnGlider(Player player, Level world, EntityRefBoard glider) {
        glider.kill();
        spawnedGlidersMap.remove(player);
        PlayerDeployedBoardProvider.removeBoardFor(player);
    }

    public boolean isDamagedBoard() {
        return this.baseStats.isDamaged();
    }

    public boolean canFly() {
        return this.canFly;
    }

    public static class ItemIDs {
        public static final String REF_BOARD = "ref_board";
        public static String GLIDE_BOARD = "glide_board";
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(
            ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn
    ) {
        RefBoardStats stats = getStatsForStack(stack);
        tooltip.add(new TextComponent("Speed: " + (int) (stats.speed() * 100))); // TODO: Translate
        tooltip.add(new TextComponent("Agility: " + (int) (stats.agility() * 100))); // TODO: Translate
        tooltip.add(new TextComponent("Lift: " + (int) (stats.lift() * 100))); // TODO: Translate

        Optional<Wheel> wheel = BoardWheels.FromStack(stack);
        if (wheel.isEmpty()) {
            tooltip.add(new TextComponent("Wheel: None")); // TODO: Translate
        } else {
            TranslatableComponent wheelName = new TranslatableComponent(wheel.get().getDescriptionId());
            tooltip.add(new TextComponent("Wheel: " + wheelName.getString()));
        }
    }

    @Override
    public void applyTechItem(Collection<ItemStack> inputs, ItemStack techItem, ItemStack target, Random random) {
        if (techItem.getItem() instanceof IBoardStatsFactoryProvider) {
            IBoardStatsFactory factory = ((IBoardStatsFactoryProvider) techItem.getItem()).getBoardStatsFactory();
            RefBoardStats refBoardStats = factory.getBoardStatsFromNBTOrCreate(techItem, baseStats, random);

            storeStatsOnStack(target, refBoardStats);
        }
        applyBoardShaping(inputs, techItem, target);
    }

    protected static void storeStatsOnStack(ItemStack target, RefBoardStats refBoardStats) {
        CompoundTag nbt = RefBoardStats.serializeNBT(refBoardStats);

        if (target.getTag() == null) {
            target.setTag(new CompoundTag());
        }
        target.getTag().put(NBT_KEY_STATS, nbt);
    }

    private void applyBoardShaping(Collection<ItemStack> inputs, ItemStack techStack, ItemStack targetStack) {
        if (inputs.size() != 1) {
            return;
        }
        ItemStack inputBoardStack = (ItemStack) inputs.toArray()[0];
        Item inputBoard = inputBoardStack.getItem();
        Item techItem = techStack.getItem();
        if (!(techItem instanceof IBoardStatsModifier)) {
            return;
        }
        Item targetBoard = targetStack.getItem();
        if (!(targetBoard instanceof IBoardStatsGetterProvider)) {
            return;
        }
        if (!(inputBoard instanceof RefBoardItem)) {
            return;
        }
        if (!(targetBoard instanceof RefBoardItem)) {
            return;
        }
        if (!inputBoard.equals(targetBoard)) {
            throw new IllegalStateException("Target board does not match input board for shaping");
        }

        RefBoardStats originalStats = ((RefBoardItem) inputBoard).boardStatsGetter().getBoardStats(inputBoardStack);
        RefBoardStats newStats = ((IBoardStatsModifier) techItem).modifyBoardStats(originalStats); // TODO: Enforce maximum

        targetStack.setTag(inputBoardStack.getTag());

        storeStatsOnStack(targetStack, newStats);
    }

    RefBoardStats getStatsForStack(ItemStack stack) {
        if (stack.getTag() == null || !stack.getTag().contains(NBT_KEY_STATS)) {
            return baseStats;
        }
        CompoundTag nbt = stack.getTag().getCompound(NBT_KEY_STATS);
        return RefBoardStats.deserializeNBT(nbt);
    }

    @Override
    public void applyPaint(Collection<ItemStack> inputs, ItemStack paint, ItemStack target) {
        if (!(paint.getItem() instanceof IColorSource)) {
            return;
        }

        ItemStack board = null;

        for (ItemStack is : inputs) {
            if (board != null) {
                break;
            }
            if (is.getItem() instanceof RefBoardItem) {
                board = is;
            }
        }

        if (board == null) {
            throw new IllegalStateException("No RefBoard found in inputs of RefBoard paint recipe");
        }
        if (board.getItem() != target.getItem()) {
            throw new IllegalStateException("Output item does not match input RefBoard for painting");
        }

        if (paint.getItem() instanceof IColorSource) {
            Color color = ((IColorSource) paint.getItem()).getColor();
            EurekaCraft.LOGGER.debug("Painted board " + color);
            target.setTag(board.getTag());
            BoardColor.AddToStack(target, color);
        }
    }

    private static class WrenchInputs {
        final ItemStack board;
        final ItemStack wheel;

        public WrenchInputs(ItemStack board, ItemStack wheel) {
            this.board = board;
            this.wheel = wheel;
        }

        public static WrenchInputs fromInputs(Collection<ItemStack> inputs) {
            ItemStack board = null;
            ItemStack wheel = null;

            for (ItemStack is : inputs) {
                if (wheel != null && board != null) {
                    break;
                }
                if (BoardWheels.isWheel(is.getItem())) {
                    wheel = is;
                    continue;
                }
                if (is.getItem() instanceof RefBoardItem) {
                    board = is;
                }
            }

            if (board == null) {
                throw new IllegalStateException("No RefBoard found in inputs of RefBoard wrench recipe");
            }
            return new WrenchInputs(board, wheel);
        }
    }

    @Override
    public boolean canApplyWrench(Collection<ItemStack> inputs, ItemStack techItem) {
        WrenchInputs i = WrenchInputs.fromInputs(inputs);
        Optional<Wheel> wheel = BoardWheels.FromStack(i.board);
        if (i.wheel == null) {
            // Only allow wrench to remove wheel if there is one on the board
            return wheel.isPresent();
        }
        // Only allow wrench to add wheel if board has none
        return wheel.isEmpty();
    }

    @Override
    public Optional<ItemStack> applyWrench(Collection<ItemStack> inputs, ItemStack target) {
        WrenchInputs i = WrenchInputs.fromInputs(inputs);

        if (i.board.getItem() != target.getItem()) {
            throw new IllegalStateException("Output item does not match input RefBoard");
        }
        target.setTag(i.board.getTag());

        if (i.wheel == null) {
            // Remove wheel
            Optional<Wheel> boardWheel = BoardWheels.FromStack(target);
            if (boardWheel.isEmpty()) {
                // TODO: Can we prevent the recipe from being accepted?
                EurekaCraft.LOGGER.warn("Could not remove wheel because RefBoard had no wheel");
                return Optional.empty();
            }
            BoardWheels.RemoveFromStack(target);
            EurekaCraft.LOGGER.debug("Removed wheel " + boardWheel.get() + " from stack");
            return Optional.of(new ItemStack(boardWheel.get()));
        }

        EurekaCraftItem wheelItem = (EurekaCraftItem) i.wheel.getItem();

        BoardWheels.AddToStack(target, wheelItem);
        return Optional.empty();
    }

    @Override
    public IBoardStatsGetter boardStatsGetter() {
        return this.statsGetter;
    }

    public static class StatsGetter implements IBoardStatsGetter {

        private final RefBoardStats baseStats;

        private StatsGetter(RefBoardStats baseStats) {
            this.baseStats = baseStats;
        }

        @Override
        public RefBoardStats getBoardStats(ItemStack stack) {
            if (stack.getTag() == null || !stack.getTag().contains(NBT_KEY_STATS)) {
                return baseStats;
            }
            CompoundTag nbt = stack.getTag().getCompound(NBT_KEY_STATS);
            return RefBoardStats.deserializeNBT(nbt);
        }
    }
}
