package ca.bradj.eurekacraft.advancements;

import ca.bradj.eurekacraft.EurekaCraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Predicate;

public class FreshCropHarvestTrigger extends SimpleCriterionTrigger<FreshCropHarvestTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "fresh_seeds_harvest_trigger");
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext ctx) {
        return new FreshCropHarvestTrigger.Instance(predicate);
    }

    @Override
    protected void trigger(ServerPlayer p_235959_1_, Predicate<Instance> p_235959_2_) {
        super.trigger(p_235959_1_, p_235959_2_);
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, Instance::matches);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(
                EntityPredicate.Composite p_i231464_2_
        ) {
            super(FreshCropHarvestTrigger.ID, p_i231464_2_);
        }
        public boolean matches() {
            return true;
        }
    }
}
