package org.omega.io;

import com.google.gson.Gson;
import org.example.PointPlotter;
import org.omega.value.math.PointXYZ;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Results {
    public static void saveResults (List<PointXYZ> points) {
        saveResults(points, "panorama_coords");
    }

    public static void saveResults (List<PointXYZ> points, String filename) {
        Gson gson = new Gson();
        Path out = Path.of("results/" + filename + ".json");
        try {
            Files.createDirectories(Path.of("results"));
            Files.deleteIfExists(out);
            Files.writeString(out, gson.toJson(points), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void splitAndSave (List<PointXYZ> points, int splits) {
        List<List<PointXYZ>> subsets = splitList(points, splits);
        for (int i = 0; i < subsets.size(); i++) {
            saveResults(subsets.get(i), "panorama_cords_DELETE-" + i);
        }
    }

    private static <T> List<List<T>> splitList (List<T> list, int n) {
        int totalSize = list.size();
        int chunkSize = totalSize / n;
        int remainder = totalSize % n;

        return IntStream.range(0, n)
                .mapToObj(i -> {
                    int start = i * chunkSize + Math.min(i, remainder);
                    int end = start + chunkSize + (i < remainder ? 1 : 0);
                    return list.subList(start, end);
                })
                .filter(sub -> !sub.isEmpty())
                .collect(Collectors.toList());
    }

    public static void plot (List<PointXYZ> points) {
        try {
            ImageIO.write(PointPlotter.plotPointsWithPath(points), "png", new File("results/points.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
