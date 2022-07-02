package ca.bradj.eurekacraft.vehicles.control;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.EurekaCraftNetwork;
import ca.bradj.eurekacraft.core.network.msg.BoardControlMessage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class PlayerBoardControlProvider implements ICapabilitySerializable<CompoundTag> {

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static Capability<ControlCapability> PLAYER_BOARD_CONTROL = CapabilityManager.get(new CapabilityToken<>() {
    });

    private ControlCapability control = null;
    private final LazyOptional<ControlCapability> opt = LazyOptional.of(this::getOrInitializeControl);

    public static Control getControl(@Nonnull ICapabilityProvider player) {
        return player.getCapability(PLAYER_BOARD_CONTROL).orElse(ControlCapability.NONE).getControl();
    }

    public static void setControl(@Nonnull Entity p, Control c, boolean andPublish) {
        LazyOptional<ControlCapability> capability = p.getCapability(PLAYER_BOARD_CONTROL);
        capability.ifPresent(b -> {
            boolean wasChanged = b.setControl(c);
            if (wasChanged && andPublish) {
                logger.debug("Sending board control change packet: " + c);
                EurekaCraftNetwork.CHANNEL.send(
                        PacketDistributor.SERVER.noArg(), // TODO: Consider limiting reach
                        new BoardControlMessage(p.getId(), c)
                );
            }
        });
    }

    public void invalidate() {
        opt.invalidate();
    }

    public @NotNull ControlCapability getOrInitializeControl() {
        if (control == null) {
            control = ControlCapability.NONE;
        }
        return control;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == PLAYER_BOARD_CONTROL) {
            return opt.cast();
        }
        return LazyOptional.empty();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        getOrInitializeControl().getControl().saveNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrInitializeControl().getControl().fromNBT(nbt);
    }
}
