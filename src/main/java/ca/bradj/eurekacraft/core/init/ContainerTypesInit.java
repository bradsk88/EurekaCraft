package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.machines.RefTableTileEntity;
import ca.bradj.eurekacraft.blocks.machines.SandingMachineTileEntity;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerTypesInit {


        public static final DeferredRegister<MenuType<?>> TYPES = DeferredRegister.create(
                ForgeRegistries.CONTAINERS, EurekaCraft.MODID
        );

        public static final RegistryObject<MenuType<RefTableContainer>> REF_TABLE = TYPES.register(
                RefTableTileEntity.ENTITY_ID, () -> IForgeMenuType.create(RefTableContainer::new)
        );
        public static final RegistryObject<MenuType<SandingMachineContainer>> SANDING_MACHINE = TYPES.register(
                SandingMachineTileEntity.ENTITY_ID, () -> IForgeMenuType.create(SandingMachineContainer::new)
        );

}
