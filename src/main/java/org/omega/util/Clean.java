package org.omega.util;

import de.pauleff.jmcx.api.IChunk;

import java.io.IOException;
import java.util.List;

public class Clean {
    public static List<IChunk> cleanNullChunks(List<IChunk> chunks){
        return chunks.stream().filter(chunk -> {
            try {
                return !chunk.getNBTData().isEmpty();
            } catch (IOException | NullPointerException e) {
                return false;
            }
        }).toList();
    }
}
