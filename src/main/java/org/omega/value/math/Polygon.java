package org.omega.value.math;

import java.util.List;

public record Polygon(List<PointXZ> vertices) {
    public int edgeCount() {
        return vertices.size();
    }

    public boolean isPointInPolygon(PointXZ test) {
        boolean inside = false;
        int n = vertices.size();

        for (int i = 0, j = n - 1; i < n; j = i++) {
            PointXZ pi = vertices.get(i);
            PointXZ pj = vertices.get(j);

            // Check if the point's Z coordinate is between the edge's Z coordinates
            // AND if the point is to the left of the intersection of the edge with the ray
            if (((pi.z() > test.z()) != (pj.z() > test.z())) &&
                    (test.x() < (double)(pj.x() - pi.x()) * (test.z() - pi.z()) / (pj.z() - pi.z()) + pi.x())) {
                inside = !inside;
            }
        }
        return inside;
    }
}