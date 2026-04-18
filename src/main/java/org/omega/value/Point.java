package org.omega.value;

///A point in 3D space
public record Point(int x, int y, int z) {

    public String command() {
        return "/tp @s %d %d %d".formatted(x, y, z);
    }

    ///Calculate distance between two points in 3D
    public double distanceXYZ(Point other) {
        long dx = (long) other.x - x;
        long dy = (long) other.y - y;
        long dz = (long) other.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    ///Returns `true` if distance is more than minDistance
    public boolean proximityCheckXYZ(Point other, double minDistance) {
        long dx = (long) other.x - x;
        long dy = (long) other.y - y;
        long dz = (long) other.z - z;

        return distanceXYZ(other) > (minDistance * minDistance);
    }
}