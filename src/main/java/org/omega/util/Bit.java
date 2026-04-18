package org.omega.util;

import java.util.List;

public class Bit {
    public static int getHeight(long[] data, int index) {
        int bits = 9;
        int entriesPerLong = 64 / bits; // 7

        int longIndex = index / entriesPerLong;
        int bitOffset = (index % entriesPerLong) * bits;

        if (longIndex >= data.length) return 0;

        return (int) ((data[longIndex] >>> bitOffset) & ((1L << bits) - 1));
    }

    public static String getBlockAt(long[] data, List<String> palette, int x, int y, int z) {
        int bitsPerBlock = 4; // Calculated from palette size (9 entries)
        int index = (y << 8) | (z << 4) | x; // Fast way to do (y*256 + z*16 + x)

        int blocksPerLong = 64 / bitsPerBlock;
        int longIndex = index / blocksPerLong;
        int bitOffset = (index % blocksPerLong) * bitsPerBlock;

        // Extract the value
        int paletteIndex = (int) ((data[longIndex] >>> bitOffset) & 0xF);

        return palette.get(paletteIndex);
    }
}
