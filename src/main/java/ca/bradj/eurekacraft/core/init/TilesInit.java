package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.TraparWaveBlock;
import ca.bradj.eurekacraft.blocks.machines.RefTableTileEntity;
import ca.bradj.eurekacraft.blocks.machines.SandingMachineTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TilesInit {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITIES, EurekaCraft.MODID
    );

    public static final RegistryObject<BlockEntityType<RefTableTileEntity>> REF_TABLE = TILES.register(
            RefTableTileEntity.ENTITY_ID, () -> BlockEntityType.Builder.of(
                    RefTableTileEntity::new, BlocksInit.REF_TABLE_BLOCK.get()
            ).build(null)
    );

    public static final RegistryObject<BlockEntityType<TraparWaveBlock.TileEntity>> TRAPAR_WAVE = TILES.register(
            TraparWaveBlock.TileEntity.ID, () -> BlockEntityType.Builder.of(
                    TraparWaveBlock.TileEntity::new, BlocksInit.TRAPAR_WAVE_BLOCK.get()
            ).build(null)
    );

    public static final RegistryObject<BlockEntityType<SandingMachineTileEntity>> SANDING_MACHINE = TILES.register(
            SandingMachineTileEntity.ENTITY_ID, () -> BlockEntityType.Builder.of(
                    SandingMachineTileEntity::new, BlocksInit.SANDING_MACHINE.get()
            ).build(null)
    );

}