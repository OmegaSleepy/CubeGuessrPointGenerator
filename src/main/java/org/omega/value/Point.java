package org.omega.value;

public record Point(int x, int y, int z) {

    public String command() {
        return "/tp @s %d %d %d".formatted(x, y, z);
    }

    public double distanceXYZ(Point other) {
        long dx = (long) other.x - x;
        long dy = (long) other.y - y;
        long dz = (long) other.z - z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public boolean proximityCheckXYZ(Point other, double minDistance) {
        long dx = (long) other.x - x;
        long dy = (long) other.y - y;
        long dz = (long) other.z - z;

        return (dx * dx + dy * dy + dz * dz) > (minDistance * minDistance);
    }
}