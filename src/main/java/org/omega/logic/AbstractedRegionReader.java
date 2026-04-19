package org.omega.logic;

import de.pauleff.jmcx.api.IChunk;
import de.pauleff.jmcx.formats.anvil.AnvilReader;
import org.omega.util.Clean;
import org.omega.value.minecraft.Chunk;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AbstractedRegionReader {
    public static List<IChunk> read(File mcaFile) throws IOException {

        try (AnvilReader reader = new AnvilReader(mcaFile)) {
            var region = reader.readRegion();
            return Clean.cleanNullIChunks(region.getChunks());
        } catch (Exception e) {
            throw e;
        }
    }

    public static List<Chunk> readAndFormat(File mcaFile) throws IOException {
        List<IChunk> iChunks = read(mcaFile);
        return iChunks.stream().map(iChunk -> {
                    try {
                        return ChunkExtract.extract(iChunk);
                    } catch (Exception e) {
                        return null;
                    }
                }
        ).toList();
    }
}
