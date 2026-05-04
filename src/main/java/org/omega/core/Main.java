package org.omega.core;

import org.omega.core.algorithms.PolygonAlgorithm;
import org.omega.io.Results;
import org.omega.util.statistic.TimeSorter;
import org.omega.value.math.BinnedPointSorting;
import org.omega.value.math.PointXYZ;
import org.omega.value.math.PointXZ;
import org.omega.value.math.Polygon;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static final File world = new File("/home/martin/PycharmProjects/WebDownloads/region");

    public static final List<PointXZ> regionCoordinates = new ArrayList<>();

    private static final Pattern pattern = Pattern.compile("r\\.(-?\\d+)\\.(-?\\d+)\\.mca");

    static {
        Arrays.stream(Objects.requireNonNull(world.listFiles())).map(File::getName).forEach(fileName -> {
            Matcher matcher = pattern.matcher(fileName);

            if (matcher.matches()) {
                int x = Integer.parseInt(matcher.group(1));
                int z = Integer.parseInt(matcher.group(2));

                regionCoordinates.add(new PointXZ(x, z));
            }
        });
    }

    public static Map<String, Long> splits = new HashMap<>();

    public static void putTime (String msg) {
        splits.put(msg, System.currentTimeMillis());
    }

    static void main () {
        putTime("initial");

        List<PointXYZ> points = getPointXYZS();
        putTime("points generated");

        points = points.stream().filter(Objects::nonNull).toList();
        putTime("points null filtered");

        points = BinnedPointSorting.binnedPath(points, 700, 64);
        putTime("points binned sorting");

        points.removeFirst();
        BinnedPointSorting.untangle(points);
        putTime("points untangled");

        System.out.println("points = " + points);
        System.out.println("points_count = " + points.size());

        Results.splitAndSave(points, 3);
        putTime("points saved");

        Results.plot(points);
        putTime("points plotted");

        TimeSorter.printTimestampMap(splits);
    }

    private static List<PointXYZ> getPointXYZS () {
        Polygon polygon = new Polygon(List.of(
                new PointXZ(-5642, -2008),
                new PointXZ(-5482, -2057),
                new PointXZ(-5061, -1988),
                new PointXZ(-4758, -1430),
                new PointXZ(-4767, -981),
                new PointXZ(-6094, -923),
                new PointXZ(-5986, -1240),
                new PointXZ(-5770, -1439),
                new PointXZ(-5596, -1426),
                new PointXZ(-5559, -1847),
                new PointXZ(-5645, -1872)
        ));

        var algorithm = new PolygonAlgorithm(List.of(polygon));

        return algorithm.genPoints(1200);
    }

}