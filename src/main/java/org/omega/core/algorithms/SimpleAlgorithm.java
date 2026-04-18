package org.omega.core.algorithms;

import org.omega.core.Main;
import org.omega.logic.IPointAlgorithm;
import org.omega.util.statistic.RandomPoint;
import org.omega.value.*;

import java.io.IOException;
import java.util.*;

public class SimpleAlgorithm implements IPointAlgorithm {

    private final List<PointXYZ> pointXYZList = new ArrayList<>();
    private final int MIN_DISTANCE = 25;
    private final int MAX_DISTANCE = 800;

    private final Random random = new Random();

    private final List<PointXYZ> centers = List.of(new PointXYZ(650, 80, 560));

    private Set<PointXZ> uniqueRegionCoords = new HashSet<>();

    public SimpleAlgorithm(){
        Set<PointXZ> uniqueRegionCoords = new HashSet<>();

        for (PointXYZ center : centers) {
            int minX = center.x() - MAX_DISTANCE;
            int maxX = center.x() + MAX_DISTANCE;
            int minZ = center.z() - MAX_DISTANCE;
            int maxZ = center.z() + MAX_DISTANCE;

            for (int x = minX; x <= maxX + 512; x += 512) {
                for (int z = minZ; z <= maxZ + 512; z += 512) {
                    PointXZ currentPoint = new PointXZ(Math.min(x, maxX), Math.min(z, maxZ));
                    PointXZ regionCoord = Region.getRegionPointXZFromGlobalBlockXZ(currentPoint);

                    uniqueRegionCoords.add(regionCoord);
                }
            }
        }

        this.uniqueRegionCoords = uniqueRegionCoords;

    }

    @Override
    public PointXYZ generatePoint () {
        var list = new ArrayList<>(uniqueRegionCoords);
        var randomRegionPoint = list.get(random.nextInt(list.size()));

        Chunk randomChunk;
        try {

            Region region = World.getRegion(randomRegionPoint);
            assert region != null;
            var chunkList = region.getChunkHeadersList();
            if (chunkList.isEmpty()) {
                return null;
            }
            var randomChunkCoordinate = chunkList.get(random.nextInt(chunkList.size()));
            randomChunk = region.getChunk(randomChunkCoordinate);

        } catch (IOException e) {
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
            if (passes > 10) return null;
        } while (isInvalidPoint(newPoint));

        var resultCoordinate = new PointXYZ(newPoint.x(), heightmapValue-1, newPoint.z());

        pointXYZList.add(resultCoordinate);
        System.out.println(resultCoordinate + " GENERATED");
        return resultCoordinate;
    }

    private boolean isInvalidPoint(PointXZ pointXZ) {
        for (PointXYZ existing : pointXYZList) {
            if (pointXZ.isWithinRadius(new PointXZ(existing.x(), existing.z()), MIN_DISTANCE)) {
                return true;
            }
        }

        boolean nearCenter = false;
        for (PointXYZ center : centers) {
            if (pointXZ.isWithinRadius(new PointXZ(center.x(), center.z()), MAX_DISTANCE)) {
                nearCenter = true;
                break;
            }
        }

        return !nearCenter;
    }
}
