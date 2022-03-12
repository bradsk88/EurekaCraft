package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.entity.EntityRefBoard;
import ca.bradj.eurekacraft.entity.JudgeEntity;
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

    public static final RegistryObject<EntityType<EntityRefBoard>> REF_BOARD = ENTITIES.register(
            EntityRefBoard.ENTITY_ID,
            () -> EntityType.Builder.<EntityRefBoard>
                    of(EntityRefBoard::new, EntityClassification.MISC).
                    build(REF_BOARD_ID)
    );

    public static final RegistryObject<EntityType<JudgeEntity>> JUDGE;

    static {
        JUDGE = ENTITIES.register(
                JudgeEntity.ENTITY_ID.getPath(),
                () -> EntityType.Builder.
                        of(JudgeEntity::new, EntityClassification.CREATURE).
                        build(JudgeEntity.ENTITY_ID.getPath())
        );
    }

}
