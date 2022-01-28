package ca.bradj.eurekacraft.vehicles;

public class RefBoardStats {

    public static RefBoardStats BadBoard = new RefBoardStats(1.0, 0.25, 0.25, 0.25);
    public static RefBoardStats HeavyBoard = new RefBoardStats(1.0, 0.5, 0.5, 1.0);
    public static RefBoardStats StandardBoard = new RefBoardStats(0.5, 1.0, 1.0, 1.0);
    public static RefBoardStats EliteBoard = new RefBoardStats(0.25, 1.0, 1.0, 2.0);

    private final double boardWeight;
    private final double boardSpeed;
    private final double turnSpeed;
    private final double liftFactor;

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

}
