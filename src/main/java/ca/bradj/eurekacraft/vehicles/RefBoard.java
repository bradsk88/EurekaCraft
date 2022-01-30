package ca.bradj.eurekacraft.vehicles;

import com.google.common.collect.MapMaker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Map;

public abstract class RefBoard extends Item {

    private static Map<PlayerEntity, EntityRefBoard> spawnedGlidersMap = new MapMaker().weakKeys().weakValues().makeMap();

    private static final Item.Properties PROPS = new Item.Properties().tab(ItemGroup.TAB_MISC);
    private final RefBoardStats stats;

    protected RefBoard(RefBoardStats stats) {
        super(PROPS);
        this.stats = stats;
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
                despawnGlider(player, glider);
            }
            if (glider != null && glider.isAlive()) {
                if (glider.getHandHeld() == hand) despawnGlider(player, glider);
                // if deployed glider is in other hand, ignore
            } else spawnGlider(player, player.level, hand);
        }



        return ActionResult.success(s);
    }


    private static void spawnGlider(PlayerEntity player, World world, Hand hand) {
        EntityRefBoard glider = new EntityRefBoard(player, world, hand);
        Vector3d position = player.position();
        glider.setPos(position.x, position.y, position.z);
        world.addFreshEntity(glider);
        spawnedGlidersMap.put(player, glider);
    }

    private static void despawnGlider(PlayerEntity player, EntityRefBoard glider) {
        glider.kill();
        spawnedGlidersMap.remove(player);
    }

    public static class ItemIDs {
        public static final String REF_BOARD = "ref_board";
        public static String GLIDE_BOARD = "glide_board";
    }
}
