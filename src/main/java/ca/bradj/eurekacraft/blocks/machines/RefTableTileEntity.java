package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.TilesInit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class RefTableTileEntity extends LockableLootTileEntity {
    public static final String ENTITY_ID = "ref_table_tile_entity";
    public static final TileEntityType<RefTableTileEntity> TYPE = TileEntityType.Builder.of(
            RefTableTileEntity::new, BlocksInit.REF_TABLE_BLOCK.get()
    ).build(null);


    private static int slots = 2;
    protected NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);

    public RefTableTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public RefTableTileEntity() {
        this(TilesInit.REF_TABLE.get());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + EurekaCraft.MODID + ".ref_table");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new RefTableContainer(id, player, this);
    }

    @Override
    public int getContainerSize() {
        return slots;
    }

}
