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

}
