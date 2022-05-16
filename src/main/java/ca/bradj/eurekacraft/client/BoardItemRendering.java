package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BoardItemRendering {
    public static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static void initItemProperties(EntityAttributeCreationEvent event) {
        // TODO: Reimplement
//        logger.debug("initItemProperties");
//        // TODO: Central board registry to reduce duplication here
//        AmbientCreature.createLivingAttributes().build()
//        event.put(
//                ItemsInit.GLIDE_BOARD.get(),
//                AmbientCreature.createLivingAttributes().
//                        add(AttributesInit.BOARD_DEPLOYED).
//                        build()
//        );
//        ItemModelsProperties.register(
//                ItemsInit.STANDARD_BOARD.get(),
//                new ResourceLocation(EurekaCraft.MODID, "deployed"),
//                new DeployedPropGetter()
//        );
//        ItemModelsProperties.register(
//                ItemsInit.BROKEN_BOARD.get(),
//                new ResourceLocation(EurekaCraft.MODID, "deployed"),
//                new DeployedPropGetter()
//        );
//        ItemModelsProperties.register(
//                ItemsInit.REF_BOARD_CORE.get(),
//                new ResourceLocation(EurekaCraft.MODID, "deployed"),
//                new DeployedPropGetter()
//        );
//        ItemModelsProperties.register(
//                ItemsInit.ELITE_BOARD.get(),
//                new ResourceLocation(EurekaCraft.MODID, "deployed"),
//                new DeployedPropGetter()
//        );
    }

//
//    public static class DeployedPropGetter extends AttributeSupplier {
//
//        public DeployedPropGetter(Map<Attribute, AttributeInstance> p_22243_) {
//            super(p_22243_);
//        }
//
//        @Override
//        public float call(ItemStack item, @Nullable ClientLevel world, @Nullable LivingEntity entity) {
//            if (entity == null) {
//                return 0.0F;
//            }
//            if (!(item.getItem() instanceof RefBoardItem)) {
//                return 0.0F;
//            }
//            if (!(entity instanceof Player)) {
//                return 0.0F;
//            }
//            Optional<BoardType> boardType = PlayerDeployedBoard.get((Player) entity);
//            if (!boardType.isPresent()) {
//                return 0.0F;
//            }
//            if (BoardType.NONE.equals(boardType.get())) {
//                return 0.0F;
//            }
//            return 1.0F;
//        }
//    }
}
