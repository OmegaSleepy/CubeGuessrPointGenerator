package org.omega.util;

import de.pauleff.jmcx.api.IChunk;

import java.io.IOException;

public class Heightmap {
    public static long[] getHeightmap (IChunk chunk) throws IOException {
        if (chunk == null || chunk.getNBTData() == null) return null;

        var heightmaps = chunk.getNBTData().getCompound("Heightmaps");
        if (heightmaps == null) return null;

        long[] data = heightmaps.getLongArray("MOTION_BLOCKING_NO_LEAVES");
        if (data == null || data.length == 0) return null;

        return data;
    }
}
