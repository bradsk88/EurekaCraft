package ca.bradj.eurekacraft.advancements;

import ca.bradj.eurekacraft.EurekaCraft;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Predicate;

public class BoardTrickTrigger extends SimpleCriterionTrigger<BoardTrickTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(
            EurekaCraft.MODID, "board_trick_trigger"
    );

    public enum Trick {
        Invalid,
        FirstRefFlight,
        Fly1000Blocks;

        private static final BiMap<Trick, String> stringVals = ImmutableBiMap.of(
                Trick.FirstRefFlight, "first_ref_flight",
                Trick.Fly1000Blocks, "fly_1000_blocks"
                // TODO: More tricks (grinding? arcade style flip tricks?)
        );

        public static Trick fromJSON(JsonElement trick_id) {
            String key = trick_id.getAsString();
            if (!stringVals.inverse().containsKey(key)) {
                throw new IllegalArgumentException(
                        String.format("Trick ID is unexpected: %s", trick_id)
                );
            }
            return stringVals.inverse().get(key);
        }

        public String getID() {
            return stringVals.get(this);
        }
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext parser) {
        if (!json.has("trick_id")) {
            throw new IllegalStateException(String.format(
                    "Recipe of type %s is missing trick_id [Recipe: %s]",
                    ID, parser.getAdvancementId()
            ));
        }

        return new BoardTrickTrigger.Instance(predicate, Trick.fromJSON(json.get("trick_id")));
    }

    @Override
    protected void trigger(ServerPlayer p_235959_1_, Predicate<Instance> p_235959_2_) {
        super.trigger(p_235959_1_, p_235959_2_);
    }

    public void trigger(ServerPlayer player, Trick trickID) {
        super.trigger(player, instance -> instance.matches(trickID));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final Trick trickID;

        public Instance(
                EntityPredicate.Composite p_i231464_2_, Trick trickID
        ) {
            super(BoardTrickTrigger.ID, p_i231464_2_);
            if (Trick.Invalid.equals(trickID)) {
                throw new IllegalArgumentException("trickID must not be invalid");
            }
            this.trickID = trickID;
        }

        public boolean matches(
                Trick trickID
        ) {
            return this.trickID.equals(trickID);
        }
    }
}
