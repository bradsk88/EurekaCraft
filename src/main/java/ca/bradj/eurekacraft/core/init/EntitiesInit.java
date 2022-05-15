package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.entity.JudgeEntity;
import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EntitiesInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITIES, EurekaCraft.MODID
    );

    private static final String REF_BOARD_ID = new ResourceLocation(EurekaCraft.MODID, EntityRefBoard.ENTITY_ID).toString();

    public static final RegistryObject<EntityType<EntityRefBoard>> REF_BOARD = ENTITIES.register(
            EntityRefBoard.ENTITY_ID,
            () -> EntityType.Builder.<EntityRefBoard>
                            of(EntityRefBoard::new, MobCategory.MISC).
                    build(REF_BOARD_ID)
    );

    public static final RegistryObject<EntityType<JudgeEntity>> JUDGE;

    static {
        Supplier<EntityType<JudgeEntity>> builderFactory = () -> EntityType.Builder.of(
                (EntityType<JudgeEntity> et, Level w) -> new JudgeEntity(et, w),
                MobCategory.CREATURE
        ).build(JudgeEntity.ENTITY_ID.getPath());
        JUDGE = ENTITIES.register(JudgeEntity.ENTITY_ID.getPath(), builderFactory);
    }

}
