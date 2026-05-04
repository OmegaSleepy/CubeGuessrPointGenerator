package org.omega.value.math;

import java.util.*;
import java.util.stream.Collectors;

public class BinnedPointSorting {

    public static List<PointXYZ> binnedPath (List<PointXYZ> points, double gridSize, double clusterRadius) {
        if (points.isEmpty()) return List.of();

        // 1. Group points into Bins
        Map<String, List<PointXYZ>> grid = new HashMap<>();
        for (PointXYZ p : points) {
            int gx = (int) Math.floor(p.x() / gridSize);
            int gy = (int) Math.floor(p.y() / gridSize);
            int gz = (int) Math.floor(p.z() / gridSize);
            String key = gx + "," + gy + "," + gz;
            grid.computeIfAbsent(key, k -> new ArrayList<>()).add(p);
        }

        // 2. Get unique bin coordinates and sort them in a Serpentine/Snake order
        List<int[]> binCoords = grid.keySet().stream()
                .map(s -> Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray())
                .sorted((a, b) -> {
                    // 1. Primary Sort: Z-axis
                    if (a[2] != b[2]) return Integer.compare(a[2], b[2]);

                    // 2. Secondary Sort: Y-axis (Snake)
                    // If Z is odd, flip the Y direction
                    boolean flipY = (a[2] % 2 != 0);
                    if (a[1] != b[1]) {
                        return flipY ? Integer.compare(b[1], a[1]) : Integer.compare(a[1], b[1]);
                    }

                    // 3. Tertiary Sort: X-axis (Double Snake)
                    // If the "Y-row" we are in is odd (considering the Z-flip), flip X direction
                    boolean flipX = (flipY ? (a[1] % 2 == 0) : (a[1] % 2 != 0));
                    return flipX ? Integer.compare(b[0], a[0]) : Integer.compare(a[0], b[0]);
                })
                .collect(Collectors.toList());

        // 3. Process bins in order
        List<PointXYZ> finalPath = new ArrayList<>();
        PointXYZ currentPos = null;

        for (int[] coord : binCoords) {
            String key = coord[0] + "," + coord[1] + "," + coord[2];
            List<PointXYZ> binPoints = grid.get(key);

            // Connect the last point of the previous bin to the closest point in this bin
            while (!binPoints.isEmpty()) {
                PointXYZ best = findNextPoint(binPoints, currentPos, clusterRadius);
                finalPath.add(best);
                binPoints.remove(best);
                currentPos = best;
            }
        }

        return finalPath;
    }

    private static PointXYZ findNextPoint (List<PointXYZ> remaining, PointXYZ current, double radius) {
        if (current == null) return remaining.get(0);

        PointXYZ best = null;
        double bestDist = Double.MAX_VALUE;
        double radiusSq = radius * radius;

        // Try to find within radius first (your original logic)
        for (PointXYZ p : remaining) {
            double d = current.distanceXYZ(p);
            if (d < radiusSq && d < bestDist) {
                bestDist = d;
                best = p;
            }
        }

        // Fallback to absolute nearest in bin
        if (best == null) {
            for (PointXYZ p : remaining) {
                double d = current.distanceXYZ(p);
                if (d < bestDist) {
                    bestDist = d;
                    best = p;
                }
            }
        }
        return best;
    }

    public static List<PointXYZ> untangle (List<PointXYZ> path) {
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 1; i < path.size() - 2; i++) {
                for (int j = i + 1; j < path.size() - 1; j++) {
                    double currentDist = path.get(i - 1).distanceXYZ(path.get(i))
                            + path.get(j).distanceXYZ(path.get(j + 1));
                    double swapDist = path.get(i - 1).distanceXYZ(path.get(j))
                            + path.get(i).distanceXYZ(path.get(j + 1));

                    if (swapDist < currentDist) {
                        // Reverse the segment between i and j
                        Collections.reverse(path.subList(i, j + 1));
                        improved = true;
                    }
                }
            }
        }
        return path;
    }
}