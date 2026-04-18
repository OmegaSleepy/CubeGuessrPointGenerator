package org.omega.core;

import org.omega.core.algorithms.SimpleAlgorithm;
import org.omega.value.PointXYZ;
import org.omega.value.PointXZ;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static final File world = new File("/home/martin/Documents/old world/s1/world/region");

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

    static void main () {
        long start = System.currentTimeMillis();
        var algorithm = new SimpleAlgorithm();
        List<PointXYZ> points = algorithm.genPoints(100);
        points = points.stream().filter(Objects::nonNull).toList();
        System.out.println("points = " + points);
        System.out.println("points_count = " + points.size());
        System.out.println(((System.currentTimeMillis()-start)*1e-3) + "sec");
    }

}