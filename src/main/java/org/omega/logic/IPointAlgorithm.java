package org.omega.logic;

import org.omega.value.PointXYZ;

import java.util.ArrayList;
import java.util.List;

public interface IPointAlgorithm {
    default List<PointXYZ> genPoints (int number){
        List<PointXYZ> pointXYZS = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            pointXYZS.add(generatePoint());
        }
        return pointXYZS;
    }

    PointXYZ generatePoint ();


}
