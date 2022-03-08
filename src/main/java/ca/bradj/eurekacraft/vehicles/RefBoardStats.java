package ca.bradj.eurekacraft.vehicles;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.SerializationException;

import java.util.Random;

public class RefBoardStats implements INBTSerializable<CompoundNBT> {

    private static final String NBT_KEY_STATS_WEIGHT = "weight";
    private static final String NBT_KEY_STATS_SPEED = "speed";
    private static final String NBT_KEY_STATS_AGILITY = "agility";
    private static final String NBT_KEY_STATS_LIFT = "lift";
    // TODO: land resistance and surf

    private static final double MAX_SPEED = 1.0;
    private static final double MAX_AGILITY = 1.0;
    private static final double MIN_LIFT = 0.0;
    private static final double MAX_LIFT = 1.0;
    private static final double MAX_SURF = 1.0;

    public static RefBoardStats BadBoard = new RefBoardStats(1.0, 0.25, 0.25, 0.25);
    public static RefBoardStats HeavyBoard = new RefBoardStats(1.0, 0.5, 0.25, 0.25);
    public static RefBoardStats GlideBoard = new RefBoardStats(0.25, 0.5, MAX_AGILITY, MIN_LIFT).
            withLandResistance(0.80).
            WithSurf(0.80);
    public static RefBoardStats SurfBoard = new RefBoardStats(1.0, 0.75, MAX_AGILITY, MIN_LIFT).
            WithSurf(MAX_SURF);
    public static RefBoardStats StandardBoard = new RefBoardStats(0.5, 0.5, 0.5, 0.5);
    public static RefBoardStats SpeedBoard = new RefBoardStats(0.5, MAX_SPEED, 0.5, 0.5);
    public static RefBoardStats EliteBoard = new RefBoardStats(0.25, 0.75, MAX_AGILITY, MAX_LIFT);
    private double boardWeight;

    private double boardSpeed;
    private double turnSpeed;
    private double liftFactor;
    private double landResistance = 0;
    private boolean damaged;
    private double surf = 0.40;

    private RefBoardStats(
            double boardWeight,
            double boardSpeed, // This is a boost and is not coupled to weight
            double turnSpeed, // This is a boost and is not coupled to weight
            double liftFactor // This is a boost and is not coupled to weight
    ) {
        this.boardWeight = boardWeight;
        this.boardSpeed = boardSpeed;
        this.turnSpeed = turnSpeed;
        this.liftFactor = liftFactor;
    }

    private RefBoardStats(
            double boardWeight,
            double boardSpeed, // This is a boost and is not coupled to weight
            double turnSpeed, // This is a boost and is not coupled to weight
            double liftFactor, // This is a boost and is not coupled to weight
            double landResistance,
            double surf
    ) {
        this.boardWeight = boardWeight;
        this.boardSpeed = boardSpeed;
        this.turnSpeed = turnSpeed;
        this.liftFactor = liftFactor;
        this.landResistance = landResistance;
        this.surf = surf;
    }

    public static RefBoardStats FromNBT(CompoundNBT nbt) {
        RefBoardStats refBoardStats = new RefBoardStats(-1.0, -1.0, -1.0, -1.0);
        refBoardStats.deserializeNBT(nbt);
        if (refBoardStats.weight() < 0) {
            throw new SerializationException("Failed to load stats from nbt");
        }
        return refBoardStats;
    }

    public static RefBoardStats FromReferenceWithRandomOffsets(RefBoardStats creationReference, Random rand) {
        double weight = creationReference.weight();
        double speed = creationReference.speed() + 0.15 - (0.25 * rand.nextDouble());
        double agility = creationReference.agility() + 0.15 - (0.25 * rand.nextDouble());
        double lift = creationReference.lift() + 0.15 - (0.25 * rand.nextDouble());
        speed = Math.min(MAX_SPEED, speed);
        agility = Math.min(MAX_AGILITY, agility);
        lift = Math.min(MAX_LIFT, lift);
        return new RefBoardStats(weight, speed, agility, lift, creationReference.landResistance, creationReference.surf);
    }

    public RefBoardStats WithAllIncreased(double increase) {
        return new RefBoardStats(
                this.boardWeight, this.boardSpeed + increase, this.turnSpeed + increase, this.liftFactor + increase,
                this.landResistance, this.surf
        );
    }

    public RefBoardStats withLandResistance(double landResistance) {
        return new RefBoardStats(
                this.boardWeight, this.boardSpeed, this.turnSpeed, this.liftFactor, landResistance, this.surf
        );
    }

    private RefBoardStats WithSurf(double surf) {
        return new RefBoardStats(
                this.boardWeight, this.boardSpeed, this.turnSpeed, this.liftFactor, this.landResistance, surf
        );
    }

    public RefBoardStats damaged() {
        RefBoardStats refBoardStats = new RefBoardStats(
                this.boardWeight, this.boardSpeed, this.turnSpeed, this.liftFactor
        );
        refBoardStats.damaged = true;
        return refBoardStats;
    }

    public double weight() {
        return this.boardWeight;
    }

    public double speed() {
        return this.boardSpeed;
    }

    public double agility() {
        return this.turnSpeed;
    }

    public double lift() {
        return this.liftFactor;
    }

    public double landResist() {
        return this.landResistance;
    }

    public boolean isDamaged() {
        return this.damaged;
    }

    public double surf() {
        return this.surf;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putDouble(NBT_KEY_STATS_WEIGHT, weight());
        nbt.putDouble(NBT_KEY_STATS_SPEED, speed());
        nbt.putDouble(NBT_KEY_STATS_AGILITY, agility());
        nbt.putDouble(NBT_KEY_STATS_LIFT, lift());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.boardWeight = nbt.getDouble(NBT_KEY_STATS_WEIGHT);
        this.boardSpeed = nbt.getDouble(NBT_KEY_STATS_SPEED);
        this.turnSpeed = nbt.getDouble(NBT_KEY_STATS_AGILITY);
        this.liftFactor = nbt.getDouble(NBT_KEY_STATS_LIFT);
    }
}
