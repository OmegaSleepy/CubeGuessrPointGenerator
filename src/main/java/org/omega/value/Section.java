package org.omega.value;

import java.util.List;

public class Section {
    private final List<String> palette;
    private final short[] blockIds; // 4096 entries (16^3)

    public Section(List<String> palette, short[] blockIds) {
        this.palette = palette;
        this.blockIds = blockIds;
    }

    public String getBlock(int relX, int relY, int relZ) {
        int bitsPerBlock = 4; // Calculated from palette size (9 entries)
        int index = (relY << 8) | (relZ << 4) | relX; // Fast way to do (y*256 + z*16 + x)

        int blocksPerLong = 64 / bitsPerBlock;
        int longIndex = index / blocksPerLong;
        int bitOffset = (index % blocksPerLong) * bitsPerBlock;

        // Extract the value
        int paletteIndex = (int) ((blockIds[longIndex] >>> bitOffset) & 0xF);

        return palette.get(paletteIndex);
    }
}