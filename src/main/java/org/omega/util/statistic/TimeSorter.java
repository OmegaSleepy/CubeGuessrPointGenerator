package org.omega.util.statistic;

import java.util.List;
import java.util.Map;

public class TimeSorter {
    public static void printTimestampMap (Map<String, Long> data) {
        // 1. Sort the entries by the Long value (timestamp)
        List<Map.Entry<String, Long>> sortedEntries = data.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList();

        // 2. Iterate and calculate delta
        Long previousTime = null;

        System.out.printf("%-20s | %-10s%n", "Message", "Delta (s)");
        System.out.println("------------------------------------------------------------");

        for (Map.Entry<String, Long> entry : sortedEntries) {
            String msg = entry.getKey();
            long currentTime = entry.getValue();

            // Calculate delta in seconds (current - previous) / 1000.0
            double delta = (previousTime == null) ? 0.0 : (currentTime - previousTime) / 1000.0;

            System.out.printf("%-20s | +%.3fs%n", msg, delta);

            previousTime = currentTime;
        }
    }
}