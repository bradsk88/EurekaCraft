package ca.bradj.eurekacraft.client.gui;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SandingMachineScreen extends AbstractContainerScreen<SandingMachineContainer> {

    private final ResourceLocation GUI = new ResourceLocation(EurekaCraft.MODID, "textures/screens/sanding_machine_screen.png");

    public SandingMachineScreen(SandingMachineContainer container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        super.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Arrow
        this.blit(stack, this.leftPos + 116, this.topPos + 35, this.imageWidth + 5, 0, 17, 15);
        float wPercent = (float) this.getMenu().getCraftedPercent() / 100f;
        int width = (int) (17 * wPercent);
        this.blit(stack, this.leftPos + 116, this.topPos + 35, this.imageWidth + 5, 15, width, 15);
    }

}
