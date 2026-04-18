package org.omega.value;

import de.pauleff.jmcx.api.IChunk;

import java.io.IOException;

public record Chunk(int chunkX, int chunkZ, Section[] sections, long[] heightmap) {

    public PointXZ getChunkCoordinates () {
        return new PointXZ(chunkX, chunkZ);
    }

    public String getBlock (int x, int y, int z) {
        int normalizedY = y + 64;

        int sectionIndex = normalizedY >> 4;

        if (sectionIndex < 0 || sectionIndex >= sections.length) return "minecraft:air";

        Section section = sections[sectionIndex];

        return (section == null) ? "minecraft:air" : section.getBlock(x & 15, normalizedY & 15, z & 15);
    }

    public int getHeightmap(int x, int z) {
        int localX = x & 15;
        int localZ = z & 15;

        int index = (localZ << 4) | localX;

        int bitsPerEntry = 9;
        int bitsPerLong = 64;
        int valuesPerLong = bitsPerLong / bitsPerEntry; // 7 values

        int longIndex = index / valuesPerLong;
        int bitOffset = (index % valuesPerLong) * bitsPerEntry;

        long mask = (1L << bitsPerEntry) - 1;

        return (int) ((heightmap[longIndex] >>> bitOffset) & mask)-64;
    }

    public static long[] getHeightmap (IChunk iChunk) throws IOException {
        if (iChunk == null || iChunk.getNBTData() == null) return null;

        var heightmaps = iChunk.getNBTData().getCompound("Heightmaps");
        if (heightmaps == null) return null;

        long[] data = heightmaps.getLongArray(Heightmaps.WORLD_SURFACE.toString().toUpperCase());
        if (data == null || data.length == 0) return null;

        return data;

    }

    public PointXZ getGlobalFromLocal(PointXZ local) {
        int globalX = local.x() + chunkX*16;
        int globalZ = local.z() + chunkZ*16;

        return new PointXZ(globalX, globalZ);
    }

    public static PointXZ getLocalFromGlobal(PointXZ global) {
        int localX = Math.floorMod(global.x(), 16);
        int localZ = Math.floorMod(global.z(), 16);

        return new PointXZ(localX, localZ);
    }

}