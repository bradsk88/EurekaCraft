package ca.bradj.eurekacraft.world.loot;

import ca.bradj.eurekacraft.interfaces.IInitializable;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class BlueprintsAdditionModifier extends LootModifier {

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

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (!this.shouldAdd(context)) {
            return generatedLoot;
        }

        ItemStack stack = new ItemStack(addition, 1);
        if (addition instanceof IInitializable) {
            ((IInitializable) addition).initialize(stack, context.getRandom());
        }
        generatedLoot.add(stack);
        return generatedLoot;
    }

    private boolean shouldAdd(LootContext context) {
        if (true) {
            return true;
        }
        Random random = context.getRandom();
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

    public static class Serializer extends GlobalLootModifierSerializer<BlueprintsAdditionModifier> {

        @Override
        public BlueprintsAdditionModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            Item addition = ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation(GsonHelper.getAsString(object, "addition"))
            );
            float chance = GsonHelper.getAsFloat(object, "chance");

            return new BlueprintsAdditionModifier(
                    ailootcondition, addition, chance
            );
        }

        @Override
        public JsonObject write(BlueprintsAdditionModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("addition", ForgeRegistries.ITEMS.getKey(instance.addition).toString());
            json.addProperty("chance", instance.chance);
            return json;
        }
    }
}
