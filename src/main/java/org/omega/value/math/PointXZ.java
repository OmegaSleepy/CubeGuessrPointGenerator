package org.omega.value.math;

///A point in 2D space
public record PointXZ(int x, int z) {

    public String command() {
        return "/tp @s %d ~ %d".formatted(x,z);
    }

    ///Calculate distance between two points in 2D
    public double distanceXZ(PointXZ other) {
        long dx = (long) other.x - x;
        long dz = (long) other.z - z;
        return dx * dx + dz * dz;
    }

    ///Returns `true` if distance is more than minDistance
    public boolean isWithinRadius (PointXZ other, double radius) {
        long dx = (long) other.x - x;
        long dz = (long) other.z - z;
        return (dx * dx + dz * dz) <= (radius * radius);
    }
}