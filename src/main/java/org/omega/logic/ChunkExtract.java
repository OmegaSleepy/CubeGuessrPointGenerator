package org.omega.logic;

import de.pauleff.jmcx.api.IChunk;
import de.pauleff.jnbt.core.Tag_Compound;
import org.omega.util.Package;
import org.omega.value.Chunk;

import java.io.IOException;
import java.util.List;

public class ChunkExtract {
    public static Chunk extract(IChunk iChunk) throws IOException, NullPointerException{

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
