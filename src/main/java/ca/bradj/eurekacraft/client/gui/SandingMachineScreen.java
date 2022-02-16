package ca.bradj.eurekacraft.client.gui;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SandingMachineScreen extends ContainerScreen<SandingMachineContainer> {

    private final ResourceLocation GUI = new ResourceLocation(EurekaCraft.MODID, "textures/screens/sanding_machine_screen.png"); // TODO: Specific UI

    public SandingMachineScreen(SandingMachineContainer container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        super.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(GUI);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Arrow
        this.blit(stack, this.leftPos + 116, this.topPos + 35, this.imageWidth + 5, 0, 17, 15);
        float wPercent = (float) this.getMenu().getCraftedPercent() / 100f;
        int width = (int) (17 * wPercent);
        this.blit(stack, this.leftPos + 116, this.topPos + 35, this.imageWidth + 5, 15, width, 15);
    }

}
