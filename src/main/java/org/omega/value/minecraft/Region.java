package org.omega.value.minecraft;

import org.omega.logic.AbstractedRegionReader;
import org.omega.util.Clean;
import org.omega.value.math.PointXZ;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Region {
    private final Chunk[][] chunks = new Chunk[32][32];
    private final List<PointXZ> chunkHeadersList;
    public final int x;
    public final int z;

    public Region (int x, int z, File mcaFile) throws IOException {
        this.x = x;
        this.z = z;

        List<Chunk> chunkList = AbstractedRegionReader.readAndFormat(mcaFile);
        chunkList = Clean.cleanNullChunks(chunkList);
        this.chunkHeadersList = chunkList.stream().map(Chunk::getChunkCoordinates).toList();

        for (Chunk chunk : chunkList) {
            int localX = getLocal(chunk.chunkX());
            int localZ = getLocal(chunk.chunkZ());

            chunks[localX][localZ] = chunk;
        }
    }

    public static PointXZ getRegionPointXZFromGlobalBlockXZ(PointXZ currentPoint) {
        int regionX = currentPoint.x() >> 9;
        int regionZ = currentPoint.z() >> 9;

        return new PointXZ(regionX, regionZ);
    }

    private int getLocal (int coord) {
        return ((coord % 32) + 32) % 32;
    }

    public Chunk getChunk (int localX, int localZ) {
        return chunks[localX][localZ];
    }

    public Chunk getChunk (PointXZ globalXZ) {
        return getChunk(getLocal(globalXZ.x()), getLocal(globalXZ.z()));
    }

    public List<PointXZ> getChunkHeadersList () {
        return chunkHeadersList;
    }

    public Chunk[][] getChunks () {
        return chunks;
    }
}