package org.omega.core.algorithms;

import org.omega.logic.IPointAlgorithm;
import org.omega.util.statistic.RandomPoint;
import org.omega.value.math.PointXYZ;
import org.omega.value.math.PointXZ;
import org.omega.value.math.Polygon;
import org.omega.value.minecraft.Chunk;
import org.omega.value.minecraft.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static org.omega.core.Main.putTime;

public class PolygonAlgorithm implements IPointAlgorithm {

    private final Logger logger = Logger.getLogger("PolygonAlgorithm");

    private final List<PointXYZ> pointXYZList = new ArrayList<>();
    private final int MIN_DISTANCE = 16;

    private final Random random = new Random();

    private final List<PointXZ> validChunkCoords;

    public PolygonAlgorithm (List<Polygon> polygons) {
        List<PointXZ> chunksInPolygon = new ArrayList<>();

        for (Polygon polygon : polygons) {
            List<PointXZ> vertices = polygon.vertices();
            if (vertices.isEmpty()) continue;

            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;

            for (PointXZ v : vertices) {
                if (v.x() < minX) minX = v.x();
                if (v.x() > maxX) maxX = v.x();
                if (v.z() < minZ) minZ = v.z();
                if (v.z() > maxZ) maxZ = v.z();
            }

            int minChunkX = minX >> 4;
            int maxChunkX = maxX >> 4;
            int minChunkZ = minZ >> 4;
            int maxChunkZ = maxZ >> 4;

            for (int cx = minChunkX; cx <= maxChunkX; cx++) {
                for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {

                    PointXZ chunkCoord = new PointXZ(cx, cz);
                    PointXZ chunkCenterAsBlocks = new PointXZ((cx << 4) + 8, (cz << 4) + 8);

                    if (polygon.isPointInPolygon(chunkCenterAsBlocks)) {
                        if (World.chunkExists(chunkCoord)) {
                            chunksInPolygon.add(chunkCoord);
                        }
                    }
                }
            }
        }
        System.out.println(chunksInPolygon);
        this.validChunkCoords = chunksInPolygon;
        putTime("polygon algorithm initialized");
    }

    @Override
    public PointXYZ generatePoint () {
        var list = validChunkCoords;
        var randomChunkCoord = list.get(random.nextInt(list.size()));

        Chunk randomChunk;
        try {
            randomChunk = World.getChunk(randomChunkCoord);
        } catch (IOException e) {
            logger.severe("Error reading chunk " + randomChunkCoord);
            return null;
        }

        PointXZ randomCoordinate;
        PointXZ newPoint = null;
        int heightmapValue = 0;

        int passes = 0;
        do {

            randomCoordinate = RandomPoint.randomBoundPointXZ(16);
            var localPoint = Chunk.getLocalFromGlobal(randomCoordinate);
            heightmapValue = randomChunk.getHeightmap(localPoint.x(), localPoint.z());

            newPoint = randomChunk.getGlobalFromLocal(randomCoordinate);

            passes++;
            if (passes > 10) {
                logger.warning("Exhausted chunk " + Chunk.getLocalFromGlobal(randomCoordinate) + " at " + randomChunk.getChunkCoordinates());
                return null;
            }
        } while (isInvalidPoint(newPoint));

        var resultCoordinate = new PointXYZ(newPoint.x(), heightmapValue, newPoint.z());

        pointXYZList.add(resultCoordinate);
        validChunkCoords.remove(randomChunkCoord);
        logger.info(resultCoordinate + " GENERATED");
        return resultCoordinate;
    }

    private boolean isInvalidPoint (PointXZ pointXZ) {
        for (PointXYZ existing : pointXYZList) {
            if (pointXZ.isWithinRadius(new PointXZ(existing.x(), existing.z()), MIN_DISTANCE)) {
                return true;
            }
        }

        return false;
    }
}
