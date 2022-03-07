package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.ITechAffectable;
import ca.bradj.eurekacraft.materials.BlueprintItem;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class RefBoardItem extends Item implements ITechAffectable {

    private static final String NBT_KEY_STATS = "stats";

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private static Map<PlayerEntity, EntityRefBoard> spawnedGlidersMap = new MapMaker().weakKeys().weakValues().makeMap();

    private static final Item.Properties PROPS = new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);
    private final RefBoardStats baseStats;
    private final BoardType id;
    protected boolean canFly = true;

    protected RefBoardItem(RefBoardStats stats, BoardType boardId) {
        super(PROPS);
        this.baseStats = stats;
        this.id = boardId;
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
                if (glider.getHandHeld() == hand) despawnGlider(player, world, glider);
                // if deployed glider is in other hand, ignore
            } else spawnGlider(player, player.level, hand, this.id);
        }

        return ActionResult.success(s);
    }

    // Server Side Only
    private static void spawnGlider(PlayerEntity player, World world, Hand hand, BoardType id) {
        EntityRefBoard glider = new EntityRefBoard(player, world, hand);
        Vector3d position = player.position();
        glider.setPos(position.x, position.y, position.z);
        world.addFreshEntity(glider);
        spawnedGlidersMap.put(player, glider);
        PlayerDeployedBoard.set(player, id);
    }

    // Server Side only
    private static void despawnGlider(PlayerEntity player, World world, EntityRefBoard glider) {
        glider.kill();
        spawnedGlidersMap.remove(player);
        PlayerDeployedBoard.remove(player);
    }

    public RefBoardStats getStats() {
        return this.baseStats;
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
    public void applyTechItem(ItemStack blueprint, ItemStack target, Random rand) {
        if (blueprint.getItem() instanceof BlueprintItem) {
            RefBoardStats refBoardStats = BlueprintItem.getBoardStatsFromNBTOrCreate(blueprint, baseStats, rand);

            CompoundNBT nbt = refBoardStats.serializeNBT();

            if (target.getTag() == null) {
                target.setTag(new CompoundNBT());
            }
            target.getTag().put(NBT_KEY_STATS, nbt);
        }
    }

    RefBoardStats getStatsForStack(ItemStack stack) {
        if (stack.getTag() == null || !stack.getTag().contains(NBT_KEY_STATS)) {
            return baseStats;
        }
        CompoundNBT nbt = stack.getTag().getCompound(NBT_KEY_STATS);
        return RefBoardStats.FromNBT(nbt);
    }
}
