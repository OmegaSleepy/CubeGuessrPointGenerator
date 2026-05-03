package org.example;

import org.omega.value.math.PointXYZ;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PointPlotter {

    public static BufferedImage plotPointsWithPath(List<PointXYZ> points) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("Point list is empty or null");
        }

        // === Bounds ===
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;

        for (PointXYZ p : points) {
            minX = Math.min(minX, p.x());
            maxX = Math.max(maxX, p.x());
            minZ = Math.min(minZ, p.z());
            maxZ = Math.max(maxZ, p.z());
        }

        int width = maxX - minX + 1;
        int height = maxZ - minZ + 1;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int n = points.size();

        // === Draw lines ===
        for (int i = 0; i < n - 1; i++) {
            PointXYZ a = points.get(i);
            PointXYZ b = points.get(i + 1);

            // Mapping: (Current Val - Min Val)
            // This ensures Min is at 0 (Top/Left) and Max is at Width/Height (Bottom/Right)
            int x1 = a.x() - minX;
            int y1 = a.z() - minZ; // Changed: now matches X logic

            int x2 = b.x() - minX;
            int y2 = b.z() - minZ; // Changed: now matches X logic

            float hue = (float) i / Math.max(1, n - 1);
            g.setColor(Color.getHSBColor(hue, 1f, 1f));

            g.drawLine(x1, y1, x2, y2);
        }

        // === Draw points ===
        int pointSize = 64; // (16*4)

        for (int i = 0; i < n; i++) {
            PointXYZ p = points.get(i);

            int x = p.x() - minX;
            int y = p.z() - minZ; // Changed: negative Z is now closer to the top (0)

            float hue = (float) i / Math.max(1, n - 1);
            g.setColor(Color.getHSBColor(hue, 1f, 1f));

            g.fillOval(x - pointSize / 2, y - pointSize / 2, pointSize, pointSize);
        }

        g.dispose();
        return image;
    }
}