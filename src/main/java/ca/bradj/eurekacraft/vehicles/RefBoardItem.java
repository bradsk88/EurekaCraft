package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import com.google.common.collect.MapMaker;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class RefBoardItem extends Item {

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

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        // TODO: This doesn't work. Find the right hook. This is only for manually creeated baords. Get the stats from the blueprint for crafted boards.
        CompoundNBT nbt = itemstack.getTag();
        if (nbt != null && !nbt.contains("stats")) {
            Random rand = new Random();
            CompoundNBT stats = new CompoundNBT();
            // TODO: Clamp to min/max
            // TODO: Generate NBT and randomness from RefBoardStats.newNBT()
            stats.putDouble("weight", baseStats.weight());
            stats.putDouble("speed", baseStats.speed() + 1 - (2 * rand.nextDouble()));
            stats.putDouble("agility", baseStats.agility() + 1 - (2 * rand.nextDouble()));
            stats.putDouble("lift", baseStats.lift() + 1 - (2 * rand.nextDouble()));
            nbt.put("stats", stats);
        }
        return super.createEntity(world, location, itemstack);
    }

    RefBoardStats getStatsForStack(ItemStack stack) {
        if (stack.getTag() == null || !stack.getTag().contains("stats")) {
            logger.error("Board does not have NBT stats. Falling back to defaults");
            return baseStats;
        }
        CompoundNBT nbt = stack.getTag().getCompound("stats");
        return RefBoardStats.FromNBT(nbt);
    }
}
