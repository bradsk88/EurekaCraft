package ca.bradj.eurekacraft.integration.mc;

import ca.bradj.eurekacraft.EurekaCraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Compat {
    public static final Random RANDOM = new Random();

    public static void playNeutralSound(
            ServerLevel serverLevel,
            BlockPos pos,
            SoundEvent sound
    ) {
        float volume = 0.5f;
        float pitchUpOrDown = 1.0F + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.4F;
        serverLevel.playSound(
                null,
                pos,
                sound,
                SoundSource.NEUTRAL,
                volume,
                pitchUpOrDown
        );
    }

    public static void playSound(
            ServerLevel serverLevel,
            BlockPos pos,
            SoundEvent sound,
            SoundSource source
    ) {
        float volume = 0.5f;
        float pitchUpOrDown = 1.0F + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.4F;
        serverLevel.playSound(
                null,
                pos,
                sound,
                source,
                volume,
                pitchUpOrDown
        );
    }

    public static Component translatable(String key) {
        return new TranslatableComponent(key);
    }

    public static MutableComponent translatable(
            String key,
            Object... args
    ) {
        return new TranslatableComponent(key, args);
    }

    public static Component translatableStyled(
            String s,
            Style style,
            Object... args
    ) {
        MutableComponent v = translatable(s, args);
        v.setStyle(style);
        return v;
    }

    public static Component literal(String x) {
        return new TextComponent(x);
    }

    public static <X> ArrayList<X> shuffle(
            Collection<X> c,
            ServerLevel serverLevel
    ) {
        ArrayList<X> list = new ArrayList<>(c);
        int size = list.size();
        for (int i = size; i > 1; --i) {
            Collections.swap(list, i - 1, serverLevel.getRandom().nextInt(i));
        }
        return list;
    }

    public static int nextInt(
            @Nullable ServerLevel server,
            int i
    ) {
        return server.getRandom().nextInt(i);
    }

    public static Direction getRandomHorizontal(ServerLevel serverLevel) {
        return Direction.Plane.HORIZONTAL.getRandomDirection(serverLevel.getRandom());
    }

    public static void setCutoutRenderType(Block block) {
        // Render layer is set via model JSON files
    }

    public static <MSG> SimpleChannel.MessageBuilder<MSG> withConsumer(
            SimpleChannel.MessageBuilder<MSG> decoder,
            BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer
    ) {
        return decoder.consumer(consumer);
    }

    public static void openScreen(
            ServerPlayer sender,
            MenuProvider menuProvider,
            Consumer<FriendlyByteBuf> consumer
    ) {
        NetworkHooks.openGui(sender, menuProvider, consumer);
    }

    public static DeferredRegister<MenuType<?>> CreateMenuRegister(String modid) {
        return DeferredRegister.create(ForgeRegistries.CONTAINERS, modid);
    }

    public static void enqueueOrLog(
            FMLCommonSetupEvent event,
            Runnable staticInitialize
    ) {
        event.enqueueWork(staticInitialize).exceptionally(
                ex -> {
                    EurekaCraft.LOGGER.error("Enqueued work failed", ex);
                    return null;
                }
        );
    }

    public static <X> Supplier<X> configGet(ForgeConfigSpec.ConfigValue<X> cfg) {
        return cfg::get;
    }

    public static boolean insertInNextOpenSlot(
            IItemHandler iItemHandler,
            ItemStack inserted,
            int targetSize
    ) {
        if (inserted.getOrCreateTag().isEmpty()) {
            for (int i = 0; i < iItemHandler.getSlots(); i++) {
                ItemStack stackInSlot = iItemHandler.getStackInSlot(i);
                if (stackInSlot.getOrCreateTag().isEmpty() && stackInSlot.sameItem(inserted)) {
                    if (stackInSlot.getCount() < targetSize) {
                        iItemHandler.insertItem(i, inserted, false);
                        return true;
                    }
                }
            }
        }
        for (int i = 0; i < iItemHandler.getSlots(); i++) {
            ItemStack stackInSlot = iItemHandler.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                iItemHandler.insertItem(i, inserted, false);
                return true;
            }
        }
        return false;
    }

    public static void drawDarkText(
            Font font,
            PoseStack stack,
            Component translatable,
            int x,
            int y
    ) {
        font.draw(stack, translatable, x, y, 0x00000000);
    }

    public static void drawLightText(
            Font font,
            PoseStack stack,
            String translatable,
            int x,
            int y
    ) {
        font.drawShadow(stack, translatable, x, y, 0xFFFFFFFF);
    }

    public static Component getItemName(Item item) {
        return translatable(getItemId(item).toString());
    }

    public static ResourceLocation getItemId(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }

    public static ResourceLocation getItemId(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public static boolean getRandomBool(@Nullable ServerLevel serverLevel) {
        return serverLevel.getRandom().nextBoolean();
    }

    public static int getRandomInt(
            ServerLevel serverLevel,
            int size
    ) {
        return serverLevel.getRandom().nextInt(size);
    }

    public static void sendMessage(
            ServerPlayer sender,
            Component message
    ) {
        sender.sendMessage(message, sender.getUUID());
    }

    public static void initCommands(IEventBus bus) {
        // Only required in 1.19 or above
        // CommandsInit.register(bus)
    }
}
