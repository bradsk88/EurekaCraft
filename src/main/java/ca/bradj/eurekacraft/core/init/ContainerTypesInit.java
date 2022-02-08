package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.machines.RefTableTileEntity;
import ca.bradj.eurekacraft.blocks.machines.SandingMachineTileEntity;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypesInit {


        public static final DeferredRegister<ContainerType<?>> TYPES = DeferredRegister.create(
                ForgeRegistries.CONTAINERS, EurekaCraft.MODID
        );

        public static final RegistryObject<ContainerType<RefTableContainer>> REF_TABLE = TYPES.register(
                RefTableTileEntity.ENTITY_ID, () -> IForgeContainerType.create(RefTableContainer::new)
        );
        public static final RegistryObject<ContainerType<SandingMachineContainer>> SANDING_MACHINE = TYPES.register(
                SandingMachineTileEntity.ENTITY_ID, () -> IForgeContainerType.create(SandingMachineContainer::new)
        );

}
