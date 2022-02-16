package ca.bradj.eurekacraft.client.gui;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class RefTableScreen extends ContainerScreen<RefTableContainer> {

    private final ResourceLocation GUI = new ResourceLocation(EurekaCraft.MODID, "textures/screens/ref_table_screen.png");

    public RefTableScreen(RefTableContainer container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        super.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack stack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(GUI);
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
