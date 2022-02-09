package ca.bradj.eurekacraft.client.gui;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
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
    protected void renderBg(MatrixStack stack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(GUI);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(stack, this.leftPos + 116, this.topPos + 35, this.imageWidth + 8, 0, 22, 15);
        float wPercent = (float) this.getMenu().getCraftedPercent() / 100f;
        int width = (int) (22 * wPercent);
        this.blit(stack, this.leftPos + 116, this.topPos + 35, this.imageWidth + 8, 15, width, 15);
    }

}
