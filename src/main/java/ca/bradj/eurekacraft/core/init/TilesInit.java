package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.TraparWaveBlock;
import ca.bradj.eurekacraft.blocks.machines.RefTableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TilesInit {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(
            ForgeRegistries.TILE_ENTITIES, EurekaCraft.MODID
    );

    public static final RegistryObject<TileEntityType<RefTableTileEntity>> REF_TABLE = TILES.register(
            RefTableTileEntity.ENTITY_ID, () -> TileEntityType.Builder.of(
                    RefTableTileEntity::new, BlocksInit.REF_TABLE_BLOCK.get()
            ).build(null)
    );

    public static final RegistryObject<TileEntityType<TraparWaveBlock.TileEntity>> TRAPAR_WAVE = TILES.register(
            TraparWaveBlock.TileEntity.ID, () -> TileEntityType.Builder.of(
                    TraparWaveBlock.TileEntity::new, BlocksInit.TRAPAR_WAVE_BLOCK.get()
            ).build(null)
    );

}