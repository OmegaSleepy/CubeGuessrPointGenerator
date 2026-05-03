package org.omega.core;

import com.google.gson.Gson;
import org.example.PointPlotter;
import org.example.ToPoligon;
import org.omega.core.algorithms.CircleAreaAndFallAlgorithm;
import org.omega.core.algorithms.PolygonAlgorithm;
import org.omega.value.math.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

    static void main () throws IOException {
        long start = System.currentTimeMillis();

        List<PointXYZ> points = getPointXYZS();
        points = points.stream().filter(Objects::nonNull).toList();
        points = BinnedPointSorting.binnedPath(points, 700, 64);
        BinnedPointSorting.untangle(points);
        ImageIO.write(PointPlotter.plotPointsWithPath(points), "png", new File("points.png"));

        System.out.println("points = " + points);
        System.out.println("points_count = " + points.size());

        Gson gson = new Gson();
        Path out = Path.of("panorama_coords.json");
        Files.deleteIfExists(out);
        Files.writeString(out, gson.toJson(points), StandardOpenOption.CREATE_NEW);
        System.out.println(((System.currentTimeMillis() - start) * 1e-3) + "sec");

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

        return algorithm.genPoints(100);
    }

}