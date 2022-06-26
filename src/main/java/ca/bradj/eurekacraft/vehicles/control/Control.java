package ca.bradj.eurekacraft.vehicles.control;

import net.minecraft.nbt.CompoundTag;

public enum Control {
    NONE,
    ACCELERATE,
    BRAKE;

    private static String NBT_KEY = "ca.bradj.board_control.value";

    public void saveNBT(CompoundTag tag) {
        tag.putString(NBT_KEY, name());
    }

    public Control fromNBT(CompoundTag tag) {
        return Control.valueOf(tag.getString(NBT_KEY));
    }

}