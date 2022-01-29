package ca.bradj.eurekacraft.client.gui;

import ca.bradj.eurekacraft.container.RefTableContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class RefTableScreen extends ContainerScreen<RefTableContainer> {

    public RefTableScreen(RefTableContainer container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.width = 120;
        this.height = 100;
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.blit(p_230450_1_, 100, 100, 0, 0, this.height, this.width);
    }
}
