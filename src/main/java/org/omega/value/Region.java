package org.omega.value;

import de.pauleff.jmcx.api.IChunk;
import org.omega.logic.ChunkExtract;

import java.util.List;

public class Region {
    List<Chunk> chunks;

    public Region (List<IChunk> chunks) {
        this.chunks = chunks.stream().map(iChunk -> {
                    try {
                        return ChunkExtract.extract(iChunk);
                    } catch (Exception e) {
                        return null;
                    }
                }
        ).toList();
        for (Chunk chunk : this.chunks) {
            System.out.println(chunk.getChunkCoordinates());
        }
    }
}
