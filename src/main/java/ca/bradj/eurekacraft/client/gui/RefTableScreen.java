package ca.bradj.eurekacraft.client.gui;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RefTableScreen extends AbstractContainerScreen<RefTableContainer> {

    private final ResourceLocation GUI = new ResourceLocation(EurekaCraft.MODID, "textures/screens/ref_table_screen.png");

    public RefTableScreen(RefTableContainer container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        super.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Arrow
        this.blit(stack, this.leftPos + 116, this.topPos + 35, this.imageWidth + 5, 0, 17, 15);
        float wPercent = (float) this.getMenu().getCraftedPercent() / 100f;
        int width = (int) (17 * wPercent);
        this.blit(stack, this.leftPos + 116, this.topPos + 35, this.imageWidth + 5, 15, width, 15);

        // Fire
        int fireLeft = this.leftPos + RefTableContainer.leftOfFuel + 1;
        int fireTop = this.topPos + RefTableContainer.topOfFuel - RefTableContainer.boxHeight + RefTableContainer.margin;
        this.blit(stack, fireLeft, fireTop, 198, 0, 13, 13);
        float cPercent = (float) this.getMenu().getFirePercent() / 100f;
        int done = 13 - (int) (Math.ceil(13 * cPercent));
        int size = 13 - done;
        this.blit(stack, fireLeft, fireTop + done, 198, 13 + done, 13, size);
    }

}
