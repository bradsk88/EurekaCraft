package ca.bradj.eurekacraft.advancements;

import ca.bradj.eurekacraft.EurekaCraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class RefTableTrigger extends SimpleCriterionTrigger<RefTableTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "ref_table_trigger");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext parser) {
        if (!json.has("machine_output")) {
            throw new IllegalStateException("Recipe of type " + ID + " is missing machine_output [Recipe: " + parser.getAdvancementId() + "]");
        }
        ItemPredicate aitempredicate = ItemPredicate.fromJson(json.get("machine_output"));
        return new RefTableTrigger.Instance(predicate, aitempredicate);
    }

    @Override
    protected void trigger(ServerPlayer p_235959_1_, Predicate<Instance> p_235959_2_) {
        super.trigger(p_235959_1_, p_235959_2_);
    }

    public void trigger(ServerPlayer player, ItemStack refTableOutput) {
        super.trigger(player, (instance) -> instance.matches(refTableOutput));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate predicate;
        public Instance(
                EntityPredicate.Composite p_i231464_2_, ItemPredicate predicate
        ) {
            super(RefTableTrigger.ID, p_i231464_2_);
            this.predicate = predicate;
        }
        public boolean matches(ItemStack refTableOutput) {
            return this.predicate.matches(refTableOutput);
        }
    }
}
