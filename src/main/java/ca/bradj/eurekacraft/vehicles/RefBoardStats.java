package ca.bradj.eurekacraft.vehicles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

public class RefBoardStats {

    private static final String NBT_KEY_STATS_WEIGHT = "weight";
    private static final String NBT_KEY_STATS_SPEED = "speed";
    private static final String NBT_KEY_STATS_AGILITY = "agility";
    private static final String NBT_KEY_STATS_LIFT = "lift";
    // TODO: land resistance and surf

    private static final double MIN_WEIGHT = 0.1;
    private static final double MAX_WEIGHT = 1.0;
    public static final double MAX_SPEED = 1.0;
    public static final double MIN_SPEED = 0.1;
    private static final double MAX_AGILITY = 1.0;
    public static final double NO_LIFT = 0.0;
    public static final double MIN_POSITIVE_LIFT = 0.1;
    private static final double MAX_LIFT = 1.0;
    private static final double MAX_SURF = 1.0;
    public static final double MAX_SURF_FOREVER = 1.1;

    public static final RefBoardStats BadBoard;
    public static final RefBoardStats HeavyBoard;
    public static final RefBoardStats GlideBoard;
    public static final RefBoardStats SurfBoard;
    public static final RefBoardStats StandardBoard;
    public static final RefBoardStats SpeedBoard;
    public static final RefBoardStats EliteBoard;

    static {
        BadBoard = new RefBoardStats("bad", 1.0, 0.25, 0.25, 0.25, 0, 0);
        HeavyBoard = new RefBoardStats("heavy", 1.0, 0.5, 0.25, 0.25, 0, 0);
        GlideBoard = new RefBoardStats("glide", 0.25, 0.25, 0.10, NO_LIFT, 0, 0).
                withLandResistance(0.80).
                WithSurf(0.80);
        SurfBoard = new RefBoardStats("surf", 1.0, 0.75, MAX_AGILITY, NO_LIFT, 0, 0).
                WithSurf(MAX_SURF);
        StandardBoard = new RefBoardStats("standard", 0.75, 0.5, 0.25, 0.5, 0, 0);
        SpeedBoard = new RefBoardStats("speed", 0.5, MAX_SPEED, 0.10, 0.5, 0, 0);
        EliteBoard = new RefBoardStats("elite", 0.25, 0.75, 0.75, 0.75, 25, 25);
    }

    private double boardWeight;

    private String id;
    private double boardSpeed;
    private double turnSpeed;
    private double liftFactor;
    private double landResistance ;
    private boolean damaged;
    private double surf;
    private double latentBraking;
    private double latentAccel;

    private RefBoardStats(
            String id,
            double boardWeight,
            double boardSpeed, // This is a boost and is not coupled to weight
            double turnSpeed, // This is a boost and is not coupled to weight
            double liftFactor, // This is a boost and is not coupled to weight
            double latentBraking,
            double latentAcceleration
    ) {
        this(id, boardWeight, boardSpeed, turnSpeed, liftFactor, latentBraking, latentAcceleration, 0, 0.4);
    }

    private RefBoardStats(
            String id,
            double boardWeight,
            double boardSpeed, // This is a boost and is not coupled to weight
            double turnSpeed, // This is a boost and is not coupled to weight
            double liftFactor, // This is a boost and is not coupled to weight
            double latentBraking,
            double latentAcceleration,
            double landResistance,
            double surf
    ) {
        this.id = id;
        this.boardWeight = Math.min(MAX_WEIGHT, Math.max(MIN_WEIGHT, boardWeight));
        this.boardSpeed = Math.min(MAX_SPEED, boardSpeed);
        this.turnSpeed = Math.min(MAX_AGILITY, turnSpeed);
        this.liftFactor = Math.min(MAX_LIFT, liftFactor);
        this.latentBraking = latentBraking;
        this.latentAccel = latentAcceleration;
        this.landResistance = landResistance;
        this.surf = surf;
    }

    public static RefBoardStats FromReferenceWithRandomOffsets(RefBoardStats creationReference, RandomSource rand) {
        double weight = creationReference.weight();
        double speed = creationReference.speed() + 0.15 - (0.25 * rand.nextDouble());
        double agility = creationReference.agility() + 0.15 - (0.25 * rand.nextDouble());
        double lift = creationReference.lift() + 0.15 - (0.25 * rand.nextDouble());
        speed = Math.min(MAX_SPEED, speed);
        agility = Math.min(MAX_AGILITY, agility);
        lift = Math.min(MAX_LIFT, lift);
        return new RefBoardStats(creationReference.id, weight, speed, agility, lift, creationReference.landResistance, creationReference.surf);
    }

