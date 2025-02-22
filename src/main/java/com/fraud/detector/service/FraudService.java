package com.fraud.detector.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class FraudService {

    private final List<Map<String, Object>> fraudData = Collections.synchronizedList(new ArrayList<>());
    private int cumulativeFraudCount = 0;
    private final List<Integer> recentPredictions = new ArrayList<>();

    // Metric counters
    private int truePositive = 0;
    private int falsePositive = 0;
    private int falseNegative = 0;
    private int trueNegative = 0;

    public List<Map<String, Object>> getFraudData() {
        return new ArrayList<>(fraudData);
    }

    public synchronized void addFraudEntry(LocalDateTime dateTime, Integer prediction, Integer actual) {
        // Update metrics based on prediction vs. actual
        if (prediction == 1 && actual == 1) {
            truePositive++;
        } else if (prediction == 1 && actual == 0) {
            falsePositive++;
        } else if (prediction == 0 && actual == 1) {
            falseNegative++;
        } else if (prediction == 0 && actual == 0) {
            trueNegative++;
        }

        // Update the sliding window of predictions
        recentPredictions.add(prediction);
        if (recentPredictions.size() > 5) {
            recentPredictions.remove(0);
        }

        // When the window is full, update the cumulative fraud count and record data
        if (recentPredictions.size() == 5) {
            int ones = 0, zeros = 0;
            for (Integer p : recentPredictions) {
                if (p == 1) ones++;
                else if (p == 0) zeros++;
            }
            int difference = ones - zeros;
            cumulativeFraudCount += difference;

            Map<String, Object> entry = new HashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = dateTime.format(formatter);
            entry.put("time", formattedTime);
            entry.put("count", cumulativeFraudCount);
            entry.put("val", zeros);  // Count of 0's
            entry.put("valu", ones);  // Count of 1's
            fraudData.add(entry);

            // Keep only the most recent 10 entries
            if (fraudData.size() > 10) {
                fraudData.remove(0);
            }
        }
    }

    public Map<String, Double> getMetrics() {
        double precision = (truePositive + falsePositive) > 0 ?
                (double) truePositive / (truePositive + falsePositive) : 0.0;
        double recall = (truePositive + falseNegative) > 0 ?
                (double) truePositive / (truePositive + falseNegative) : 0.0;
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("precision", precision);
        metrics.put("recall", recall);
        return metrics;
    }

    public Map<String, Object> getModelInterpretability() {
        Map<String, Object> interpretability = new HashMap<>();
        interpretability.put("truePositive", truePositive);
        interpretability.put("falsePositive", falsePositive);
        interpretability.put("falseNegative", falseNegative);
        interpretability.put("trueNegative", trueNegative);
        interpretability.put("description", "Precision shows the fraction of correct fraud predictions out of all fraud predictions. " +
                "Recall shows the fraction of actual fraud cases detected.");
        return interpretability;
    }
}
