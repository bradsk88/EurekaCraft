package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import ca.bradj.eurekacraft.interfaces.*;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import com.google.common.collect.MapMaker;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
    private static Map<PlayerEntity, EntityRefBoard> spawnedGlidersMap = new MapMaker().weakKeys().weakValues().makeMap();

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
            return RefBoardStats.FromNBT(itemStack.getTag().getCompound(NBT_KEY_STATS));
        }
        return ((RefBoardItem) itemStack.getItem()).baseStats;
    }

    @Override
    public ItemStack getDefaultInstance() {
        return super.getDefaultInstance();
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack s = player.getItemInHand(hand);

        if (player.isOnGround()) {
            return ActionResult.pass(s);
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
                EntityRefBoard spawned = EntityRefBoard.spawnFromInventory(player, (ServerWorld) player.level, boardItem, this.id);
                if (spawned == null) {
                    spawnedGlidersMap.remove(player);
                } else {
                    spawnedGlidersMap.put(player, spawned);
                }
            }
        }

        return ActionResult.success(s);
    }

    // Server Side only
    private static void despawnGlider(PlayerEntity player, World world, EntityRefBoard glider) {
        glider.kill();
        spawnedGlidersMap.remove(player);
        PlayerDeployedBoard.remove(player);
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
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        RefBoardStats stats = getStatsForStack(stack);
        tooltip.add(new StringTextComponent("Speed: " + (int) (stats.speed() * 100))); // TODO: Translate
        tooltip.add(new StringTextComponent("Agility: " + (int) (stats.agility() * 100))); // TODO: Translate
        tooltip.add(new StringTextComponent("Lift: " + (int) (stats.lift() * 100))); // TODO: Translate
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

    private void storeStatsOnStack(ItemStack target, RefBoardStats refBoardStats) {
        CompoundNBT nbt = refBoardStats.serializeNBT();

        if (target.getTag() == null) {
            target.setTag(new CompoundNBT());
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
        CompoundNBT nbt = stack.getTag().getCompound(NBT_KEY_STATS);
        return RefBoardStats.FromNBT(nbt);
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
            CompoundNBT nbt = stack.getTag().getCompound(NBT_KEY_STATS);
            return RefBoardStats.FromNBT(nbt);
        }
    }
}
