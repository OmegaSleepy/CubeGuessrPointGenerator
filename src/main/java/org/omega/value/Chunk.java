package org.omega.value;

public record Chunk(int chunkX, int chunkZ, Section[] sections) {
    public Chunk (int x, int z, int sectionCount) {
        this(x, z, new Section[sectionCount]);
    }

    public String getChunkCoordinates () {
        return "X " + chunkX + " Y " + chunkZ;
    }

    public String getBlock(int x, int y, int z) {
        int normalizedY = y + 64;

        int sectionIndex = normalizedY >> 4;

        if (sectionIndex < 0 || sectionIndex >= sections.length) return "minecraft:air";

        Section section = sections[sectionIndex];

        // 3. Use normalizedY for the relative coordinate (y & 15)
        return (section == null) ? "minecraft:air" : section.getBlock(x & 15, normalizedY & 15, z & 15);
    }
}