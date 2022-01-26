package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.vehicles.EntityRefBoard;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntitiesInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITIES, EurekaCraft.MODID
    );

    private static final String REF_BOARD_ID = new ResourceLocation(EurekaCraft.MODID, EntityRefBoard.ENTITY_ID).toString();

    public static final RegistryObject<EntityType<? extends Entity>> REF_BOARD = ENTITIES.register(
            EntityRefBoard.ENTITY_ID,
            () -> EntityType.Builder.of(EntityRefBoard::new, EntityClassification.MISC).build(REF_BOARD_ID)
    );

}
