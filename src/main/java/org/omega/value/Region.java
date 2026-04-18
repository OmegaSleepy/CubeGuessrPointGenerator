package org.omega.value;

import org.omega.logic.AbstractedRegionReader;
import org.omega.util.Clean;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Region {
    private final Chunk[][] chunks = new Chunk[32][32];
    public final int x;
    public final int z;

    public Region(int x, int z, File mcaFile) throws IOException {
        this.x = x;
        this.z = z;

        List<Chunk> chunkList = AbstractedRegionReader.readAndFormat(mcaFile);
        chunkList = Clean.cleanNullChunks(chunkList);

        for (Chunk chunk : chunkList) {
            int localX = getLocal(chunk.chunkX());
            int localZ = getLocal(chunk.chunkZ());

            chunks[localX][localZ] = chunk;
        }
    }

    private int getLocal(int coord) {
        return ((coord % 32) + 32) % 32;
    }

    public Chunk getChunk(int localX, int localZ) {
        return chunks[localX][localZ];
    }

    public Chunk[][] getChunks () {
        return chunks;
    }
}