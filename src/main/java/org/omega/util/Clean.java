package org.omega.util;

import de.pauleff.jmcx.api.IChunk;
import org.omega.value.minecraft.Chunk;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Clean {
    public static List<IChunk> cleanNullIChunks(List<IChunk> chunks){
        return chunks.stream().filter(chunk -> {
            try {
                return !chunk.getNBTData().isEmpty();
            } catch (IOException | NullPointerException e) {
                return false;
            }
        }).toList();
    }

    public static List<Chunk> cleanNullChunks(List<Chunk> chunks){
        return chunks.stream().filter(Objects::nonNull).toList();
    }
}
