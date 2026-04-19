package org.example;

import org.omega.core.Main;
import org.omega.value.minecraft.Chunk;
import org.omega.value.minecraft.Region;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.omega.util.Bit.getHeight;

public class RegionDrawer {

    static final int MIN_COLOR = 0x000000;
    static final int MAX_COLOR = 0xFFFFFF;

    public static void main (String[] args) throws InterruptedException {
        File[] files = Main.world.listFiles();

        if (files == null) {
            System.err.println("Could not find region directory or directory is empty.");
            return;
        }

        // Using Virtual Threads to handle I/O-bound tasks (Disk R/W)
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (File file : files) {
                if (!file.getName().endsWith(".mca")) continue;

                executor.submit(() -> {
                    try {
                        processRegionFile(file);
                        System.out.println("Finished: " + file.getName());
                    } catch (Exception e) {
                        System.err.println("Error processing " + file.getName() + ": " + e.getMessage());
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);
        }
    }

    private static void processRegionFile (File file) throws IOException {
        Region region = new Region(0,0, file);
        Chunk[][] chunks = region.getChunks();
        BufferedImage regionImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);

        Arrays.stream(chunks).sequential().forEach(chunkArray -> {
            for (Chunk chunk : chunkArray) {

                drawChunk(regionImage, chunk, chunk.heightmap());
            }
        });

        ImageIO.write(regionImage, "png", new File("export/%s.png".formatted(file.getName())));
    }

    public static void drawChunk (BufferedImage image, Chunk chunk, long[] data) {
        // chunk position INSIDE region (0–31)
        int baseX = chunk.chunkX();
        int baseZ = chunk.chunkZ();

        int r1 = (MIN_COLOR >> 16) & 0xFF, g1 = (MIN_COLOR >> 8) & 0xFF, b1 = MIN_COLOR & 0xFF;
        int r2 = (MAX_COLOR >> 16) & 0xFF, g2 = (MAX_COLOR >> 8) & 0xFF, b2 = MAX_COLOR & 0xFF;

        int worldMin = 62;
        int worldMax = 320;

        for (int i = 0; i < 256; i++) {
            int h = getHeight(data, i);

            // Standardizing coordinates: x increases first, then z
            int x = i % 16;
            int z = i / 16;

            double t = (h - worldMin) / (double) (worldMax - worldMin);
            t = Math.clamp(t, 0, 1);

            int r = (int) (r1 + t * (r2 - r1));
            int g = (int) (g1 + t * (g2 - g1));
            int b = (int) (b1 + t * (b2 - b1));

            image.setRGB(baseX + x, baseZ + z, (r << 16) | (g << 8) | b);
        }
    }


}