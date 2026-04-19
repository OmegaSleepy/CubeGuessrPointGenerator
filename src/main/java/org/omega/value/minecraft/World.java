package org.omega.value.minecraft;

import org.omega.value.math.PointXYZ;
import org.omega.value.math.PointXZ;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class World {

    private static final int MAX_REGIONS = 32;
    private static final Map<RegionKey, Region> regionCache = new LinkedHashMap<>(MAX_REGIONS, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<RegionKey, Region> eldest) {
            return size() > MAX_REGIONS;
        }
    };

    private record RegionKey(int x, int z) {
    }

    public static String getBlock (PointXYZ pointXYZ) throws IOException, NullPointerException {
        int chunkX = pointXYZ.x() >> 4;
        int chunkZ = pointXYZ.z() >> 4;

        int regionX = chunkX >> 5; // Same as / 32
        int regionZ = chunkZ >> 5;

        RegionKey key = new RegionKey(regionX, regionZ);

        Region region = regionCache.get(key);

        if (region == null) {
            File regionFile = new File("/home/martin/Documents/old world/s1/world/region/r.%s.%s.mca"
                    .formatted(regionX, regionZ));

            if (!regionFile.exists()) {
                return "minecraft:air";
            }

            region = new Region(regionX, regionZ, regionFile);
            regionCache.put(key, region);
        }

        int relChunkX = Math.floorMod(chunkX, 32);
        int relChunkZ = Math.floorMod(chunkZ, 32);

        return region.getChunk(relChunkX,relChunkZ).getBlock(pointXYZ.x(), pointXYZ.y(), pointXYZ.z());
    }

    public static Region getRegion (PointXZ pointXZ) throws IOException {
        RegionKey key = new RegionKey(pointXZ.x(), pointXZ.z());

        Region region = regionCache.get(key);

        if (region == null) {
            File regionFile = new File("/home/martin/Documents/old world/s1/world/region/r.%s.%s.mca"
                    .formatted(pointXZ.x(), pointXZ.z()));

            if (!regionFile.exists()) {
                return null;
            }

            region = new Region(pointXZ.x(), pointXZ.z(), regionFile);
            regionCache.put(key, region);
        }

        return region;

    }

    public static Chunk getChunkFromBlockCoordinate (PointXZ pointXZ) throws IOException {
        int chunkX = pointXZ.x() >> 4;
        int chunkZ = pointXZ.z() >> 4;

        int regionX = chunkX >> 5; // Same as / 32
        int regionZ = chunkZ >> 5;

        RegionKey key = new RegionKey(regionX, regionZ);

        Region region = regionCache.get(key);

        if (region == null) {
            File regionFile = new File("/home/martin/Documents/old world/s1/world/region/r.%s.%s.mca"
                    .formatted(regionX, regionZ));

            if (!regionFile.exists()) {
                return null;
            }

            region = new Region(regionX, regionZ, regionFile);
            regionCache.put(key, region);
        }

        int relChunkX = Math.floorMod(chunkX, 32);
        int relChunkZ = Math.floorMod(chunkZ, 32);

        return region.getChunk(relChunkX, relChunkZ);

    }
    public static Chunk getChunk (PointXZ pointXZ) throws IOException {

        int regionX = pointXZ.x() >> 5; // Same as / 32
        int regionZ = pointXZ.z() >> 5;

        RegionKey key = new RegionKey(regionX, regionZ);

        Region region = regionCache.get(key);

        if (region == null) {
            File regionFile = new File("/home/martin/Documents/old world/s1/world/region/r.%s.%s.mca"
                    .formatted(regionX, regionZ));

            if (!regionFile.exists()) {
                return null;
            }

            region = new Region(regionX, regionZ, regionFile);
            regionCache.put(key, region);
        }

        return region.getChunk(pointXZ);

    }

    public static boolean chunkExists (PointXZ chunkCoord) {
        try {
            return getChunk(chunkCoord) != null;
        } catch (IOException e) {
            return false;
        }
    }

    public static void clearCache () {
        regionCache.clear();
    }
}