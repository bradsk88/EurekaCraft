package ca.bradj.eurekacraft.world.loot;

import ca.bradj.eurekacraft.integration.mc.Compat;
import ca.bradj.eurekacraft.interfaces.IInitializable;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class LootAdditionModifier extends LootModifier {

    Supplier<Codec<LootAdditionModifier>> CODEC =
            Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ForgeRegistries.ITEMS.getCodec().fieldOf("addition").forGetter(m -> m.addition))
                    .and(ExtraCodecs.POSITIVE_FLOAT.fieldOf("chance").forGetter(m -> m.chance))
                    .apply(inst, LootAdditionModifier::new)));

    private final Item addition;
    private final float chance;

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected LootAdditionModifier(
            LootItemCondition[] conditionsIn,
            Item addition,
            float chance
    ) {
        super(conditionsIn);
        this.addition = addition;
        this.chance = chance;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(
            ObjectArrayList<ItemStack> generatedLoot,
            LootContext context
    ) {
        if (!this.shouldAdd(context)) {
            return generatedLoot;
        }

        ItemStack stack = new ItemStack(addition, 1);
        if (addition instanceof IInitializable) {
            ((IInitializable) addition).initialize(stack, Compat.random(context::getRandom));
        }
        generatedLoot.add(stack);
        return generatedLoot;
    }

    private boolean shouldAdd(LootContext context) {
        float rolled1 = Compat.nextFloat(context);
        float rolled2 = Compat.nextFloat(context);
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
        return CODEC.get();
    }
}
