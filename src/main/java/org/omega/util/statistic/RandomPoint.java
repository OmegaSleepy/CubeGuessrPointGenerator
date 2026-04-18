package org.omega.util.statistic;

import org.omega.value.PointXYZ;
import org.omega.value.PointXZ;

import java.util.Random;

public class RandomPoint {
    private static final Random random = new Random();
    public static PointXYZ randomBoundPointXYZ(PointXZ region){

        int xBound = region.x()*512;
        int zBound = region.z()*512;

        return new PointXYZ
                (
                random.nextInt(512)+xBound,
                random.nextInt(160)-63,
                random.nextInt(512)+zBound
                );

    }

    public static PointXZ randomBoundPointXZ(PointXZ region){

        int xBound = region.x()*512;
        int zBound = region.z()*512;

        return new PointXZ
                (
                random.nextInt(512)+xBound,
                random.nextInt(512)+zBound
                );

    }
}
