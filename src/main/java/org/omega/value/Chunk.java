package org.omega.value;

public record Chunk(int chunkX, int chunkZ, Section[] sections) {
    public Chunk (int x, int z, int sectionCount) {
        this(x, z, new Section[sectionCount]);
    }

    public String getChunkCoordinates () {
        return "X " + chunkX + " Y " + chunkZ;
    }

    public String getBlock (int x, int y, int z) {
        int sectionIndex = y >> 4; // Same as y / 16
        if (sectionIndex < 0 || sectionIndex >= sections.length) return "minecraft:air";

        Section section = sections[sectionIndex];
        return (section == null) ? "minecraft:air" : section.getBlock(x & 15, y & 15, z & 15);
    }
}