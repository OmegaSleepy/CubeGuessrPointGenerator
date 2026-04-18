package org.omega.core.algorithms;

import org.omega.core.Main;
import org.omega.logic.IPointAlgorithm;
import org.omega.value.Point;

import java.io.File;
import java.util.*;

public class SimpleAlgorithm implements IPointAlgorithm {

    private final List<Point> pointList = new ArrayList<>();
    private final int MIN_DISTANCE = 150;

    private List<String> mcaFiles;
    private final Random random = new Random();

    public SimpleAlgorithm(){
        mcaFiles = Arrays.stream(Objects.requireNonNull(Main.world.listFiles())).map(File::getName).toList();
    }

    @Override
    public Point generatePoint () {
        
        return null;
    }



    private boolean checkPoint(Point point) {
        boolean flag = false;
        for (Point point1 : pointList) {
            if (!point.proximityCheckXYZ(point1, MIN_DISTANCE)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
