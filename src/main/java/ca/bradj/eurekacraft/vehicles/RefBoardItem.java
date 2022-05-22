package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import ca.bradj.eurekacraft.interfaces.*;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import com.google.common.collect.MapMaker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class RefBoardItem extends Item implements ITechAffected, IBoardStatsGetterProvider {

    private static final String NBT_KEY_STATS = "stats";

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private static Map<Player, EntityRefBoard> spawnedGlidersMap = new MapMaker().weakKeys().weakValues().makeMap();

    private static final Item.Properties PROPS = new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);
    private final RefBoardStats baseStats;
    private final BoardType id;
    private final StatsGetter statsGetter;
    protected boolean canFly = true;

    protected RefBoardItem(RefBoardStats stats, BoardType boardId) {
        super(PROPS);
        this.baseStats = stats;
        this.id = boardId;
        this.statsGetter = new StatsGetter(stats);
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
                EntityRefBoard spawned = EntityRefBoard.spawnFromInventory(player, (ServerLevel) player.level, boardItem, this.id);
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

    public ResourceLocation getID() {
        return this.id;
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

    protected void storeStatsOnStack(ItemStack target, RefBoardStats refBoardStats) {
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
        if (!(inputBoard instanceof RefBoardItem)) {
            return;
        }
        Item targetBoard = targetStack.getItem();
        if (!(targetBoard instanceof  RefBoardItem)) {
            return;
        }
        Item techItem = techStack.getItem();
        if (!(techItem instanceof IBoardStatsModifier)) {
            return;
        }
        if (!(targetStack.getItem() instanceof IBoardStatsGetterProvider)) {
            return;
        }
        RefBoardStats originalStats = ((RefBoardItem) inputBoard).boardStatsGetter().getBoardStats(inputBoardStack);
        RefBoardStats newStats = ((IBoardStatsModifier) techItem).modifyBoardStats(originalStats); // TODO: Enforce maximum
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
