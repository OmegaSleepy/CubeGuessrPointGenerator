package org.omega.logic;

import de.pauleff.jmcx.api.IChunk;
import de.pauleff.jnbt.core.Tag;
import de.pauleff.jnbt.core.Tag_Compound;
import de.pauleff.jnbt.core.Tag_String;
import org.omega.util.Package;
import org.omega.value.Chunk;
import org.omega.value.Heightmaps;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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


        Set<String> biomes = new HashSet<>();

        iChunk.getNBTData().getList("sections").getData().stream()
                .map(el -> (Tag_Compound) el)
                .map(el -> el.getCompound("biomes"))
                .map(el -> el.getList("palette").getData())
                .map(list -> list.stream()
                        .map(el -> (Tag_String) el)
                        .map(Tag::getData).collect(Collectors.toSet())
                ).forEach(biomes::addAll);

        for (String biome : biomes) {
            if (biome.contains("ocean")) return null;
            if (biome.contains("river")) return null;
        }


        long[] heightmap = iChunk.getNBTData().getCompound("Heightmaps").getLongArray(Heightmaps.WORLD_SURFACE.name().toUpperCase());

        if (heightmap==null) throw new NullPointerException("Chunk at: " + iChunk.getX() + " " + iChunk.getZ() + " has a null heightmap");
        return new Chunk(iChunk.getX(),iChunk.getZ(), Package.packageSections(sectionBlockPallets, sectionData), heightmap);
    }
}
