package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModelsInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.vehicles.*;
import ca.bradj.eurekacraft.vehicles.control.PlayerBoardControlProvider;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.List;

import static net.minecraft.world.InteractionHand.OFF_HAND;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, value = Dist.CLIENT)
public class BoardPlayerRenderHandler {

    private static final LazyOptional<List<ItemStack>> RENDERABLE_BOARDS = LazyOptional.of(() -> ImmutableList.of(
            ItemsInit.STANDARD_REF_BOARD.get().getDefaultInstance(),
            ItemsInit.ELITE_BOARD.get().getDefaultInstance(),
            ItemsInit.GLIDE_BOARD.get().getDefaultInstance()
    ));

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    @SubscribeEvent
    public static void playerRender(final RenderPlayerEvent.Pre event) {
        PlayerDeployedBoardProvider.getBoardTypeFor(event.getPlayer()).ifPresent(
                (PlayerDeployedBoard.DeployedBoard bt) -> renderPlayerWithBoard(event, bt)
        );
    }

    private static void renderPlayerWithBoard(final RenderPlayerEvent.Pre event, PlayerDeployedBoard.DeployedBoard bt) {
        if (BoardType.NONE.equals(bt.boardType)) {
            return;
        }

        AbstractBoardModel<?> model = ModelsInit.getModel(bt.boardType, bt.getColor());

        PoseStack matrixStack = event.getPoseStack();
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));

        LivingEntity living = event.getEntityLiving();
        living.animationSpeed = 0;
        living.yHeadRot = living.yBodyRot + 90;

        Vec3 rv = living.getForward().normalize();
        final int tipAmt = 10;
        switch (PlayerBoardControlProvider.getControl(event.getPlayer())) {
            case BRAKE -> {
                matrixStack.mulPose(Vector3f.XP.rotationDegrees((float) (-tipAmt * rv.x)));
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) (-tipAmt * rv.z)));
            }
            case ACCELERATE -> {
                matrixStack.mulPose(Vector3f.XP.rotationDegrees((float) (tipAmt * rv.x)));
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) (tipAmt * rv.z)));
            }
        }

        float newYRot = (float) Math.toRadians(-living.yBodyRot);

        VertexConsumer ivertexbuilder = event.getMultiBufferSource().getBuffer(model.getRenderType());
        model.getModelRenderer().yRot = newYRot;
        model.renderToBuffer(
                matrixStack, ivertexbuilder, event.getPackedLight(),
                OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F
        );

        if (bt.wheel.isPresent()) {
            WheelModel wModel = new WheelModel(bt.wheel.get());
            wModel.getModelRenderer().yRot = newYRot;
            wModel.renderToBuffer(
                    matrixStack, ivertexbuilder, event.getPackedLight(),
                    OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F
            );
        }
    }

    @SubscribeEvent
    public static void renderFirstPerson(RenderHandEvent event) {
        if (event.getHand() == OFF_HAND) {
            return;
        }
        Item item = event.getItemStack().getItem();
        if (!(item instanceof RefBoardItem)) {
            return;
        }
        if (!PlayerDeployedBoard.DeployedBoard.IsStackDeployed(event.getItemStack())) {
            return;
        }
        Color c = BoardColor.FromStack(event.getItemStack());
        if (isRenderableBoard(event.getItemStack())) {
            renderPlayerHandWithBoard(event, ((RefBoardItem) item).getBoardType(), c);
        }
    }

    private static boolean isRenderableBoard(ItemStack is) {
        for (ItemStack i : RENDERABLE_BOARDS.orElse(ImmutableList.of())) {
            if (is.sameItemStackIgnoreDurability(i)) {
                return true;
            }
        }
        return false;
    }

    private static void renderPlayerHandWithBoard(
            RenderHandEvent event, BoardType bt, Color c
    ) {
        if (BoardType.NONE.equals(bt)) {
            return;
        }

        PoseStack matrixStack = event.getPoseStack();

        AbstractBoardModel<?> model = ModelsInit.getModel(bt, c);

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-event.getInterpolatedPitch()));
        matrixStack.translate(0.4, -1, 0);

        VertexConsumer ivertexbuilder = event.getMultiBufferSource().getBuffer(model.getRenderType());
        model.renderToBuffer(
                matrixStack, ivertexbuilder, event.getPackedLight(),
                OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F
        );
    }
}

