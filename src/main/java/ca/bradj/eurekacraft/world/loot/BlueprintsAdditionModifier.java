package ca.bradj.eurekacraft.world.loot;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BlueprintsAdditionModifier extends LootModifier {

    public static final Supplier<Codec<BlueprintsAdditionModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(
                            ForgeRegistries.ITEMS.getCodec()
                                    .fieldOf("addition")
                                    .forGetter(m -> m.addition)
                    )
                    .and(
                            Codec.FLOAT
                                    .fieldOf("chance")
                                    .forGetter(m -> m.chance)
                    )
                    .apply(inst, BlueprintsAdditionModifier::new)));

    private final Item addition;
    private final float chance;

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected BlueprintsAdditionModifier(
            LootItemCondition[] conditionsIn, Item addition, float chance
    ) {
        super(conditionsIn);
        this.addition = addition;
        this.chance = chance;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(
            ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!this.shouldAdd(context)) {
            return generatedLoot;
        }

        generatedLoot.add(new ItemStack(addition, 1));
        return generatedLoot;
    }

    private boolean shouldAdd(
            LootContext context
    ) {
        RandomSource random = context.getRandom();
        float rolled1 = random.nextFloat();
        float rolled2 = random.nextFloat();
        boolean passed1 = rolled1 < this.chance;
        boolean passed2 = rolled2 < this.chance;
        float luck = context.getLuck();
        boolean isLucky = luck > 0.5;
        if (isLucky) {
            return passed1 || passed2;
        }

        return passed1;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return null;
    }

}
