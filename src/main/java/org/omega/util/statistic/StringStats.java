package org.omega.util.statistic;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.text.DecimalFormat;

public class StringStats {

    public static String getFrequencyCSV(List<String> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return "Value,Count,Percentage";
        }

        Map<String, Integer> counts = new HashMap<>();
        for (String s : inputList) {
            counts.put(s, counts.getOrDefault(s, 0) + 1);
        }

        int totalCount = inputList.size();
        StringBuilder csv = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");

        csv.append("Value,Count,Percentage\n");

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / totalCount;

            csv.append(entry.getKey())
                    .append(",")
                    .append(entry.getValue())
                    .append(",")
                    .append(df.format(percentage))
                    .append("%\n");
        }

        return csv.toString();
    }

    public static String getFrequencyCSV(Map<String, Long> counts) {
        if (counts == null || counts.isEmpty()) {
            return "Value,Count,Percentage";
        }

        // Calculate the total sum of all counts to determine percentages
        long totalSum = 0;
        for (long count : counts.values()) {
            totalSum += count;
        }

        StringBuilder csv = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");

        csv.append("Value,Count,Percentage\n");

        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            // Handle division by zero just in case totalSum is 0
            double percentage = (totalSum == 0) ? 0.0 : (entry.getValue() * 100.0) / totalSum;

            csv.append(entry.getKey())
                    .append(",")
                    .append(entry.getValue())
                    .append(",")
                    .append(df.format(percentage))
                    .append("%\n");
        }

        return csv.toString();
    }
}