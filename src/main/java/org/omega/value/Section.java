package org.omega.value;

import java.util.List;

public class Section {
    private final List<String> palette;
    private final long[] blockIds; // 4096 entries (16^3)

    public Section(List<String> palette, long[] blockIds) {
        this.palette = palette;
        this.blockIds = blockIds;
    }

    public String getBlock(int relX, int relY, int relZ) {
        int bitsPerBlock = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));

        int index = (relY << 8) | (relZ << 4) | relX;

        int blocksPerLong = 64 / bitsPerBlock;
        int longIndex = index / blocksPerLong;
        int bitOffset = (index % blocksPerLong) * bitsPerBlock;

        long mask = (1L << bitsPerBlock) - 1;

        int paletteIndex = (int) ((blockIds[longIndex] >>> bitOffset) & mask);

        return palette.get(paletteIndex);
    }
}