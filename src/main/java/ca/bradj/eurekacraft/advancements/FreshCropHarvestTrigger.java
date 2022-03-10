package ca.bradj.eurekacraft.advancements;

import ca.bradj.eurekacraft.EurekaCraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

import java.util.function.Predicate;

public class FreshCropHarvestTrigger extends AbstractCriterionTrigger<FreshCropHarvestTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "fresh_seeds_harvest_trigger");
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser parser) {
        return new FreshCropHarvestTrigger.Instance(predicate);
    }

    @Override
    protected void trigger(ServerPlayerEntity p_235959_1_, Predicate<Instance> p_235959_2_) {
        super.trigger(p_235959_1_, p_235959_2_);
    }

    public void trigger(ServerPlayerEntity player) {
        super.trigger(player, Instance::matches);
    }

    public static class Instance extends CriterionInstance {
        public Instance(
                EntityPredicate.AndPredicate p_i231464_2_
        ) {
            super(FreshCropHarvestTrigger.ID, p_i231464_2_);
        }
        public boolean matches() {
            return true;
        }
    }
}
