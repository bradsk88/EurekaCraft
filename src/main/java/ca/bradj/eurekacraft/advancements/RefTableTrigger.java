package ca.bradj.eurekacraft.advancements;

import ca.bradj.eurekacraft.EurekaCraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

import java.util.function.Predicate;

public class RefTableTrigger extends AbstractCriterionTrigger<RefTableTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "ref_table_trigger");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser parser) {
        ItemPredicate aitempredicate = ItemPredicate.fromJson(json.get("machine_output"));
        return new RefTableTrigger.Instance(predicate, aitempredicate);
    }

    @Override
    protected void trigger(ServerPlayerEntity p_235959_1_, Predicate<Instance> p_235959_2_) {
        super.trigger(p_235959_1_, p_235959_2_);
    }

    public void trigger(ServerPlayerEntity player, ItemStack refTableOutput) {
        super.trigger(player, (instance) -> instance.matches(refTableOutput));
    }

    public static class Instance extends CriterionInstance {
        private final ItemPredicate predicate;
        public Instance(
                EntityPredicate.AndPredicate p_i231464_2_, ItemPredicate predicate
        ) {
            super(RefTableTrigger.ID, p_i231464_2_);
            this.predicate = predicate;
        }
        public boolean matches(ItemStack refTableOutput) {
            return this.predicate.matches(refTableOutput);
        }
    }
}
