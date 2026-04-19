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

    private final List<CircleCenter> centers = List.of(
            new CircleCenter(new PointXYZ(650, 80, 560), 800));

    private final List<PointXZ> validChunkCoords;

    public SimpleAlgorithm () {
        Set<PointXZ> chunksInRadius = new HashSet<>();

        for (CircleCenter circleCenter : centers) {
            var center = circleCenter.pointXYZ();
            int radius = circleCenter.radius();
            PointXZ centerXZ = new PointXZ(center.x(), center.z());

            int minX = center.x() - radius;
            int maxX = center.x() + radius;
            int minZ = center.z() - radius;
            int maxZ = center.z() + radius;

            for (int x = minX; x <= maxX + 16; x += 16) {
                for (int z = minZ; z <= maxZ + 16; z += 16) {
                    PointXZ currentBlock = new PointXZ(Math.min(x, maxX), Math.min(z, maxZ));
                    PointXZ chunkCoord = Chunk.getChunkPointXZFromGlobalBlockXZ(currentBlock);

                    if (chunkCoord.isWithinRadius(centerXZ, radius)) {
                        if (World.chunkExits(chunkCoord)){
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

        boolean nearCenter = false;

        for (CircleCenter circleCenter : centers) {

            var center = circleCenter.pointXYZ();
            int maxDistance = circleCenter.radius();

            if (pointXZ.isWithinRadius(new PointXZ(center.x(), center.z()), maxDistance)) {
                nearCenter = true;
                break;
            }
        }

        return !nearCenter;
    }
}
