package ca.bradj.eurekacraft.render;// Made with Blockbench 4.1.3
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.resources.ResourceLocation;

public class RefBoardCoreModel extends RefBoardModel {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            EurekaCraft.MODID, "textures/render/oak_planks.png"
    );

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}

