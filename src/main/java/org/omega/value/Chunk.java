package org.omega.value;

public class Chunk {
    private final int chunkX;
    private final int chunkZ;
    private final Section[] sections;

    public Chunk(int x, int z, int sectionCount) {
        this.chunkX = x;
        this.chunkZ = z;
        this.sections = new Section[sectionCount];
    }

    public Chunk(int x, int z, Section[] sections) {
        this.chunkX = x;
        this.chunkZ = z;
        this.sections = sections;
    }

    public int getChunkX () {
        return chunkX;
    }

    public int getChunkZ () {
        return chunkZ;
    }

    public Section[] getSections () {
        return sections;
    }

    public String getBlock(int x, int y, int z) {
        int sectionIndex = y >> 4; // Same as y / 16
        if (sectionIndex < 0 || sectionIndex >= sections.length) return "minecraft:air";

        Section section = sections[sectionIndex];
        return (section == null) ? "minecraft:air" : section.getBlock(x & 15, y & 15, z & 15);
    }
}