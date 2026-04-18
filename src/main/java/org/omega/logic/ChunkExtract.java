package org.omega.logic;

import de.pauleff.jmcx.api.IChunk;
import de.pauleff.jnbt.core.Tag_Compound;
import org.omega.util.Package;
import org.omega.value.Chunk;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChunkExtract {
    public static Chunk extract(IChunk iChunk) throws IOException {
        //chunks -> 16x16x16 sections -> block palette -> List<String> block names -> print
        Set<String> blocks = new HashSet<>();

        List<List<String>> sectionBlockPallets = iChunk.getNBTData().getList("sections").getData().stream()
                .map(el -> (Tag_Compound) el)
                .map(el -> el.getCompound("block_states"))
                .map(el -> el.getList("palette").getData())
                .map(list -> list.stream()
                        .map(el -> (Tag_Compound) el)
                        .map(el -> el.getString("Name")).toList()
                ).toList();

        List<long[]> sectionData = iChunk.getNBTData().getList("sections").getData().stream()
                .map(el -> (Tag_Compound) el)
                .map(el -> el.getCompound("block_states"))
                .map(el -> el.getLongArray("data")).toList();

        return new Chunk(iChunk.getX(),iChunk.getZ(), Package.packageSections(sectionBlockPallets, sectionData));
    }
}
