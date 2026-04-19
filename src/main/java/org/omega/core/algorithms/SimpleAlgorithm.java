package org.omega.core.algorithms;

import org.omega.logic.IPointAlgorithm;
import org.omega.util.statistic.RandomPoint;
import org.omega.value.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class SimpleAlgorithm implements IPointAlgorithm {

    private final Logger logger = Logger.getLogger("SimpleAlgorithm");

    private final List<PointXYZ> pointXYZList = new ArrayList<>();
    private final int MIN_DISTANCE = 10;

    private final Random random = new Random();

    private final List<PointXZ> validChunkCoords;

    public SimpleAlgorithm(List<CircleCenter> centers) {
        Set<PointXZ> chunksInRadius = new HashSet<>();

        for (CircleCenter circleCenter : centers) {
            var center = circleCenter.pointXYZ();
            int radius = circleCenter.radius();
            PointXZ centerXZ = new PointXZ(center.x(), center.z());

            int minChunkX = (center.x() - radius) >> 4;
            int maxChunkX = (center.x() + radius) >> 4;
            int minChunkZ = (center.z() - radius) >> 4;
            int maxChunkZ = (center.z() + radius) >> 4;

            for (int cx = minChunkX; cx <= maxChunkX; cx++) {
                for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {

                    PointXZ chunkCoord = new PointXZ(cx, cz);

                    PointXZ chunkCenterAsBlocks = new PointXZ((cx << 4) + 8, (cz << 4) + 8);

                    if (chunkCenterAsBlocks.isWithinRadius(centerXZ, radius)) {
                        if (World.chunkExists(chunkCoord)) {
                            chunksInRadius.add(chunkCoord);
                        }
                    }
                }
            }
        }
        this.validChunkCoords = new ArrayList<>(chunksInRadius);
    }

    @Override
    public PointXYZ generatePoint () {
        var list = new ArrayList<>(validChunkCoords);
        var randomChunkCoord = list.get(random.nextInt(list.size()));

        Chunk randomChunk;
        try {
            randomChunk = World.getChunk(randomChunkCoord);
        } catch (IOException e) {
            logger.severe("Error reading chunk " + randomChunkCoord);
            return null;
        }

        PointXZ randomCoordinate;
        PointXZ newPoint;
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
