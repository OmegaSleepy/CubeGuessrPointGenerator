package org.omega.logic;

import org.omega.value.Point;

import java.util.ArrayList;
import java.util.List;

public interface IPointAlgorithm {
    default List<Point> genPoints (int number){
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            points.add(generatePoint());
        }
        return points;
    }

    Point generatePoint ();


}
