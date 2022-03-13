package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.entity.EntityRefBoard;
import ca.bradj.eurekacraft.entity.JudgeEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

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
        Supplier<EntityType<JudgeEntity>> builderFactory = () -> EntityType.Builder.of(
                (EntityType<JudgeEntity> et, World w) -> new JudgeEntity(et, w),
                EntityClassification.CREATURE
        ).build(JudgeEntity.ENTITY_ID.getPath());
        JUDGE = ENTITIES.register(JudgeEntity.ENTITY_ID.getPath(), builderFactory);
    }

}