    public static RefBoardStats Average(String id, Collection<RefBoardStats> inputStats) {
            
        double avgWeight = inputStats.parallelStream().collect(Collectors.averagingDouble(RefBoardStats::weight));
        double avgSpeed = inputStats.parallelStream().collect(Collectors.averagingDouble(RefBoardStats::speed));
        double avgAgility = inputStats.parallelStream().collect(Collectors.averagingDouble(RefBoardStats::agility));
        double avgLift = inputStats.parallelStream().collect(Collectors.averagingDouble(RefBoardStats::lift));
        double avgLandRes = inputStats.parallelStream().collect(Collectors.averagingDouble(RefBoardStats::landResist));
        double avgSurf = inputStats.parallelStream().collect(Collectors.averagingDouble(RefBoardStats::surf));
        return new RefBoardStats(
                id, avgWeight, avgSpeed, avgAgility, avgLift, avgLandRes, avgSurf
        );
    }

    public static boolean isElite(RefBoardStats creationReference) {
        return EliteBoard.id.equals(creationReference.id);
    }

    public RefBoardStats copy() {
        return new RefBoardStats(
                this.id, this.boardWeight, this.boardSpeed, this.turnSpeed, this.liftFactor, this.landResistance, this.surf
        );
    }

    public RefBoardStats WithAllIncreased(double increase) {
        return new RefBoardStats(
                this.id, this.boardWeight, this.boardSpeed + increase, this.turnSpeed + increase, this.liftFactor + increase,
                this.landResistance, this.surf
        );
    }

    public RefBoardStats withLandResistance(double landResistance) {
        return new RefBoardStats(
                this.id, this.boardWeight, this.boardSpeed, this.turnSpeed, this.liftFactor, landResistance, this.surf
        );
    }

    public RefBoardStats WithSurf(double surf) {
        RefBoardStats copy = this.copy();
        copy.surf = surf;
        return copy;
    }

    public RefBoardStats damaged() {
        RefBoardStats refBoardStats = new RefBoardStats(
                this.id, this.boardWeight, this.boardSpeed, this.turnSpeed, this.liftFactor, 0, 0
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

    public static CompoundTag serializeNBT(RefBoardStats stats) {
        CompoundTag nbt = new CompoundTag();
        double weight = stats.weight();
        double speed = stats.speed();
        double agility = stats.agility();
        double lift = stats.lift();
        if (weight == 0 || speed == 0 || agility == 0) {
            throw new IllegalStateException("Zero stat being written to NBT");
        }
        nbt.putDouble(NBT_KEY_STATS_WEIGHT, weight);
        nbt.putDouble(NBT_KEY_STATS_SPEED, speed);
        nbt.putDouble(NBT_KEY_STATS_AGILITY, agility);
        nbt.putDouble(NBT_KEY_STATS_LIFT, lift);
        return nbt;
    }

    public static RefBoardStats deserializeNBT(CompoundTag nbt) {
        RefBoardStats out = StandardBoard.copy();
        if (nbt.contains(NBT_KEY_STATS_WEIGHT)) {
            if (nbt.getDouble(NBT_KEY_STATS_WEIGHT) != 0) {
                out.boardWeight = nbt.getDouble(NBT_KEY_STATS_WEIGHT);
            }
        }
        if (nbt.contains(NBT_KEY_STATS_SPEED)) {
            if (nbt.getDouble(NBT_KEY_STATS_SPEED) != 0) {
                out.boardSpeed = nbt.getDouble(NBT_KEY_STATS_SPEED);
            }
        }
        if (nbt.contains(NBT_KEY_STATS_AGILITY)) {
            if (nbt.getDouble(NBT_KEY_STATS_AGILITY) != 0) {
                out.turnSpeed = nbt.getDouble(NBT_KEY_STATS_AGILITY);
            }
        }
        if (nbt.contains(NBT_KEY_STATS_LIFT)) {
            if (nbt.getDouble(NBT_KEY_STATS_LIFT) != 0) {
                out.liftFactor = nbt.getDouble(NBT_KEY_STATS_LIFT);
            }
        }
        return out;
    }

    public RefBoardStats WithWeight(double newWeight) {
        RefBoardStats out = this.copy();
        out.boardWeight = newWeight;
        return out;
    }

    public RefBoardStats WithSpeed(double newSpeed) {
        RefBoardStats out = this.copy();
        out.boardSpeed = newSpeed;
        return out;
    }

    public RefBoardStats WithAgility(double newSpeed) {
        RefBoardStats out = this.copy();
        out.turnSpeed = newSpeed;
        return out;
    }

    public RefBoardStats WithLift(double newSpeed) {
        RefBoardStats out = this.copy();
        out.liftFactor = newSpeed;
        return out;
    }

    public double getLatentBraking() {
        return this.latentBraking;
    }

    public double getLatentAcceleration() {
        return this.latentAccel;
    }
}
