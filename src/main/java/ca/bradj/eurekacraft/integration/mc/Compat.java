package ca.bradj.eurekacraft.integration.mc;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.client.BoardItemRendering;
import ca.bradj.eurekacraft.client.KeyInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import ca.bradj.eurekacraft.materials.BlueprintFolderItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Compat {
    public static final Random RANDOM = new Random();
    public static final ResourceLocation OVERWORLD = Level.OVERWORLD.location();
    public static final Style GRAY = Style.EMPTY.withColor(TextColor.parseColor("GRAY"));

    public static void playNeutralSound(
            ServerLevel serverLevel,
            BlockPos pos,
            SoundEvent sound
    ) {
        float volume = 0.5f;
        float pitchUpOrDown = 1.0F + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.4F;
        serverLevel.playSound(null, pos, sound, SoundSource.NEUTRAL, volume, pitchUpOrDown);
    }

    public static void playSound(
            ServerLevel serverLevel,
            BlockPos pos,
            SoundEvent sound,
            SoundSource source
    ) {
        float volume = 0.5f;
        float pitchUpOrDown = 1.0F + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.4F;
        serverLevel.playSound(null, pos, sound, source, volume, pitchUpOrDown);
    }

    public static Component translatable(String key) {
        return Component.translatable(key);
    }

    public static MutableComponent translatable(
            String key,
            Object... args
    ) {
        return Component.translatable(key, args);
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
        return Component.literal(x);
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
        return decoder.consumerNetworkThread(consumer);
    }

    public static void openScreen(
            ServerPlayer sender,
            MenuProvider menuProvider,
            Consumer<FriendlyByteBuf> consumer
    ) {
        NetworkHooks.openScreen(sender, menuProvider, consumer);
    }

    public static DeferredRegister<MenuType<?>> CreateMenuRegister(String modid) {
        return DeferredRegister.create(ForgeRegistries.MENU_TYPES, modid);
    }

    public static void enqueueOrLog(
            FMLCommonSetupEvent event,
            Runnable staticInitialize
    ) {
        event.enqueueWork(staticInitialize).exceptionally(ex -> {
            EurekaCraft.LOGGER.error("Enqueued work failed", ex);
            return null;
        });
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
        sender.sendSystemMessage(message);
    }

    public static void initCommands(IEventBus bus) {
        // Only required in 1.19 or above
        // CommandsInit.register(bus)
    }

    public static float nextFloat(LootContext context) {
        return context.getRandom().nextFloat();
    }

    public static RandomSrc random(Supplier<RandomSource> level) {
        RandomSource random = level.get();
        return new RandomSrc() {
            @Override
            public double nextDouble() {
                return random.nextDouble();
            }

            @Override
            public int nextInt(int xRange) {
                return random.nextInt(xRange);
            }

            @Override
            public boolean nextBoolean() {
                return random.nextBoolean();
            }
        };
    }

    public static LevelAccessor getWorld(LevelEvent evt) {
        return evt.getLevel();
    }

    public static CompoundTag getPersistentData(BlockEntity e) {
        return e.getPersistentData();
    }

    public static void openScreen(
            ServerPlayer player,
            BlueprintFolderItem blueprintFolderItem
    ) {
        NetworkHooks.openScreen(player, blueprintFolderItem);
    }

    public static List<? extends Player> getPlayers(Object event) {
        if (event instanceof LevelEvent le) {
            return le.getLevel().players();
        }
        throw new IllegalArgumentException(String.format("Unexpected event type %s", event.getClass()));
    }

    public static Player getPlayer(Object event) {
        if (event instanceof PlayerEvent pe) {
            return pe.getEntity();
        }
        throw new IllegalArgumentException(String.format("Unexpected event type %s", event.getClass()));
    }

    public static void storeOnWorld(
            ServerPlayer player,
            String id,
            EntityRefBoard.Data data
    ) {
        player.getLevel().getDataStorage().set(id, data);
    }

    public static void addModEventSubscribers(IEventBus modEventBus) {
        // Only required in 1.18
//        modEventBus.addListener((RegistryEvent.Register e) -> {
//            Registry.register(Registry.RECIPE_TYPE, RefTableRecipe.Type.ID, RefTableRecipe.Type.INSTANCE);
//            Registry.register(Registry.RECIPE_TYPE, SandingMachineRecipe.Type.ID, SANDING_MACHINE);
//        });
//        modEventBus.addListener((RegistryEvent<GlobalLootModifierSerializer<?>> e) -> {
//            event.getRegistry().registerAll(
//              LootAdditions.ALL.stream().map(v -> new LootAdditionModifier.Serializer().setRegistryName(new ResourceLocation(EurekaCraft.MODID, v))
//            );
//        });
        modEventBus.addListener((RegisterColorHandlersEvent.Item e) -> {
            e.register(BoardItemRendering::itemColor, ItemsInit.STANDARD_REF_BOARD.get());
        });
        modEventBus.addListener((ModelEvent.BakingCompleted e) -> {
            Map<ResourceLocation, BakedModel> models = e.getModels();
            BoardItemRendering.registerItemModel(models::get, models::put);
        });

        modEventBus.addListener((RegisterKeyMappingsEvent e) -> {
            e.register(KeyInit.accelerateFlightMapping);
            e.register(KeyInit.brakeFlightMapping);
        });
    }

    public static void openScreen(
            ServerPlayer player,
            MenuProvider te,
            BlockPos blockpos
    ) {
        NetworkHooks.openScreen(player, te, blockpos);
    }

    public static void getFromWorld(
            ServerPlayer playre,
            Function<CompoundTag, ? extends SavedData> o,
            String id
    ) {
        playre.getLevel().getDataStorage().get(o, id);
    }

    public interface RandomSrc {
        double nextDouble();

        int nextInt(int xRange);

        boolean nextBoolean();
    }

    public static class RecipeType<T extends Recipe<?>> implements net.minecraft.world.item.crafting.RecipeType<T> {
        // 1.18
//        @Override
//        public <C extends Container> Optional<RefTableRecipe> tryMatch(Recipe<C> p_44116_, Level p_44117_, C p_44118_) {
//            return net.minecraft.world.item.crafting.RecipeType.super.tryMatch(p_44116_, p_44117_, p_44118_);
//        }
    }

    public abstract static class RecipeSerializer<T extends Recipe<?>>
//            extends ForgeRegistryEntry<net.minecraft.world.item.crafting.RecipeSerializer<?>>
            implements net.minecraft.world.item.crafting.RecipeSerializer<T> {
    }

    public static abstract class AbstractTreeGrower extends net.minecraft.world.level.block.grower.AbstractTreeGrower {
        private final Holder<ConfiguredFeature<TreeConfiguration, ?>> tree;

        public AbstractTreeGrower(Holder<ConfiguredFeature<TreeConfiguration, ?>> traparTree) {
            tree = traparTree;
        }

        @Override
        protected @Nullable Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(
                RandomSource p_222910_,
                boolean p_222911_
        ) {
            return tree;
        }
    }
}
